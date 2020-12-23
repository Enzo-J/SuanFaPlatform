package com.zkwg.modelmanager.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.entity.Training;
import com.zkwg.modelmanager.entity.User;
import com.zkwg.modelmanager.entity.UserRole;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.request.RoleRequest;
import com.zkwg.modelmanager.request.UserRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.IUserService;
import com.zkwg.modelmanager.utils.AIMenuUtil;
import com.zkwg.modelmanager.utils.SecurityUtil;
import io.fabric8.kubernetes.api.model.Pod;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 查看用户列表
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody UserRequest userRequest){

//        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>()
//                .lambda()
//                .like(User::getUsername, userRequest.getUsername())
//                .eq(User::getIsDelete, 0)
//                .orderByAsc(User::getCreateTime);
//        IPage<User> page = userService.findByPage(userRequest.getPageNum(), userRequest.getPageSize(),queryWrapper);
        IPage<User> page = userService.getUserVosPage(userRequest.getPageNum(), userRequest.getPageSize(),userRequest,new DataScope());
        return ResultUtil.success(page);
    }

    /**
     * 查看用户列表
     */
    @PostMapping("/runners")
    public R<T> runners(@RequestBody UserRequest userRequest){

        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .lambda()
                .like(StrUtil.isNotBlank(userRequest.getUsername()), User::getUsername, userRequest.getUsername())
                .eq(User::getUserType, 2)
                .orderByAsc(User::getCreateTime);
        BaseContextHandler.setIgnoreTenantId(true);
        IPage<User> page = userService.findByPage(userRequest.getPageNum(), userRequest.getPageSize(),queryWrapper);
        BaseContextHandler.setIgnoreTenantId(false);
        return ResultUtil.success(page);
    }

    /**
     * 保存用户包括角色和部门
     * @return
     */
    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('sys:user:add')")
    public R create(@RequestBody UserRequest userRequest) {
//        User user = deserializeUserRequest(userRequest);
//        UserRole userRole = deserializeUserRole(userRequest);
        userService.createUser(userRequest);
        return ResultUtil.success();
    }


    /**
     * 更新用户包括角色和部门
     */
//    @SysOperaLog(descrption = "更新用户包括角色和部门")
    @PostMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('sys:user:update')")
    public R update(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        userRequest.setId(id);
        userService.updateUser(userRequest);
        return ResultUtil.success();
    }

    @PostMapping("/runner/update/{id}")
//    @PreAuthorize("hasAuthority('sys:user:update')")
    public R runnerUpdate(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        userRequest.setId(id);
        userRequest.setUserType((byte) 2); // 运营用户
        userService.updateRunner(userRequest);
        return ResultUtil.success();
    }

    /**
     * 删除用户
     */
//    @SysOperaLog(descrption = "根据用户id删除用户包括角色和部门")
    @RequestMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('sys:user:delete')")
    public R delete(@PathVariable("id") Integer userId) {
        User user = userService.selectByPrimaryKey(userId);
        Assert.notNull(user, "无法找到用户");
        user.setIsDelete((byte) 1);
        userService.updateByPrimaryKeySelective(user);
        return ResultUtil.success();
    }


    /**
     * 重置密码
     */
//    @SysOperaLog(descrption = "重置密码")
    @RequestMapping("/reset/{id}")
//    @PreAuthorize("hasAuthority('sys:user:rest')")
    public R restPass(@PathVariable("id") Integer userId) {
        User user =  userService.selectByPrimaryKey(userId);
        Assert.notNull(user, "无法找用户");
        user.setPassword(AIMenuUtil.encode("123456"));
        userService.updateByPrimaryKeySelective(user);
        return ResultUtil.success();
    }


    /**
     * 获取个人信息
     */
    @RequestMapping("/info")
    public R getUserInfo() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("username", SecurityUtil.getUser().getUsername());
        resultMap.put("tenantId", SecurityUtil.getUser().getTenantId());
        resultMap.put("userId", SecurityUtil.getUser().getUserId());
        return ResultUtil.success(resultMap);
    }

    /**
     * 修改密码
     *
     * @return
     */
//    @SysOperaLog(descrption = "修改密码")
//    @PutMapping("updatePass")
//    @PreAuthorize("hasAuthority('sys:user:updatePass')")
//    public R updatePass(@RequestParam String oldPass, @RequestParam String newPass) {
//        // 校验密码流程
//        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUsername(SecurityUtil.getUser().getUsername()));
//        if (!PreUtil.validatePass(oldPass, sysUser.getPassword())) {
//            throw new PreBaseException("原密码错误");
//        }
//        if (StrUtil.equals(oldPass, newPass)) {
//            throw new PreBaseException("新密码不能与旧密码相同");
//        }
//        // 修改密码流程
//        SysUser user = new SysUser();
//        user.setUserId(sysUser.getUserId());
//        user.setPassword(PreUtil.encode(newPass));
//        return R.ok(userService.updateUserInfo(user));
//    }

    /**
     * 检测用户名是否存在 避免重复
     * @return
     */
    @PostMapping("/vailUserName")
    public R vailUserName(@RequestParam String account) {
        User param = new User();
        param.setAccount(account);
        User user = userService.findSecurityUserByUser(param);
        return ResultUtil.success(ObjectUtil.isNull(user));
    }

//    /**
//     * 发送邮箱验证码
//     *
//     * @param to
//     * @param request
//     * @return
//     */
//    @PostMapping("/sendMailCode")
//    public R sendMailCode(@RequestParam String to, HttpServletRequest request) {
//        emailUtil.sendSimpleMail(to, request);
//        return R.ok();
//    }
//
//    /**
//     * 修改密码
//     *
//     * @return
//     */
//    @SysOperaLog(descrption = "修改邮箱")
//    @PutMapping("updateEmail")
//    @PreAuthorize("hasAuthority('sys:user:updateEmail')")
//    public R updateEmail(@RequestParam String mail, @RequestParam String code, @RequestParam String pass, HttpServletRequest request) {
//        // 校验验证码流程
//        String ccode = (String) request.getSession().getAttribute(PreConstant.RESET_MAIL);
//        if (ObjectUtil.isNull(ccode)) {
//            throw new PreBaseException("验证码已过期");
//        }
//        if (!StrUtil.equals(code.toLowerCase(), ccode)) {
//            throw new PreBaseException("验证码错误");
//        }
//        // 校验密码流程
//        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUsername(SecurityUtil.getUser().getUsername()));
//        if (!PreUtil.validatePass(pass, sysUser.getPassword())) {
//            throw new PreBaseException("密码错误");
//        }
//        // 修改邮箱流程
//        SysUser user = new SysUser();
//        user.setUserId(sysUser.getUserId());
//        user.setEmail(mail);
//        return R.ok(userService.updateUserInfo(user));
//    }

    private User deserializeUserRequest(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest,user);
        return user;
    }

}

