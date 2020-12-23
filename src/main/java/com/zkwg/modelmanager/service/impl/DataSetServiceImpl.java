package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.core.dataset.MonitorDataSyncTread;
import com.zkwg.modelmanager.entity.DataSet;
import com.zkwg.modelmanager.entity.DsSyncTask;
import com.zkwg.modelmanager.mapper.DataSetMapper;
import com.zkwg.modelmanager.request.DatasetRequest;
import com.zkwg.modelmanager.service.IDataSetService;
import com.zkwg.modelmanager.service.IDsSyncTaskService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.DolphinSchedulerClientUtils;
import com.zkwg.modelmanager.utils.SecurityUtil;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DataSetServiceImpl extends BaseService<DataSetMapper, DataSet> implements IDataSetService {

    private static Logger logger = LoggerFactory.getLogger(DataSetServiceImpl.class);

    @Autowired
    private IDsSyncTaskService dsSyncTaskService;

    private DataSetMapper dataSetMapper;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public void setModelMapper(DataSetMapper dataSetMapper) {
        this.dataSetMapper = dataSetMapper;
        this.baseMapper = dataSetMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DataSet dataSet, DatasetRequest datasetRequest) throws IOException {

        int processDefId = 0;
        int processInstanceId = 0;
        int taskInstanceId = 0;

        try {

            String processDefinitionJson = datasetRequest.getProcessDefinitionJson();
            Map<String,Object> processDefinitionJsonMap = Serialization.unmarshal(processDefinitionJson, Map.class);
            List<Map<String,Object>> mapList = (List<Map<String, Object>>) processDefinitionJsonMap.get("tasks");
            Optional<String> targetTableOptional = mapList.stream().map(m -> {
                                                        Map<String,String> paramsMap = (Map<String, String>) m.get("params");
                                                        return paramsMap.get("targetTable");
                                                    }).findFirst();
            // 1.向ds创建工作流定义
            Map<String, String> processParams = new HashMap<>();
            processParams.put("processDefinitionJson", processDefinitionJson);
            processParams.put("name", dataSet.getName());
            processParams.put("description","");
            processParams.put("locations",datasetRequest.getLocations());
            processParams.put("connects","[]");
            logger.info(" 创建流程定义参数  {}  " + processParams);
            DolphinSchedulerClientUtils.saveProcess(processParams);

            processDefId = DolphinSchedulerClientUtils.queryProcessDefinitionListPaging(dataSet.getName());
            logger.info("dolphinScheduler  流程定义ID  {}  ", processDefId);
            // 2.上线工作流定义
            DolphinSchedulerClientUtils.releaseProccessDefinition(processDefId, 1);
            // 3.运行工作流
            Map<String, String> startProcessParams = new HashMap<>();
            startProcessParams.put("processDefinitionId",processDefId+"");
            startProcessParams.put("scheduleTime","");
            startProcessParams.put("failureStrategy","CONTINUE");
            startProcessParams.put("warningType","NONE");
            startProcessParams.put("warningGroupId","0");
            startProcessParams.put("execType","");
            startProcessParams.put("startNodeList","");
            startProcessParams.put("taskDependType","TASK_POST");
            startProcessParams.put("runMode","RUN_MODE_SERIAL");
            startProcessParams.put("processInstancePriority","MEDIUM");
            startProcessParams.put("receivers","");
            startProcessParams.put("receiversCc","");
            startProcessParams.put("workerGroup","default");
            logger.info(" 执行流程参数  {}  " + startProcessParams);
            DolphinSchedulerClientUtils.startProcessInstance(startProcessParams);
            // 4.查询运行实例id
            processInstanceId = DolphinSchedulerClientUtils.queryProcessInstanceList(processDefId);
            logger.info("dolphinScheduler  流程实例ID  {}  ", processDefId);
            // 5.查询任务实例id
            taskInstanceId = DolphinSchedulerClientUtils.queryTaskListPaging(processInstanceId);
            // 保存同步任务实例

            DsSyncTask dsSyncTask = new DsSyncTask(processDefId, processInstanceId, taskInstanceId,dataSet.getSource(), dataSet.getTarget(),targetTableOptional.get());
            dsSyncTaskService.insertSelective(dsSyncTask);

            dataSet.setStatus(-1);
            dataSet.setDsSyncTaskId(dsSyncTask.getId());
            dataSetMapper.insert(dataSet);
            // 监控同步状态
            executorService.submit(new MonitorDataSyncTread(SecurityUtil.getUser().getTenantId(), this, dataSet, dsSyncTaskService, dsSyncTask));
        } catch (Exception e) {
            clearDsTask(processDefId, processInstanceId);
            throw new RuntimeException("创建数据集出现异常", e);
        }

    }

    private void clearDsTask(int processDefId, int processInstanceId) throws IOException {

        if (processInstanceId > 0) {
            DolphinSchedulerClientUtils.deleteProcessInstanceById(processInstanceId);
            logger.info("delete processInstanceId");
        }

        if (processDefId > 0) {
            DolphinSchedulerClientUtils.releaseProccessDefinition(processDefId, 0);
            DolphinSchedulerClientUtils.deleteProcessDefinitionById(processDefId);
            logger.info("delete processDefId");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(DataSet dataSet) throws IOException {
        // 清除同步任务
        Integer dsSyncTaskId =  dataSet.getDsSyncTaskId();
        //
        DsSyncTask dsSyncTask = dsSyncTaskService.getById(dsSyncTaskId);
        //
        clearDsTask(dsSyncTask.getProcessDefId(), dsSyncTask.getProcessInstanceId());
        // 删除对应的target数据
        dataSet.setIsDelete(1);
        dataSetMapper.updateById(dataSet);
    }

    @Override
    public String log(DataSet dataSet) throws IOException {
        // 查询同步任务
        Integer dsSyncTaskId =  dataSet.getDsSyncTaskId();
        //
        DsSyncTask dsSyncTask = dsSyncTaskService.getById(dsSyncTaskId);
        // 查询日志
        return DolphinSchedulerClientUtils.queryLog(dsSyncTask.getTaskInstanceId());
    }

    @Override
    public void sync(DataSet dataSet) {
        try {
            // 查询同步任务
            Integer dsSyncTaskId =  dataSet.getDsSyncTaskId();
            //
            DsSyncTask dsSyncTask = dsSyncTaskService.getById(dsSyncTaskId);
            // 执行流程实例
            DolphinSchedulerClientUtils.execute(dsSyncTask.getProcessInstanceId());
            // 查询任务实例id
            int taskInstanceId = DolphinSchedulerClientUtils.queryTaskListPaging(dsSyncTask.getProcessInstanceId());
            Assert.state(taskInstanceId < 0 ,"无法查询任务实例ID, 流程实例ID为 ");
            dsSyncTask.setTaskInstanceId(taskInstanceId);
            // 提交同步任务
            dataSet.setStatus(0);
            // 更新
            dsSyncTaskService.updateById(dsSyncTask);
            dataSetMapper.updateById(dataSet);
            // 监控同步状态
            executorService.submit(new MonitorDataSyncTread(SecurityUtil.getUser().getTenantId(), this, dataSet, dsSyncTaskService, dsSyncTask));
        }  catch (Exception e) {
            throw new RuntimeException("同步出现异常", e);
        }
    }

}
