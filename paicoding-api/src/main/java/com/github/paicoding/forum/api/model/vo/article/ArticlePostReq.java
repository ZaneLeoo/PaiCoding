package com.github.paicoding.forum.api.model.vo.article;

import com.github.paicoding.forum.api.model.enums.ArticleTypeEnum;
import com.github.paicoding.forum.api.model.enums.PushStatusEnum;
import com.github.paicoding.forum.api.model.enums.SourceTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 发布文章请求参数
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
public class ArticlePostReq implements Serializable {
    /**
     * 文章ID， 当存在时，表示更新文章
     */
    private Long articleId;
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章总结
     */
    private String summary;
    /**
     * 标签
     */
    private Set<String> tags;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 封面
     */
    private String cover;

    /**
     * 状态：0-未发布，1-已发布
     *
     * @see com.github.paicoding.forum.api.model.enums.PushStatusEnum
     */
    private Integer status;

    /**
     * POST 发表, SAVE 暂存 DELETE 删除
     */
    private String actionType;


    public PushStatusEnum pushStatus() {
        if ("post".equalsIgnoreCase(actionType)) {
            return PushStatusEnum.ONLINE;
        } else {
            return PushStatusEnum.OFFLINE;
        }
    }

    public boolean deleted() {
        return "delete".equalsIgnoreCase(actionType);
    }
}