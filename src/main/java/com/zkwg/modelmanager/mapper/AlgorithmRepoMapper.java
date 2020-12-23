package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.AlgorithmRepo;
import com.zkwg.modelmanager.request.AlgorithmRepoRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmRepoMapper extends BaseMapper<AlgorithmRepo> {

    IPage<AlgorithmRepo> getAlgorithmRepoVosPage(Page page, @Param("query") AlgorithmRepoRequest algorithmRepoRequest);
}