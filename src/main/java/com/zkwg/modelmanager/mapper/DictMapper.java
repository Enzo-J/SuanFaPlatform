package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkwg.modelmanager.entity.Dict;
import com.zkwg.modelmanager.entity.DictItem;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 */
@Repository
public interface DictMapper extends BaseMapper<Dict> {

    @Select("SELECT sdi.id,sdi.text,sdi.value FROM dict AS sd LEFT JOIN dict_item AS sdi ON sd.id = sdi.dict_id WHERE sd.name=#{dictName} order by sdi.sort")
    List<DictItem> queryDictItemByDictName(String dictName);
}
