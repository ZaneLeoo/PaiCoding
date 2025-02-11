package com.github.paicoding.forum.web.global.vo;

import com.github.paicoding.forum.api.model.vo.user.dto.BaseUserInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
public class GlobalVo {

    /**
     * 环境
     */
    private String env;

    /**
     * 是否已登录
     */
    private Boolean isLogin;

    /**
     * 登录用户信息
     */
    private BaseUserInfoDTO user;

    /**
     * 消息通知数量
     */
    private Integer msgNum;

    /**
     * 在线用户人数
     */
    private Integer onlineCnt;

    private String currentDomain;

    private String jsonLd;

}
