package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkwg.modelmanager.entity.AlgorithmParameter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgorithmParameterMapper extends BaseMapper<AlgorithmParameter> {

    List<AlgorithmParameter> selectListByGroupId(@Param("groupId") Integer groupId);

}