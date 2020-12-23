package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zkwg.modelmanager.entity.TrainingModel;
import com.zkwg.modelmanager.mapper.TrainingModelMapper;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.ITrainingModelService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
public class TrainingModelServiceImpl extends BaseService<TrainingModelMapper, TrainingModel> implements ITrainingModelService {

    private static Logger logger = LoggerFactory.getLogger(AlgorithmTypeServiceImpl.class);

    private TrainingModelMapper trainingModelMapper;

    @Autowired
    public void setModelMapper(TrainingModelMapper trainingModelMapper) {
        this.trainingModelMapper = trainingModelMapper;
        this.baseMapper = trainingModelMapper;
    }

    @Override
    public String downloadModel(String modeId, String filename, HttpServletResponse response) {
        List<TrainingModel> trainingModels = trainingModelMapper.selectList(new QueryWrapper<TrainingModel>()
                .lambda()
                .eq(TrainingModel::getModel_id, modeId));
        try {
            // 处理成文件提供下载
            response.reset();
            //response.setContentType("application/force-download");
            //response.setContentType("application/octet-stream");
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8") + ".m");
            response.addHeader("Content-Length", String.valueOf(trainingModels.get(0).getModel().length));


            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(trainingModels.get(0).getModel());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "下载成功";
    }
}
