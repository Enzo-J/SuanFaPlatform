package com.zkwg.modelmanager.service.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zkwg.modelmanager.entity.User;
import com.zkwg.modelmanager.request.UserRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IBaseService<T> extends IService<T> {

    T selectByPrimaryKey(int id);

    T selectOne(Wrapper<T>  t);

    List<T> selectAll();

    List<T> select(Wrapper<T> t);

    List<T> selectByMap(Map<String, Object> columnMap);

    int insertSelective(T t);

    int updateByPrimaryKeySelective(T t);

    IPage<T> findByPage(Page<T> page, Wrapper<T> queryWrapper);

    IPage<T> findByPage(int pageNum, int pageSize, Wrapper<T> queryWrapper);
}
