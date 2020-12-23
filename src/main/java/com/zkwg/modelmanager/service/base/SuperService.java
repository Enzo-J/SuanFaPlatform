package com.zkwg.modelmanager.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zkwg.modelmanager.utils.SuperMapper;
import org.omg.SendingContext.RunTime;

import java.util.List;

/**
 * 基于MP的 IService 新增了2个方法： saveBatchSomeColumn、updateAllById
 * 其中：
 * 1，updateAllById 执行后，会清除缓存
 * 2，saveBatchSomeColumn 批量插入
 *
 * @param <T> 实体
 * @author zuihuo
 * @date 2020年03月03日20:49:03
 */
public interface SuperService<T> extends IService<T> {

    /**
     * 批量保存数据
     * <p>
     * 注意：该方法仅仅测试过mysql
     *
     * @param entityList
     * @return
     */
    default boolean saveBatchSomeColumn(List<T> entityList) {
        if (entityList.isEmpty()) {
            return true;
        }
        if (entityList.size() > 5000) {
            throw new RuntimeException();
        }
        return SqlHelper.retBool(((SuperMapper) getBaseMapper()).insertBatchSomeColumn(entityList));
    }

    /**
     * 根据id修改 entity 的所有字段
     *
     * @param entity
     * @return
     */
    boolean updateAllById(T entity);

}
