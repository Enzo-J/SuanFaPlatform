package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkwg.modelmanager.entity.DeployProcessDef;
import org.springframework.stereotype.Repository;

@Repository
public interface DeployProcessDefMapper extends BaseMapper<DeployProcessDef> {

    void insertAndGetId(DeployProcessDef deployProcessDef);
}