package com.zkwg.modelmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.entity.Training;
import com.zkwg.modelmanager.entity.TrainingModel;
import com.zkwg.modelmanager.entity.TrainingParameter;
import com.zkwg.modelmanager.k8s.K8sOperator;
import com.zkwg.modelmanager.request.AlgorithmRequest;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.request.TrainingRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IAlgorithmService;
import com.zkwg.modelmanager.service.IAlgorithmTypeService;
import com.zkwg.modelmanager.service.ITrainingModelService;
import com.zkwg.modelmanager.service.ITrainingService;
import io.swagger.models.Response;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingManagerController {

    private static Logger logger = LoggerFactory.getLogger(TrainingManagerController.class);

    @Autowired
    private ITrainingService trainingService;

    @Autowired
    private ITrainingModelService trainingModelService;

    /**
     * 创建训练实例
     */
    @PostMapping("/create")
    public R<T> create(@RequestBody TrainingRequest trainingRequest){
        Training training = deserializeTrainingRequest(trainingRequest);
        List<TrainingParameter> trainingParameters = trainingRequest.getTrainingParameterList();
        trainingService.createOrUpdate(training, trainingParameters, false);
        return ResultUtil.success(training);
    }

    @PostMapping("/run/{id:[0-9]*}")
    public R<T> run(@PathVariable("id") Integer id){
        Training  training = trainingService.selectByPrimaryKey(id);
        return trainingService.run(training);
    }

    @PostMapping("/log/{id:[0-9]*}")
    public R<T> log(@PathVariable("id") Integer id){
        Training  training = trainingService.selectByPrimaryKey(id);
        return ResultUtil.success(trainingService.log(training));
    }

    @PostMapping("/stop/{id:[0-9]*}")
    public R<T> stop(@PathVariable("id") Integer id){
        Training  training = trainingService.selectByPrimaryKey(id);
        return trainingService.stop(training);
    }

    @GetMapping("/result/{modelId}")
    public String result(@PathVariable("modelId") String modelId, HttpServletResponse response){
        Training training = trainingService.selectOne(new QueryWrapper<Training>()
                .lambda()
                .eq(Training::getModelId, modelId));

        if (training == null)
        {
            return "下载失败";
        }

        return trainingModelService.downloadModel(modelId, training.getName(), response);
    }

    /**
     * 查找训练列表
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody TrainingRequest trainingRequest){

        LambdaQueryWrapper<Training> queryWrapper = new QueryWrapper<Training>()
                .lambda()
                .like(StrUtil.isNotBlank(trainingRequest.getName()), Training::getName, trainingRequest.getName())
                .eq(Training::getIsDelete, 0)
                .orderByDesc(Training::getCreateTime);
        IPage<Training> page = trainingService.findByPage(trainingRequest.getPageNum(), trainingRequest.getPageSize(),queryWrapper);
        return ResultUtil.success(page);
    }

    @PostMapping("/search/{id:[0-9]*}")
    public R<T> search(@PathVariable int id){
        return ResultUtil.success(trainingService.selectByPrimaryKey(id));
    }

    /**
     * 训练详情
     */
    @RequestMapping("/detail/{id:[0-9]*}")
    public R detail(@PathVariable int id){
        Training  training = trainingService.selectByPrimaryKey(id);
        return ResultUtil.success(training);
    }

    /**
     * 删除训练
     */
    @RequestMapping("/delete/{id:[0-9]*}")
    public R delete(@PathVariable int id){
        Training  training = trainingService.selectByPrimaryKey(id);
        trainingService.delete(training);
        return ResultUtil.success();
    }

    /**
     * 更新训练
     * @param trainingRequest
     * @return
     */
    @PostMapping("/update/{id:[0-9]*}")
    public R<T> update(@PathVariable int id, @RequestBody TrainingRequest trainingRequest){
        Training training = deserializeTrainingRequest(trainingRequest);
        training.setId(id);
        List<TrainingParameter> trainingParameters = trainingRequest.getTrainingParameterList();
        trainingService.createOrUpdate(training,trainingParameters, true);
        return ResultUtil.success(training);
    }

//    @PostMapping("/update/{id:[0-9]*}")
//    public R<T> update(@PathVariable("id") int id){
//        Training  training = trainingService.selectByPrimaryKey(id);
//        training.setIsDelete((byte) 1);
//        trainingService.updateByPrimaryKeySelective(training);
//        return ResultUtil.success();
//    }
//

//
//
//    /**
//     * 根据状态判定操作是否可以执行
//     * @param id 资源池ID
//     * @param operator 检测操作：1.删除
//     */
//    private Container checkContainerStatus(int id,Operator operator) {
//
//        Container container = containerService.selectByPrimaryKey(id);
//        Assert.notNull(container,"容器不存在！");
//
//        byte status = container.getStatus();
//        // 1.未分配 2.已分配 3.运行中 4.超时 5.失败 6.停止
//        switch (operator) {
//            case CREATE:
//                break;
//            case DELETE:
//                if(status != 1){
//                    throw new RuntimeException("容器已分配不能删除！");
//                }
//                break;
//            case UPDATE:
//                if(status != 1){
//                    throw new RuntimeException("容器已分配不能修改！");
//                }
//                break;
//            case MONITOR:
//                if(status != 3){
//                    throw new RuntimeException("容器未运行，不能监控！");
//                }
//                break;
//        }
//
//        return container;
//    }
//
//
    /**
     * 解析trainingRequest参数
     * @param trainingRequest
     * @return
     */
    private Training deserializeTrainingRequest(TrainingRequest trainingRequest){
        Training training = new Training();
        BeanUtils.copyProperties(trainingRequest,training);

        training.setStatus((byte) 1);
        training.setCreateTime(new Date());
        return training;
    }
//
//    private void checkContainerJson(String containerJson) {
//        try {
//            io.fabric8.kubernetes.api.model.Container container =  Serialization.unmarshal(containerJson, io.fabric8.kubernetes.api.model.Container.class);
//            //TODO 检查分配资源是否合理
//            logger.info(container.toString());
//        } catch (Exception e) {
//            throw new RuntimeException("containerJson 格式不正确！");
//        }
//    }

    private enum Operator {
        CREATE,
        DELETE,
        UPDATE,
        MONITOR
    }

}
