package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.entity.Permission;
import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.entity.User;
import com.zkwg.modelmanager.entity.vo.MenuMetaVo;
import com.zkwg.modelmanager.entity.vo.MenuVo;
import com.zkwg.modelmanager.entity.vo.SysMenu;
import com.zkwg.modelmanager.security.dataScope.DataScopeType;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.util.*;

/**
 * @Classname AIMenuUtil
 * @Description 系统用户工具类
 */
@UtilityClass
public class AIMenuUtil {

    public static User createAdminUser(Tenant tenant) {
        User admin = new User();
        admin.setAccount(PingYinTools.getPinYinHeadChar(tenant.getTenantName()));
        admin.setUsername("admin");
        admin.setPassword(AIMenuUtil.encode("admin"));
        admin.setUserType((byte) 2);
        admin.setStatus((byte) 1);
        admin.setCreateTime(new Date());
        admin.setIsDelete((byte) 0);
        admin.setCreateTime(new Date());
        admin.setOrgId(0);
        admin.setCreator(SecurityUtil.getUser().getUserId());
        return admin;
    }

    public static Role createAdminRole() {
        Role role = new Role();
        role.setName("系统管理员");
        role.setCreator(1);
        role.setIsDelete((byte) 0);
        role.setCreateTime(new Date());
        role.setDsType(DataScopeType.THIS_LEVEL_CHILDREN);
        role.setStatus((byte) 1);
//        role.setType();
        return role;
    }

    public List<MenuVo> buildMenus(List<Permission> sysMenuList) {

        List<MenuVo> list = new LinkedList<>();
        sysMenuList.forEach(sysMenu -> {
                    if (sysMenu != null) {
                        List<Permission> menuDTOList = sysMenu.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(sysMenu.getName());
                        menuVo.setPath(sysMenu.getPath());
                        // 如果不是外链
                        if (sysMenu.getIsFrame() > 0) {
                            if (sysMenu.getParentId().equals(0)) {
                                //一级目录需要加斜杠，不然访问 会跳转404页面
                                menuVo.setPath("/" + sysMenu.getPath());
                                menuVo.setComponent(StringUtils.isEmpty(sysMenu.getComponent()) ? "Layout" : sysMenu.getComponent());
                            } else if (!StringUtils.isEmpty(sysMenu.getComponent())) {
                                menuVo.setComponent(sysMenu.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(sysMenu.getName(), sysMenu.getIcon()));
                        if (menuDTOList != null && menuDTOList.size() != 0 && sysMenu.getType() == 0) {
                            menuVo.setChildren(buildMenus(menuDTOList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (sysMenu.getParentId().equals(0)) {
                            menuVo.setAlwaysShow(false);
                            menuVo.setRedirect("noredirect");
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (sysMenu.getIsFrame() < 0) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(sysMenu.getPath());
                            }
//                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
//                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    /**
     * 遍历菜单
     *
     * @param menuList
     * @param menus
     * @param menuType
     */
//    public void findChildren(List<SysMenu> menuList, List<SysMenu> menus, int menuType) {
//        for (SysMenu sysMenu : menuList) {
//            List<SysMenu> children = new ArrayList<>();
//            for (SysMenu menu : menus) {
//                if (menuType == 1 && menu.getType() == 2) {
//                    // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
//                    continue;
//                }
//                if (sysMenu.getMenuId() != null && sysMenu.getMenuId().equals(menu.getParentId())) {
//                    menu.setParentName(sysMenu.getName());
//                    menu.setLevel(sysMenu.getLevel() + 1);
//                    if (exists(children, menu)) {
//                        children.add(menu);
//                    }
//                }
//            }
//            sysMenu.setChildren(children);
//            children.sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
//            findChildren(children, menus, menuType);
//        }
//    }

    /**
     * 构建部门tree
     *
     * @param sysDepts
     * @param depts
     */
//    public void findChildren(List<SysDept> sysDepts, List<SysDept> depts) {
//
//        for (SysDept sysDept : sysDepts) {
//            List<SysDept> children = new ArrayList<>();
//            for (SysDept dept : depts) {
//                if (sysDept.getDeptId() != null && sysDept.getDeptId().equals(dept.getParentId())) {
//                    dept.setParentName(sysDept.getName());
//                    dept.setLevel(sysDept.getLevel() + 1);
//                    children.add(dept);
//                }
//            }
//            sysDept.setChildren(children);
//            findChildren(children, depts);
//        }
//    }

    /**
     * 构建部门tree
     *
     * @param sysDepts
     * @param depts
     */
//    public void findChildren1(List<DeptTreeVo> sysDepts, List<SysDept> depts) {
//
//        for (DeptTreeVo sysDept : sysDepts) {
//            sysDept.setId(sysDept.getId());
//            sysDept.setLabel(sysDept.getLabel());
//            List<DeptTreeVo> children = new ArrayList<>();
//            for (SysDept dept : depts) {
//                if (sysDept.getId() == dept.getParentId()) {
//                    DeptTreeVo deptTreeVo1 = new DeptTreeVo();
//                    deptTreeVo1.setLabel(dept.getName());
//                    deptTreeVo1.setId(dept.getDeptId());
//                    children.add(deptTreeVo1);
//                }
//            }
//            sysDept.setChildren(children);
//            findChildren1(children, depts);
//        }
//    }

    /**
     * 判断菜单是否存在
     *
     * @param sysMenus
     * @param sysMenu
     * @return
     */
//    public boolean exists(List<SysMenu> sysMenus, SysMenu sysMenu) {
//        boolean exist = false;
//        for (SysMenu menu : sysMenus) {
//            if (menu.getMenuId().equals(sysMenu.getMenuId())) {
//                exist = true;
//            }
//        }
//        return !exist;
//    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param rawPass
     * @return
     */
    public String encode(String rawPass) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPass);
    }


    /**
     * 校验密码
     *
     * @param newPass
     * @param passwordEncoderOldPass
     * @return
     */
    public boolean validatePass(String newPass, String passwordEncoderOldPass) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(newPass, passwordEncoderOldPass);
    }

    /**
     * 不重复的验证码
     *
     * @param i
     * @return
     */
    public String codeGen(int i) {
        char[] codeSequence = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I',
                'O', 'P', 'L', 'K', 'J', 'H', 'G', 'F', 'D',
                'S', 'A', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '1',
                '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        while (true) {
            // 随机产生一个下标，通过下标取出字符数组中对应的字符
            char c = codeSequence[random.nextInt(codeSequence.length)];
            // 假设取出来的字符在动态字符中不存在，代表没有重复的
            if (stringBuilder.indexOf(c + "") == -1) {
                stringBuilder.append(c);
                count++;
                //控制随机生成的个数
                if (count == i) {
                    break;
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws ParseException {
//        BasicTextEncryptor encryptor = new BasicTextEncryptor();
//        encryptor.setPassword("EbfYkitulv73I2p0mXI50JMXoaxZTKJ1");
//        System.out.println(encryptor.decrypt("upnvZ1wV5hzuS7Y8UixYJv1tsxNMUHgfnPCIY6Rh3liDh729Ro11+aCDJzI4mILg6eygkozi/NBWXIxhaAE20mS6Bi231/zR+dR66MqfCrJTKFBTLl+MRxt5BPwa6Y0rDzyeP1YOaCLc7FmZsq5uH+mkbH/xiOfbeeZhVjGBwNXhVTFqxgtb0wbgzvh996PCzMDLsT36nA+J6xtW+zgZZb3vshSCoHv3BrwiludhiV8="));
        String admin = encode("admin");
        System.out.println(admin);
    }

}
