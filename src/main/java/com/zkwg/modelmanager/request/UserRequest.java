package com.zkwg.modelmanager.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname UserDTO
 * @Description 用户Dto
 */
@Data
public class UserRequest extends PageInfoRequest {

    private Integer id;
    private String account;
    private String username;
    private Byte userType;
    private Byte sex;
    private String password;
    private Integer deptId;
    private String phone;
    private String email;
    private String avatar;
    private String lockFlag;
    private Integer orgId;
    private List<Integer> roleList;
    private List<Integer> deptList;
    /**
     * 新密码
     */
    private String newPassword;
    private String smsCode;
}
