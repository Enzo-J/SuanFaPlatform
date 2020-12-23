package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.Training;
import com.zkwg.modelmanager.entity.TrainingParameter;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.service.base.IBaseService;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface ITrainingService extends IBaseService<Training> {

    void createOrUpdate(Training training, List<TrainingParameter> trainingParameters, boolean isUpdate);

    R<T> run(Training training);

    String log(Training training);

    R<T> stop(Training training);

    void delete(Training training);
}
