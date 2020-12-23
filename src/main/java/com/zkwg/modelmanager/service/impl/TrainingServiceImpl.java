package com.zkwg.modelmanager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.zkwg.modelmanager.core.Handler;
import com.zkwg.modelmanager.core.IMessage;
import com.zkwg.modelmanager.core.JobRunResult;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.k8s.K8sOperator;
import com.zkwg.modelmanager.mapper.TrainingMapper;
import com.zkwg.modelmanager.mapper.TrainingParameterMapper;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.*;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.MD5Util;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl extends BaseService<TrainingMapper, Training> implements ITrainingService, Handler<IMessage<JobRunResult>> {

    private static Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    @Autowired
    private K8sOperator k8sOperator;

    @Autowired
    private IDataSetService dataSetService;

    @Autowired
    private IAlgorithmService algorithmService;

    @Autowired
    private IDsSyncTaskService dsSyncTaskService;

    @Autowired
    private IAlgorithmTypeService algorithmTypeService;

    @Autowired
    private IDataSourceService dataSourceService;

    @Autowired
    private IContainerService containerService;

    private TrainingMapper trainingMapper;

    @Autowired
    private TrainingParameterMapper trainingParameterMapper;

    @Autowired
    public void setModelMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
        this.baseMapper = trainingMapper;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void createOrUpdate(Training training, List<TrainingParameter> trainingParameters, boolean isUpdate) {
        // 1.加载作业模型部署模板；
        InputStream is =  TrainingServiceImpl.class.getClassLoader().getResourceAsStream("deploy/job-template.yml");
        Job jobTemplate = Serialization.unmarshal(is, Job.class);
        ObjectMeta meta = jobTemplate.getMetadata();
        meta.setName("job-name-"+ RandomStringUtils.randomAlphanumeric(5).toLowerCase());
        jobTemplate.setMetadata(meta);

        // 2.获取算法
        //Algorithm algorithm = algorithmService.selectByPrimaryKey(training.getAlgorithmId());
        // 2.1 获取算法类型（算法默认信息）
        AlgorithmType algorithmType = algorithmTypeService.selectByPrimaryKey(training.getAlgorithmId());


        // 3.获取数据集
        DataSet dataSet = dataSetService.selectByPrimaryKey(training.getDataSetId());
        LambdaQueryWrapper lambdaQueryWrapper = new QueryWrapper<Datasource>().lambda().eq(Datasource::getUuid, dataSet.getTarget());
        Datasource datasource = dataSourceService.selectOne(lambdaQueryWrapper);
        DsSyncTask dsSyncTask = dsSyncTaskService.selectByPrimaryKey(dataSet.getDsSyncTaskId());
        // 4.获取容器
        com.zkwg.modelmanager.entity.Container container = containerService.selectByPrimaryKey(Integer.parseInt(training.getContainerIds()));
        // 4.1.修改容器状态
        container.setStatus((byte)2);
        containerService.updateById(container);
        // 5.设置作业属性
        training.setModelId(RandomStringUtils.randomAlphanumeric(32));
        List<String> parameters = new ArrayList<>();
        // 分类和回归需要对数据进行分割
        if (algorithmType.getFirstClassNameEN().equals("classification") || algorithmType.getFirstClassNameEN().equals("regression")) {
            parameters.add(training.getDataSetTrainingProportion().toString());
        }
        for (TrainingParameter trainingParameter: trainingParameters) {
            //parameters.add(MessageFormat.format("\'{0}\'", trainingParameter.getValue()));
            parameters.add(trainingParameter.getValue().replace(" ", ""));
        }
        training.setParameters(String.join("#", parameters));
        List<String> args = getArgs(dsSyncTask,datasource,dataSet,algorithmType,training,trainingParameters);

        Container k8sContainer = Serialization.unmarshal(container.getContainerJson(), Container.class);
        k8sContainer.setImage("self_train:latest");
        k8sContainer.setArgs(args);
//        k8sContainer.setName("job-container-"+RandomStringUtils.randomAlphanumeric(5).toLowerCase());
        k8sContainer.setName(container.getNameEn());
        k8sContainer.setImagePullPolicy("IfNotPresent");
        //
        PodTemplateSpec podTemplateSpec = jobTemplate.getSpec().getTemplate();
        PodSpec podSpec = podTemplateSpec.getSpec();
        podSpec.setContainers(Lists.newArrayList(k8sContainer));
        // 6.完善训练属性
        String jobJsonStr = Serialization.asJson(jobTemplate);
        training.setJobJsonMd5(MD5Util.getMD5(jobJsonStr));
        training.setJobJson(jobJsonStr);
        // 7.保存训练
//        trainingMapper.insertSelective(training);
        if (isUpdate == true)
        {
            trainingMapper.updateById(training);
        }
        else {
            trainingMapper.insert(training);
        }
        // 8.保存训练参数值
//        trainingParameterMapper.insertSelective(trainingParameter);
        //trainingParameterMapper.insert(trainingParameter);
        for (TrainingParameter trainingParameter: trainingParameters) {
            trainingParameter.setTrainingId(training.getId());
            trainingParameterMapper.insert(trainingParameter);
        }
    }

    private List<String> getArgs(DsSyncTask dsSyncTask, Datasource datasource, DataSet dataSet, AlgorithmType algorithmType, Training training, List<TrainingParameter> trainingParameters) {
        /**
         * command:
         *     - "python"
         * args:
         *     - <py_file> <train_id> <alg_type> <alg_name> <table> <db_connection_info> <table_for_model> <alg_params> <training_desc>
         */

        // 构造数据库连接json数据
        Map<String, Object> mapConnInfo = new HashMap<String, Object>();
        //mapConnInfo.put("host", dataSet.getDatabase().split(":")[0]);
        //mapConnInfo.put("port", dataSet.getDatabase().split(":")[1]);
        mapConnInfo.put("host", "mysql.mysql-system.svc.cluster.local");
        mapConnInfo.put("port", "3306");
        mapConnInfo.put("user", datasource.getUsername());
        mapConnInfo.put("password", datasource.getPassword());
        mapConnInfo.put("database", "model");
        //String dbConnInfo = JSON.toJSONString(mapJson).replace("\"", "'");

        // 指定数据表
        mapConnInfo.put("table", dsSyncTask.getTables());
        //String table = dataSet.getTable();

        // 设置训练结束后模型的保存信息
        //String modelId = RandomStringUtils.randomAlphanumeric(32);
        String modelId = training.getModelId();
        //String model = "training_model(model_id,model,description)"; // 字段名之间不可有空格
        String modelTable = "training_model"; // 字段名之间不可有空格

        // 用户选择的算法及设置的参数
        String algType = algorithmType.getFirstClassNameEN();
        String algName = algorithmType.getSecondClassNameEN();
        String description = StrUtil.isEmpty(training.getRemark()) ? "Nothing" : training.getRemark();
//        List<String> parameters = new ArrayList<>();
//        // 分类和回归需要对数据进行分割
//        if (algType.compareTo("classification")==0 || algType.compareTo("regression")==0) {
//            parameters.add(training.getDataSetTrainingProportion().toString());
//        }
//        for (TrainingParameter trainingParameter: trainingParameters) {
//            //parameters.add(MessageFormat.format("\'{0}\'", trainingParameter.getValue()));
//            parameters.add(trainingParameter.getValue().replace(" ", ""));
//        }

        List<String> args = new ArrayList<>();
        args.add("/bin/bash"); // Default
        //args.add("bin/sh"); // Default
        args.add("-c"); // Default
        //args.add("python"); // Default
        args.add(MessageFormat.format("python /code/self_help_training.py {0} {1} {2} {3} {4} {5} {6} {7} {8} {9} {10} {11}",
                modelId, algType, algName,
                mapConnInfo.get("host"), mapConnInfo.get("port").toString(), mapConnInfo.get("user"), mapConnInfo.get("password"), mapConnInfo.get("database"), mapConnInfo.get("table"), modelTable,
                description, training.getParameters()));

        logger.info(args.toString());

        return args.isEmpty() ? null : args;
    }

    @Override
    public R<T> run(Training training) {
        // 设置启动时间
        training.setStartTime(new Date());
        trainingMapper.updateById(training);
        // 启动任务
        Job job = Serialization.unmarshal(training.getJobJson(),Job.class);
        k8sOperator.runJob(job,this);
        return ResultUtil.success();
    }

    @Override
    public String log(Training training) {

        //Job job = Serialization.unmarshal(training.getRuningJobJson(),Job.class);
        Pod pod = Serialization.unmarshal(training.getRuningJobJson(),Pod.class);

        String namespace = pod.getMetadata().getNamespace();
        String name = pod.getMetadata().getName();
        List<Container> containers = pod.getSpec().getContainers();
        Optional<Container> optionalContainer = containers.stream().findFirst();
        Container container = optionalContainer.get();

        String log = k8sOperator.watchPodLog(namespace,name,container.getName());

        return log;
    }

    @Override
    public R<T> stop(Training training) {
        Job job = Serialization.unmarshal(training.getJobJson(),Job.class);
        //k8sOperator.deleteJob(job,this);
        k8sOperator.deleteJob(job);
        // 1.改训练作业的状态
        training.setStatus((byte) 6);
        trainingMapper.updateById(training);

        // 2.改容器的状态
//        Pod pod = Serialization.unmarshal(training.getRuningJobJson(), Pod.class);
        List<Container> containers = job.getSpec().getTemplate().getSpec().getContainers();
        List<com.zkwg.modelmanager.entity.Container> containerList =
                containers.stream().map(m -> {
                    com.zkwg.modelmanager.entity.Container c = new com.zkwg.modelmanager.entity.Container();
                    c.setNameEn(m.getName());
                    return c;
                }).collect(Collectors.toList());
        // 恢复到已分配状态
        containerList = containerList.stream().map(m -> {m.setStatus((byte) 2); return m;}).collect(Collectors.toList());
        trainingMapper.updateById(training);
        containerService.updateStatusByNameEN(containerList);

        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(Training training) {

        Job job =  Serialization.unmarshal(training.getJobJson(), Job.class);
        List<Container> containers = job.getSpec().getTemplate().getSpec().getContainers();
        List<com.zkwg.modelmanager.entity.Container> containerList =
                containers.stream().map(m -> {
                    com.zkwg.modelmanager.entity.Container c = new com.zkwg.modelmanager.entity.Container();
                    c.setNameEn(m.getName());
                    c.setStatus((byte) 1);
                    return c;
                }).collect(Collectors.toList());
        containerService.updateStatusByNameEN(containerList);

        training.setIsDelete((byte) 1);
        trainingMapper.updateById(training);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void handlerMessage(IMessage<JobRunResult> message) {

        JobRunResult jobRunResult = message.getMessage();
        Training training = trainingMapper.selectOne(new QueryWrapper<>(new Training(jobRunResult.getJobJsonMd5())));
        training.setRuningJobJson(jobRunResult.getRunningPodJson());

        Job job = Serialization.unmarshal(jobRunResult.getRunningPodJson(), Job.class);
        List<Container> containers = job.getSpec().getTemplate().getSpec().getContainers();
        List<com.zkwg.modelmanager.entity.Container> containerList =
                containers.stream().map(m -> {
                    com.zkwg.modelmanager.entity.Container c = new com.zkwg.modelmanager.entity.Container();
                    c.setNameEn(m.getName());
                    c.setPodName(job.getMetadata().getName());
                    c.setNamespace(job.getMetadata().getNamespace());
                    return c;
                }).collect(Collectors.toList());
        containerService.updateStatusByNameEN(containerList);

        switch (jobRunResult.getStatus()) {
            case PENDING: // 等待运行
                training.setStatus((byte) 2);
                training.setEndTime(new Date());
                trainingMapper.updateById(training);
                containerList = containerList.stream().map(m -> {m.setStatus((byte) 2); return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containerList);
                break;
            case RUNNING:
                training.setStatus((byte) 3);
                training.setEndTime(new Date());
                trainingMapper.updateById(training);
                containerList = containerList.stream().map(m -> {m.setStatus((byte) 3); return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containerList);
                break;
            case ERROR:
                break;
            case SUCCESS:
                training.setStatus((byte) 5);
                training.setEndTime(new Date());
                trainingMapper.updateById(training);
                containerList = containerList.stream().map(m -> {m.setStatus((byte) 2); return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containerList);
                break;
            case DELETE:
                training.setStatus((byte) 6);
                training.setEndTime(new Date());
                trainingMapper.updateById(training);
                containerList = containerList.stream().map(m -> { m.setStatus((byte) 2); m.setPodName(""); return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containerList);
                break;
        }

    }
}
