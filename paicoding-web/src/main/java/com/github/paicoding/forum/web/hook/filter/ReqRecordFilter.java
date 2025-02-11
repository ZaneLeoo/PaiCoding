package com.github.paicoding.forum.web.hook.filter;

import com.github.paicoding.forum.api.model.context.ReqInfoContext;
import com.github.paicoding.forum.core.util.SessionUtil;
import com.github.paicoding.forum.service.user.service.LoginService;
import com.github.paicoding.forum.web.global.GlobalInitService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.UUID;

/**
 * 1. 请求参数日志输出过滤器
 * 2. 判断用户是否登录
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "reqRecordFilter", asyncSupported = true)
public class ReqRecordFilter implements Filter {

    @Autowired
    private GlobalInitService globalInitService;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 跳过静态资源和OPTIONS请求
        if (isStaticURI(request) || HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 初始化请求信息，最小化采集
        ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
        reqInfo.setPath(request.getRequestURI());  // 只采集请求路径
        reqInfo.setDeviceId(getOrInitDeviceId(request, response)); // 只初始化设备 ID

        // 初始化用户信息
        globalInitService.initLoginUser(reqInfo);

        // 将请求信息保存到上下文
        ReqInfoContext.addReqInfo(reqInfo);

        // 继续请求处理
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private boolean isStaticURI(HttpServletRequest request) {
        String uri = request.getRequestURI().toLowerCase();
        return uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png")
                || uri.endsWith(".ico") || uri.endsWith(".svg") || uri.endsWith(".map");
    }

    private String getOrInitDeviceId(HttpServletRequest request, HttpServletResponse response) {
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId) && !"null".equalsIgnoreCase(deviceId)) {
            return deviceId;
        }

        Cookie deviceCookie = SessionUtil.findCookieByName(request, LoginService.USER_DEVICE_KEY);
        if (deviceCookie != null) {
            return deviceCookie.getValue();
        }

        // 生成新的设备ID并保存到Cookie
        deviceId = UUID.randomUUID().toString();
        if (response != null) {
            response.addCookie(SessionUtil.newCookie(LoginService.USER_DEVICE_KEY, deviceId));
        }
        return deviceId;
    }
}

