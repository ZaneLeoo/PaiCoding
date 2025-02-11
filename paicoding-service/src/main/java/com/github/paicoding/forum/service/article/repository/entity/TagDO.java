package com.github.paicoding.forum.service.article.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签实体类，映射到 tags 表
 */
@Data
@NoArgsConstructor
@TableName("tag")
public class TagDO {

    /**
     * 标签 ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

}
