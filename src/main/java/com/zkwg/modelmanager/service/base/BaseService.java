package com.zkwg.modelmanager.service.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkwg.modelmanager.core.base.AIConfig;
import com.zkwg.modelmanager.core.manager.ContainerManager;
import com.zkwg.modelmanager.core.manager.DeployProcessDefManager;
import com.zkwg.modelmanager.core.manager.ModelManager;
import com.zkwg.modelmanager.core.manager.ServerManager;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 通用增删查看、分页查询
 */
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    public BaseMapper<T> baseMapper;

    @Override
    public T selectByPrimaryKey(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public T selectOne(Wrapper<T> t) {
        return baseMapper.selectOne(t);
    }

    @Override
    public List<T> selectAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<T> select(Wrapper<T> t) {
        return baseMapper.selectList(t);
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        return baseMapper.selectByMap(columnMap);
    }

    @Override
    public int insertSelective(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return baseMapper.updateById(t);
    }

    @Override
    public IPage<T> findByPage(Page<T>  page, Wrapper<T> queryWrapper) {
        return baseMapper.selectPage(page,queryWrapper);
//        return  PageHelper.startPage(param).doSelectPageInfo( () -> baseMapper.selectByExample(example) );
    }

    @Override
    public IPage<T> findByPage(int pageNum, int pageSize, Wrapper<T> queryWrapper) {
        return baseMapper.selectPage(new Page<T>(pageNum, pageSize), queryWrapper);
    }

    public AIConfig getConfig() {
        AIConfig aiConfig = AIConfig.getInstanceFromEnv();
        Assert.notNull(aiConfig,"Failed to load AI config instance");
        return aiConfig;
    }

    public ContainerManager getContainerManager() {
        return ContainerManager.getInstance(getConfig());
    }

    public ModelManager getModelManager() {
        return ModelManager.getInstance(getConfig());
    }

    public DeployProcessDefManager getDeployProcessDefManager() {
        return DeployProcessDefManager.getInstance(getConfig());
    }

    public ServerManager getServerManager() {
        return ServerManager.getInstance(getConfig());
    }
}
