package com.zkwg.modelmanager.service.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkwg.modelmanager.utils.SuperMapper;


/**
 * 不含缓存的Service实现
 * <p>
 * <p>
 * 2，removeById：重写 ServiceImpl 类的方法，删除db
 * 3，removeByIds：重写 ServiceImpl 类的方法，删除db
 * 4，updateAllById： 新增的方法： 修改数据（所有字段）
 * 5，updateById：重写 ServiceImpl 类的方法，修改db后
 *
 * @param <M> Mapper
 * @param <T> 实体
 * @author zuihou
 * @date 2020年02月27日18:15:17
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements SuperService<T> {

    public SuperMapper getSuperMapper() {
        if (baseMapper instanceof SuperMapper) {
            return baseMapper;
        }
        throw new RuntimeException();
    }

    /**
     * 构建没有租户信息的key
     *
     * @param args
     * @return
     */
    protected static String buildKey(Object... args) {
        if (args.length == 1) {
            return String.valueOf(args[0]);
        } else if (args.length > 0) {
            return "";
//            return StrUtil.join(StrPool.COLON, args);
        } else {
            return "";
        }
    }

    /**
     * 构建key
     *
     * @param args
     * @return
     */
    protected String key(Object... args) {
        return buildKey(args);
    }

//    @Override
//    public boolean save(T model) {
//        R<Boolean> result = handlerSave(model);
//        if (result.getDefExec()) {
//            return super.save(model);
//        }
//        return result.getData();
//    }

    /**
     * 处理新增相关处理
     *
     * @param model
     * @return
     */
//    protected R<Boolean> handlerSave(T model) {
//        return R.successDef();
//    }

    /**
     * 处理修改相关处理
     *
     * @param model
     * @return
     */
//    protected R<Boolean> handlerUpdateAllById(T model) {
//        return R.successDef();
//    }

    /**
     * 处理修改相关处理
     *
     * @param model
     * @return
     */
//    protected R<Boolean> handlerUpdateById(T model) {
//        return R.successDef();
//    }

    @Override
    public boolean updateAllById(T model) {
//        R<Boolean> result = handlerUpdateAllById(model);
//        if (result.getDefExec()) {
//            return SqlHelper.retBool(getSuperMapper().updateAllById(model));
//        }
//        return result.getData();
        return false;
    }

    @Override
    public boolean updateById(T model) {
//        R<Boolean> result = handlerUpdateById(model);
//        if (result.getDefExec()) {
//            return super.updateById(model);
//        }
//        return result.getData();
        return false;
    }


}
