package com.github.paicoding.forum.web.hook.interceptor;

import com.github.paicoding.forum.api.model.context.ReqInfoContext;
import com.github.paicoding.forum.api.model.vo.ResVo;
import com.github.paicoding.forum.api.model.vo.constants.StatusEnum;
import com.github.paicoding.forum.core.permission.Permission;
import com.github.paicoding.forum.core.permission.UserRole;
import com.github.paicoding.forum.core.util.JsonUtil;
import com.github.paicoding.forum.core.util.SpringUtil;
import com.github.paicoding.forum.service.rank.service.UserActivityRankService;
import com.github.paicoding.forum.service.rank.service.model.ActivityScoreBo;
import com.github.paicoding.forum.web.global.GlobalInitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.io.IOException;

/**
 * 一个自定义的 Spring MVC 拦截器，用于在请求处理前进行用户权限的验证和用户活跃度更新
 * 它在每个请求进入处理器方法之前被触发
 */
@Slf4j
@Component
@Order(-1)
public class GlobalViewInterceptor implements AsyncHandlerInterceptor {

    private static final String ADMIN_API_PATH = "/api/admin/";
    private static final String ADMIN_PATH = "/admin/";

    @Autowired
    private GlobalInitService globalInitService;

    /**
     * 处理请求前的拦截操作。用于验证用户权限，更新活跃度等。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Permission permission = getPermissionAnnotation(handlerMethod);

            if (permission == null || permission.role() == UserRole.ALL) {
                updateUserActivity();
                return true;
            }

            if (ReqInfoContext.getReqInfo() == null || ReqInfoContext.getReqInfo().getUserId() == null) {
                handleNoPermission(response, handlerMethod, request);
                return false;
            }

            if (permission.role() == UserRole.ADMIN && !UserRole.ADMIN.name().equalsIgnoreCase(ReqInfoContext.getReqInfo().getUser().getRole())) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }
        }
        return true;
    }

    /**
     * 获取方法或类级别的权限注解。
     * 如果方法级别没有注解，尝试获取类级别的注解。
     */
    private Permission getPermissionAnnotation(HandlerMethod handlerMethod) {
        Permission permission = handlerMethod.getMethod().getAnnotation(Permission.class);
        if (permission == null) {
            permission = handlerMethod.getBeanType().getAnnotation(Permission.class);
        }
        return permission;
    }

    /**
     * 处理当用户未登录的情况，返回不同类型的响应。
     */
    private void handleNoPermission(HttpServletResponse response, HandlerMethod handlerMethod, HttpServletRequest request) throws IOException, IOException {
        if (handlerMethod.getMethod().getAnnotation(ResponseBody.class) != null
                || handlerMethod.getMethod().getDeclaringClass().getAnnotation(RestController.class) != null) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().println(JsonUtil.toStr(ResVo.fail(StatusEnum.FORBID_NOTLOGIN)));
            response.getWriter().flush();
        } else if (request.getRequestURI().startsWith(ADMIN_API_PATH) || request.getRequestURI().startsWith(ADMIN_PATH)) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/");
        }
    }

    /**
     * 更新用户的活跃度。每次用户请求时增加用户的活动分数。
     */
    private void updateUserActivity() {
        if (ReqInfoContext.getReqInfo() != null) {
            // 异步更新活跃度
            SpringUtil.getBean(UserActivityRankService.class)
                    .addActivityScore(ReqInfoContext.getReqInfo().getUserId(), new ActivityScoreBo().setPath(ReqInfoContext.getReqInfo().getPath()));
        }
    }
}
