package com.github.paicoding.forum.api.model.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.github.paicoding.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import lombok.Data;

/**
 * 请求上下文，携带用户身份相关信息
 *
 * @author XuYifei
 * @date 2024-07-12
 */
public class ReqInfoContext {
    private static TransmittableThreadLocal<ReqInfo> contexts = new TransmittableThreadLocal<>();

    public static void addReqInfo(ReqInfo reqInfo) {
        contexts.set(reqInfo);
    }

    public static void clear() {
        contexts.remove();
    }

    public static ReqInfo getReqInfo() {
        return contexts.get();
    }

    @Data
    public static class ReqInfo {
        /**
         * 访问路径
         */
        private String path;

        /**
         * 登录的会话
         */
        private String session;

        /**
         * 用户id
         */
        private Long userId;

        /**
         * 用户信息
         */
        private BaseUserInfoDTO user;

        /**
         * 消息数量
         */
        private Integer msgNum;

        /**
         * 设备信息
         */
        private String deviceId;


        public String getName() {
            return session;
        }
    }
}
