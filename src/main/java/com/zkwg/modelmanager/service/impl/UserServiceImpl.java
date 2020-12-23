package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.entity.RoleOrg;
import com.zkwg.modelmanager.entity.User;
import com.zkwg.modelmanager.entity.UserRole;
import com.zkwg.modelmanager.mapper.UserMapper;
import com.zkwg.modelmanager.request.UserRequest;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.security.dataScope.DataScopeType;
import com.zkwg.modelmanager.service.IPermissionService;
import com.zkwg.modelmanager.service.IRoleService;
import com.zkwg.modelmanager.service.IUserRoleService;
import com.zkwg.modelmanager.service.IUserService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.AIMenuUtil;
import com.zkwg.modelmanager.utils.JwtUtil;
import com.zkwg.modelmanager.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseService<UserMapper, User> implements IUserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setModelMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.baseMapper = userMapper;
    }

    @Override
    public String login(String username, String password) {
        //用户验证
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        AISecurityUser userDetail = (AISecurityUser) authentication.getPrincipal();

        return JwtUtil.generateToken(userDetail);
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public void createUser(UserRequest userRequest) {
        // 插入用户信息
        User user = new User();
        BeanUtils.copyProperties(userRequest,user);
        user.setPassword(AIMenuUtil.encode("123456"));
        user.setOrgId(userRequest.getDeptId());
        user.setCreateTime(new Date());
        user.setCreator(SecurityUtil.getUser().getUserId());
        userMapper.insert(user);
        //  插入用户角色信息
        List<Integer> roleList = userRequest.getRoleList();
        List<UserRole> userRoles = roleList.stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
    }

    @Override
    public Map<String, Object> getDataScopeById(Integer userId) {
        Map<String, Object> map = new HashMap<>(2);
        List<Long> orgIds = new ArrayList<>();
        DataScopeType dsType = DataScopeType.SELF;

//        List<Role> list = roleService.findRoleByUserId(userId);
//
//        // 找到 dsType 最大的角色， dsType越大，角色拥有的权限最大
//        Optional<Role> max = list.stream().max(Comparator.comparingInt((item) -> item.getDsType().getVal()));

//        if (max.isPresent()) {
//            Role role = max.get();
//            dsType = role.getDsType();
//            map.put("dsType", dsType.getVal());
//            if (DataScopeType.CUSTOMIZE.eq(dsType)) {
//                LbqWrapper<RoleOrg> wrapper = Wraps.<RoleOrg>lbQ().select(RoleOrg::getOrgId).eq(RoleOrg::getRoleId, role.getId());
//                List<RoleOrg> roleOrgList = roleOrgService.list(wrapper);
//
//                orgIds = roleOrgList.stream().mapToLong(RoleOrg::getOrgId).boxed().collect(Collectors.toList());
//            } else if (DataScopeType.THIS_LEVEL.eq(dsType)) {
//                User user = getByIdCache(userId);
//                if (user != null) {
//                    Long orgId = RemoteData.getKey(user.getOrg());
//                    if (orgId != null) {
//                        orgIds.add(orgId);
//                    }
//                }
//            } else if (DataScopeType.THIS_LEVEL_CHILDREN.eq(dsType)) {
//                User user = getByIdCache(userId);
//                if (user != null) {
//                    Long orgId = RemoteData.getKey(user.getOrg());
//                    List<Org> orgList = orgService.findChildren(Arrays.asList(orgId));
//                    orgIds = orgList.stream().mapToLong(Org::getId).boxed().collect(Collectors.toList());
//                }
//            }
//        }
        map.put("dsType", dsType.getVal());
        map.put("orgIds", orgIds);
        return map;
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public void updateUser(UserRequest userRequest) {
        // 修改用户信息
        User user = new User();
        BeanUtils.copyProperties(userRequest,user);
        user.setOrgId(userRequest.getDeptId());
        user.setUpdateTime(new Date());
        this.updateByPrimaryKeySelective(user);
        // 删除原有关联
        userRoleService.remove(new QueryWrapper<>(new UserRole(user.getId(), null)));
        //  修改用户角色信息
        List<Integer> roleList = userRequest.getRoleList();
        List<UserRole> userRoles = roleList.stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
    }

    @Override
    public Set<String> findPermsByUserId(Integer userId) {
        return permissionService.findPermsByUserId(userId).stream().filter(StringUtils::isNoneEmpty).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findRoleIdByUserId(int userId) {
        return roleService.selectUserRoleListByUserId(userId);
    }

    @Override
    public User findSecurityUserByUser(User sysUser) {
        return null;
    }

    @Override
    public IPage<User> getUserVosPage(int pageNum, int pageSize, UserRequest userRequest, DataScope dataScope) {
        return userMapper.getUserVosPage(new Page<T>(pageNum, pageSize) , userRequest, dataScope);
    }

    @Override
    public void updateRunner(UserRequest userRequest) {
        // 修改用户信息
        User user = new User();
        BeanUtils.copyProperties(userRequest,user);
        user.setOrgId(userRequest.getDeptId());
        user.setUpdateTime(new Date());
//        this.updateByPrimaryKeySelective(user);
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new UpdateWrapper<User>()
                                    .lambda()
                                    .eq(User::getId, userRequest.getId())
                                    .eq(User::getUserType, userRequest.getUserType());
        BaseContextHandler.setIgnoreTenantId(true);
        userMapper.update(user, lambdaUpdateWrapper);
        BaseContextHandler.setIgnoreTenantId(false);
    }
}
