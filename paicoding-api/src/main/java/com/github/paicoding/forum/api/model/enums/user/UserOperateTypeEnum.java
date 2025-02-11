package com.github.paicoding.forum.api.model.enums.user;

public enum UserOperateTypeEnum {

    LIKE(1, "like"),
    CANCEL_LIKE(2,"cancel_like"),
    FOLLOW(3,"follow"),
    CANCEL_FOLLOW(4,"cancel_follow");


    int code;
    String desc;

    UserOperateTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
