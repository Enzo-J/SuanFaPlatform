package com.zkwg.modelmanager.controller;

import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname IndexController
 * @Description 主页模块
 */
@Controller
public class IndexController {

    @Autowired
    private IUserService userService;

//    @Autowired
//    private SmsCodeService smsCodeService;

//    @Autowired
//    private ProviderSignInUtils providerSignInUtils;

//    @Autowired
//    private SocialRedisHelper socialRedisHelper;

//    @Value("${ai.url.address}")
//    private String url;

//    @Autowired
//    private RedisTemplate<Object, Object> redisTemplate;


//    @PostMapping("/register")
//    public R register(@RequestBody UserDTO userDTO) {
//        Object redisCode = redisTemplate.opsForValue().get(userDTO.getPhone());
//        if (ObjectUtil.isNull(redisCode)) {
//            throw new ValidateCodeException("验证码已失效");
//        }
//        if (!userDTO.getSmsCode().toLowerCase().equals(redisCode)) {
//            throw new ValidateCodeException("短信验证码错误");
//        }
//        return R.ok(userService.register(userDTO));
//    }

    /**
     * 登录
     *
     * @param account
     * @param password
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public R login(@RequestParam("account") String account, @RequestParam("password") String password, HttpServletRequest request) {
        // 社交快速登录
        String token = request.getParameter("token");
        if (StringUtils.isNotEmpty(token))  {
            return ResultUtil.success(token);
        }

        return ResultUtil.success(userService.login(account, password));
    }

    /**
     * 保存完信息然后跳转到绑定用户信息页面
     *
     * @param request
     * @param response
     * @throws IOException
     */
//    @GetMapping("/socialSignUp")
//    public void socialSignUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String uuid = UUID.randomUUID().toString();
//        SocialUserInfo userInfo = new SocialUserInfo();
//        Connection<?> connectionFromSession = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
//        userInfo.setHeadImg(connectionFromSession.getImageUrl());
//        userInfo.setNickname(connectionFromSession.getDisplayName());
//        userInfo.setProviderId(connectionFromSession.getKey().getProviderId());
//        userInfo.setProviderUserId(connectionFromSession.getKey().getProviderUserId());
//        ConnectionData data = connectionFromSession.createData();
//        PreConnectionData preConnectionData = new PreConnectionData();
//        BeanUtil.copyProperties(data, preConnectionData);
//        socialRedisHelper.saveConnectionData(uuid, preConnectionData);
//        // 跳转到用户绑定页面
//        response.sendRedirect(url + "/bind?key=" + uuid);
//    }

    /**
     * 社交登录绑定
     *
     * @param user
     * @return
     */
//    @PostMapping("/bind")
//    public R register(@RequestBody SysUser user) {
//        return R.ok(userService.doPostSignUp(user));
//    }


    /**
     * @Description 暂时这样写
     **/
//    @RequestMapping("/info")
//    public R info() {
//        Map<String, Object> map = new HashMap<>();
//        List<String> list = new ArrayList<>();
//        list.add("admin");
//        map.put("roles", list);
//        map.put("avatar", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561394014552&di=17b6c1233048e5276f48309b306c7699&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201804%2F29%2F20180429210111_gtsnf.jpg");
//        map.put("name", "Super Admin");
//        return R.ok(map);
//    }

    /**
     * @Description 使用jwt前后分离 只需要前端清除token即可 暂时返回success
     **/
    @RequestMapping("/logout")
    public String logout() {
        return "success";
    }
}
