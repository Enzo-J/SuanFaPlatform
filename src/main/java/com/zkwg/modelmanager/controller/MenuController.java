package com.zkwg.modelmanager.controller;


import com.zkwg.modelmanager.entity.Organization;
import com.zkwg.modelmanager.entity.Permission;
import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.entity.vo.MenuVo;
import com.zkwg.modelmanager.request.MenuRequest;
import com.zkwg.modelmanager.request.TenantRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.service.IPermissionService;
import com.zkwg.modelmanager.utils.AIMenuUtil;
import com.zkwg.modelmanager.utils.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/** 
 * 菜单权限表 前端控制器
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IPermissionService menuService;

    /**
     * 添加菜单
     */
//    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping("/create")
    public R create(@RequestBody MenuRequest menuRequest) {
        Permission permission = deserializeMenu(menuRequest);
        menuService.insertSelective(permission);
        return  ResultUtil.success();
    }


    @RequestMapping("/list")
    public R list() {
        AISecurityUser securityUser = SecurityUtil.getUser();
        return ResultUtil.success(menuService.selectMenuTree(securityUser.getUserId()));
    }

    /**
     * 修改菜单
     */
//    @PreAuthorize("hasAuthority('sys:menu:update')")
//    @SysOperaLog(descrption = "修改菜单")
    @PostMapping("/update/{id}")
    public R updateMenu(@PathVariable("id") Integer id, @RequestBody MenuRequest menuRequest) {
        Permission permission = deserializeMenu(menuRequest);
        permission.setId(id);
        permission.setUpdateTime(new Date());
        menuService.updateById(permission);
        return ResultUtil.success();
    }

    /**
     * 根据id删除菜单
     */
//    @PreAuthorize("hasAuthority('sys:menu:delete')")
//    @SysOperaLog(descrption = "删除菜单")
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        Permission permission = menuService.selectByPrimaryKey(id);
        Assert.notNull(permission, "无法找到菜单");
        permission.setIsDelete((byte) 1);
        permission.setUpdateTime(new Date());
        menuService.updateById(permission);
        return ResultUtil.success();
    }

    /**
     * 获取路由
     */
    @GetMapping("/routers")
    public R getRouters() {
        AISecurityUser securityUser = SecurityUtil.getUser();
        List<MenuVo> menuVos = AIMenuUtil.buildMenus(menuService.selectMenuTree(securityUser.getUserId()));
        return ResultUtil.success(menuVos);
    }

    private Permission deserializeMenu(MenuRequest menuRequest) {
        Permission permission = new Permission();
        BeanUtils.copyProperties(menuRequest,permission);
        return permission;
    }

}

