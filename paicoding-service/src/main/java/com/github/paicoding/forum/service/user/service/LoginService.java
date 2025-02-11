package com.github.paicoding.forum.service.user.service;

import com.github.paicoding.forum.api.model.vo.user.UserPwdLoginReq;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
public interface LoginService {
    String SESSION_KEY = "p-session";
    String USER_DEVICE_KEY = "p-device";



    /**
     * 登出
     *
     * @param session 用户会话
     */
    void logout(String session);


    /**
     * 用户名密码方式登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    String loginByUserPwd(String username, String password);

    /**
     * 注册登录，并绑定对应的星球、邀请码
     *
     * @param loginReq 登录信息
     * @return
     */
    String registerByUserPwd(UserPwdLoginReq loginReq);
}
