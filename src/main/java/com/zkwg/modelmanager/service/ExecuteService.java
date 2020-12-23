package com.zkwg.modelmanager.service;

import com.alibaba.fastjson.TypeReference;
import com.zkwg.modelmanager.entity.BaseModelParam;
import com.zkwg.modelmanager.entity.PurSelSto;
import com.zkwg.modelmanager.entity.TaxpayerAddressSingle;
import com.zkwg.modelmanager.utils.Result;

public interface ExecuteService {


    <T> Result executeModel(BaseModelParam baseModelParam, String url, TypeReference<T> tf);

//    Result executeTtaxpayerAddress(BaseModelParam baseModelParam);
//
//    Result executeSinglePurSelSto(BaseModelParam baseModelParam);
}
