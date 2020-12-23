package com.zkwg.modelmanager.core.model;

import com.zkwg.modelmanager.entity.DeployProcessDef;
import com.zkwg.modelmanager.entity.Model;
import org.springframework.web.multipart.MultipartFile;

public abstract class ModelProcess {

    public abstract void process(Model model) throws Exception;

    public abstract DeployProcessDef generateProcessDef(String template, Model model) throws Exception;
}
