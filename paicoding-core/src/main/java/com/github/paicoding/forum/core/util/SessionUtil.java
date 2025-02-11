package com.github.paicoding.forum.core.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * SessionUtil 类提供了一些便捷的方法来操作和管理 HTTP 请求中的 cookies。
 * 该类主要用于设置、删除和查找 cookies，适用于用户会话管理、身份认证等场景。
 *
 * 主要功能包括：
 * 1. 创建新的 cookie
 * 2. 删除指定的 cookie
 * 3. 查找指定的 cookie
 *
 * 注意：使用 cookies 时需要考虑安全性和隐私保护，避免存储敏感数据。
 */
public class SessionUtil {

    // 设置默认 cookie 的有效期为 30 天（单位：秒）
    private static final int COOKIE_AGE = 86400;

    /**
     * 创建一个新的 Cookie，使用默认的路径（"/"）和过期时间（30天）。
     *
     * @param key     Cookie 的名称
     * @param session Cookie 的值
     * @return        创建的 Cookie 对象
     */
    public static Cookie newCookie(String key, String session) {
        return newCookie(key, session, "/", COOKIE_AGE);
    }

    /**
     * 创建一个新的 Cookie，允许指定路径和过期时间。
     *
     * @param key      Cookie 的名称
     * @param session  Cookie 的值
     * @param path     Cookie 的路径
     * @param maxAge   Cookie 的最大有效期，单位：秒
     * @return         创建的 Cookie 对象
     */
    public static Cookie newCookie(String key, String session, String path, int maxAge) {
        Cookie cookie = new Cookie(key, session);
        cookie.setPath(path);  // 设置 Cookie 路径
        cookie.setMaxAge(maxAge);  // 设置 Cookie 过期时间
        return cookie;
    }

    /**
     * 删除指定名称的 Cookie，使用默认路径（"/"）。
     *
     * @param key 需要删除的 Cookie 的名称
     * @return    删除的 Cookie 对象
     */
    public static Cookie delCookie(String key) {
        return delCookie(key, "/");
    }

    /**
     * 删除指定名称的 Cookie，并允许指定路径。
     *
     * @param key   需要删除的 Cookie 的名称
     * @param path  Cookie 的路径
     * @return      删除的 Cookie 对象
     */
    public static Cookie delCookie(String key, String path) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath(path);  // 设置路径
        cookie.setMaxAge(0);    // 设置过期时间为 0，表示删除
        return cookie;
    }

    /**
     * 根据 Cookie 名称查找对应的 Cookie 对象。
     *
     * @param request 当前 HTTP 请求
     * @param name    需要查找的 Cookie 名称
     * @return        找到的 Cookie 对象，如果没有找到则返回 null
     */
    public static Cookie findCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;  // 如果没有 cookies，返回 null
        }

        // 查找名称匹配的 Cookie
        return Arrays.stream(cookies)
                .filter(cookie -> StringUtils.equalsAnyIgnoreCase(cookie.getName(), name))
                .findFirst().orElse(null);
    }

    /**
     * 根据 Cookie 名称查找对应的 Cookie 值（用于处理 ServerHttpRequest）。
     *
     * @param request 当前 HTTP 请求
     * @param name    需要查找的 Cookie 名称
     * @return        Cookie 的值，如果没有找到则返回 null
     */
    public static String findCookieByName(ServerHttpRequest request, String name) {
        List<String> list = request.getHeaders().get("cookie");
        if (CollectionUtils.isEmpty(list)) {
            return null;  // 如果没有 cookies 头，返回 null
        }

        // 遍历 Cookie 字符串，查找名称匹配的值
        for (String sub : list) {
            String[] elements = StringUtils.split(sub, ";");
            for (String element : elements) {
                String[] subs = StringUtils.split(element, "=");
                if (subs.length == 2 && StringUtils.equalsAnyIgnoreCase(subs[0].trim(), name)) {
                    return subs[1].trim();  // 返回 Cookie 的值
                }
            }
        }
        return null;  // 未找到匹配的 Cookie 名称
    }
}
