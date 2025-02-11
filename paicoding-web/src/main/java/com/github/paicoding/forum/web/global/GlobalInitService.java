package com.github.paicoding.forum.web.global;

import com.github.paicoding.forum.api.model.context.ReqInfoContext;
import com.github.paicoding.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import com.github.paicoding.forum.core.util.SessionUtil;
import com.github.paicoding.forum.service.user.service.LoginService;
import com.github.paicoding.forum.service.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @author Zane
 * @date 2025/02/02
 */
@Slf4j
@Service
public class GlobalInitService {

    @Autowired
    private UserService userService;

    // 初始化用户信息
    public void initLoginUser(ReqInfoContext.ReqInfo reqInfo) {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        // 获取 Session 或者 Authorization 信息
        String session = Optional.ofNullable(request.getCookies())
                .map(cookies -> SessionUtil.findCookieByName(request, LoginService.SESSION_KEY))
                .map(Cookie::getValue)
                .orElse(request.getHeader("Authorization"));

        if (session != null) {
            initLoginUser(session, reqInfo);
        }
    }

    // 初始化用户信息
    public void initLoginUser(String session, ReqInfoContext.ReqInfo reqInfo) {
        BaseUserInfoDTO user = userService.getAndUpdateUserIpInfoBySessionId(session);
        reqInfo.setSession(session);
        if (user != null) {
            reqInfo.setUserId(user.getUserId());
            reqInfo.setUser(user);
        }
    }
}
