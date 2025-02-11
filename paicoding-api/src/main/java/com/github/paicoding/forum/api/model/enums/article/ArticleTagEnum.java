package com.github.paicoding.forum.api.model.enums.article;

public enum ArticleTagEnum {
    JAVA(1, "Java"),
    Life(2, "生活"),
    WEB_DESIGN(3, "Web Design"),
    DATABASE(4, "Database"),
    CACHE(5, "Cache"),
    ORM(6, "ORM"),
    PYTHON(7, "Python"),
    FRONT(8, "Front"),
    BACKEND(9, "Backend"),
    VUEJS(10,"Vue.js"),
    LINUX(11,"Linux"),
    CSS(12,"CSS"),
    SPRINGBOOT(13,"Spring Boot"),
    MYSQL(14,"MySQL"),
    GO(15,"Go"),
    MONGODB(16,"MongoDB"),
    POSTGRESQL(17,"PostgreSQL"),
    ORACLE(18,"Oracle"),
    CPP(19,"C++"),
    REACT(20, "React"),
    HTML(21, "HTML"),
    ANIMAL(22, "Animal");


    private final Long code;
    private final String description;
    // 构造函数
    ArticleTagEnum(long code, String description) {
        this.code = code;
        this.description = description;
    }
    // 获取枚举值对应的 code
    public Long getCode() {
        return code;
    }
    // 获取枚举值对应的描述
    public String getDescription() {
        return description;
    }
    // 根据 description 获取对应的 code
    public static Long fromDescription(String description) {
        for (ArticleTagEnum status : ArticleTagEnum.values()) {
            if (status.getDescription().equals(description)) {
                return status.getCode();
            }
        }
        throw new IllegalArgumentException("未知的描述: " + description);
    }
}
