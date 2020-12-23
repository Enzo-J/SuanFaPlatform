package com.zkwg.modelmanager.controller;


import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author lihaodong
 * @since 2019-05-17
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private IDictService dictService;

//    /**
//     * 添加字典信息
//     *
//     * @param sysDict
//     * @return
//     */
//    @SysOperaLog(descrption = "添加字典信息")
//    @PreAuthorize("hasAuthority('sys:dict:add')")
//    @PostMapping
//    public R add(@RequestBody SysDict sysDict) {
//        return R.ok(dictService.save(sysDict));
//    }
//
//    /**
//     * 获取字典列表集合
//     *
//     * @param page
//     * @param sysDict
//     * @return
//     */
//    @SysOperaLog(descrption = "查询字典集合")
//    @GetMapping
//    @PreAuthorize("hasAuthority('sys:dipt:view')")
//    public R getList(Page page, SysDict sysDict) {
//        return R.ok(dictService.page(page, Wrappers.query(sysDict)));
//    }
//
//
//    /**
//     * 更新字典
//     *
//     * @param dictDto
//     * @return
//     */
//    @SysOperaLog(descrption = "更新字典")
//    @PreAuthorize("hasAuthority('sys:dict:edit')")
//    @PutMapping
//    public R update(@RequestBody DictDTO dictDto) {
//        return R.ok(dictService.updateDict(dictDto));
//    }
//
//
//    /**
//     * 根据id删除字典
//     *
//     * @param id
//     * @return //
//     */
//    @SysOperaLog(descrption = "根据id删除字典")
//    @PreAuthorize("hasAuthority('sys:dict:del')")
//    @DeleteMapping("{id}")
//    public R delete(@PathVariable("id") int id) {
//        return R.ok(dictService.removeById(id));
//    }


    /**
     * 根据字典名称查询字段详情
     * @param dictName
     * @return
     */
    @PostMapping("/dictName/{dictName}")
    public R queryDictItemByDictName(@PathVariable("dictName") String dictName) {
        return ResultUtil.success(dictService.queryDictItemByDictName(dictName));
    }

}

