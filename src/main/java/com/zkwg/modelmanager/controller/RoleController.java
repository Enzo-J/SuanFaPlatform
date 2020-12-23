package com.zkwg.modelmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.request.RoleRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IRoleService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /**
     * 查看角色列表
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody RoleRequest roleRequest){

//        LambdaQueryWrapper<Role> queryWrapper = new QueryWrapper<Role>()
//                .lambda()
//                .like(Role::getName, roleRequest.getName())
//                .eq(Role::getIsDelete, 0)
//                .orderByAsc(Role::getCreateTime);
//        IPage<Role> page = roleService.findByPage(roleRequest.getPageNum(), roleRequest.getPageSize(),queryWrapper);
        IPage<Role> page = roleService.getRolesPage(roleRequest.getPageNum(), roleRequest.getPageSize(),roleRequest);
        return ResultUtil.success(page);
    }

    /**
     * 新增租户
     *
     */
//    @SysOperaLog(descrption = "新增租户")
    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('sys:tenant:add')")
    public R create(@RequestBody RoleRequest roleRequest) {
        Role role = deserializeRole(roleRequest);
//        roleService.insertSelective(role);
        roleService.createRole(roleRequest);
        return ResultUtil.success();
    }

    private Role deserializeRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setIsDelete((byte) 0);
        BeanUtils.copyProperties(roleRequest,role);
        return role;
    }

    /**
     * 修改角色
     */
    @PostMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('sys:tenant:update')")
    public R updateById(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        roleRequest.setId(id);

        roleService.updateRole(roleRequest);
//        Role role = deserializeRole(roleRequest);
//        role.setId(id);
//        role.setUpdateTime(new Date());
////        role.setStatus((byte) 1);
//        roleService.updateByPrimaryKeySelective(role);
        return ResultUtil.success();
    }


    /**
     * 通过id删除
     */
//    @SysOperaLog(descrption = "删除租户")
    @RequestMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('sys:tenant:del')")
    public R delete(@PathVariable Integer id) {
        Role role = roleService.selectByPrimaryKey(id);
        Assert.notNull(role, "无法找到角色");
        role.setIsDelete((byte) 1);
        role.setStatus((byte) 4);
        return ResultUtil.success(roleService.updateByPrimaryKeySelective(role));
    }
//
//    /**
//     * 设置租户id -- 主要是第三方登录使用 目的需要发送请求 看看后面有没有办法解决
//     *
//     * @param tenantId
//     */
//    @PostMapping("/setting/{tenantId}")
//    public R setting(@PathVariable Integer tenantId) {
//        return R.ok();
//    }


}

