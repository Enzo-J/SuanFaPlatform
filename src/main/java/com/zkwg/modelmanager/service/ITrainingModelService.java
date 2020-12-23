package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.TrainingModel;
import com.zkwg.modelmanager.service.base.IBaseService;
import com.zkwg.modelmanager.response.R;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;

public interface ITrainingModelService extends IBaseService<TrainingModel> {

    String downloadModel(String modeId, String filename, HttpServletResponse response);
}
