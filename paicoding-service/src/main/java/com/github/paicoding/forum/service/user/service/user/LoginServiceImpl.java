package com.github.paicoding.forum.service.user.service.user;

import com.github.paicoding.forum.api.model.context.ReqInfoContext;
import com.github.paicoding.forum.api.model.exception.ExceptionUtil;
import com.github.paicoding.forum.api.model.vo.constants.StatusEnum;
import com.github.paicoding.forum.api.model.vo.user.UserPwdLoginReq;
import com.github.paicoding.forum.service.user.repository.dao.UserDao;
import com.github.paicoding.forum.service.user.repository.entity.UserDO;
import com.github.paicoding.forum.service.user.service.LoginService;
import com.github.paicoding.forum.service.user.service.RegisterService;
import com.github.paicoding.forum.service.user.service.UserService;
import com.github.paicoding.forum.service.user.service.help.StarNumberHelper;
import com.github.paicoding.forum.service.user.service.help.UserPwdEncoder;
import com.github.paicoding.forum.service.user.service.help.UserSessionHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基于验证码、用户名密码的登录方式
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserDao userDao;


    @Autowired
    private UserSessionHelper userSessionHelper;
    @Autowired
    private StarNumberHelper starNumberHelper;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserPwdEncoder userPwdEncoder;

    @Autowired
    private UserService userService;


    @Override
    public void logout(String session) {
        userSessionHelper.removeSession(session);
    }


    /**
     * 用户名密码方式登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public String loginByUserPwd(String username, String password) {
        UserDO user = userDao.getUserByUserName(username);
        if (user == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userName=" + username);
        }
        // todo 如果密码加密,这里需要进行加密后对比
        Long userId = user.getId();
        // 登录成功，返回对应的session
        ReqInfoContext.getReqInfo().setUserId(userId);
        return userSessionHelper.genToken(userId);
    }


    /**
     * 用户名密码方式登录，若用户不存在，则进行注册
     *
     * @param loginReq 登录信息
     * @return
     */
    @Override
    public String registerByUserPwd(UserPwdLoginReq loginReq) {
        // 1. 前置校验
        if (StringUtils.isBlank(loginReq.getUsername()) || StringUtils.isBlank(loginReq.getPassword())) {
            throw ExceptionUtil.of(StatusEnum.USER_PWD_ERROR);
        }

        // 2. 尝试使用用户名进行登录
        UserDO user = userDao.getUserByUserName(loginReq.getUsername());
        Long userId = 0L;
        if (user != null) {
            if (!userPwdEncoder.match(loginReq.getPassword(), user.getPassword())) {
                // 3.1 用户名已经存在
                throw ExceptionUtil.of(StatusEnum.USER_LOGIN_NAME_REPEAT, loginReq.getUsername());
            }
        } else {
            //4. 走用户注册流程
            userId = registerService.registerByUserNameAndPassword(loginReq);
        }
        ReqInfoContext.getReqInfo().setUserId(userId);
        return userSessionHelper.genToken(userId);
    }


}
