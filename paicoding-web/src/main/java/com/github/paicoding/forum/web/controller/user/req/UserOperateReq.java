package com.github.paicoding.forum.web.controller.user.req;

import lombok.Data;

@Data
public class UserOperateReq {
    private Long operateUserId;
    private String userOperateType;
    private Integer operateStatus;
    private Long operateEntityId;
    private Long operateEntityAuthorId;
}
