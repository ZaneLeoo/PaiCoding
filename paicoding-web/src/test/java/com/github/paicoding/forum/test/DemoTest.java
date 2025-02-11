package com.github.paicoding.forum.test;

import com.github.paicoding.forum.core.util.DateUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author XuYifei
 * @date 2024-07-12
 */
public class DemoTest {

        public static void main(String[] args) {
            // 示例数据
            List<CommentDO> comments = Arrays.asList(
                    new CommentDO(1L, 0L, "Root Comment 1"),
                    new CommentDO(2L, 0L, "Root Comment 2"),
                    new CommentDO(3L, 1L, "Reply to Comment 1 - 1"),
                    new CommentDO(4L, 1L, "Reply to Comment 1 - 2"),
                    new CommentDO(5L, 2L, "Reply to Comment 2 - 1"),
                    new CommentDO(6L, 3L, "Reply to Comment 3 - 1"),
                    new CommentDO(7L, 3L, "Reply to Comment 3 - 2"),
                    new CommentDO(8L, 4L, "Reply to Comment 4 - 1")
            );

            // 按 parentCommentId 分组
            Map<Long, List<CommentDO>> groupedComments = comments.stream()
                    .collect(Collectors.groupingBy(CommentDO::getParentCommentId));

            // 打印分组结果
            System.out.println("Grouped Comments:");
            groupedComments.forEach((parentId, commentList) -> {
                System.out.println("Parent ID " + parentId + ": " + commentList);
            });

            // 构建评论树：递归加载子评论
            List<CommentDO> rootComments = groupedComments.get(0L); // 根评论的 parentId 是 0
            loadReplies(rootComments, groupedComments);

            // 打印完整的评论树
            System.out.println("\nComplete Comment Tree:");
            rootComments.forEach(System.out::println);
        }

        // 递归加载子评论
        private static void loadReplies(List<CommentDO> comments, Map<Long, List<CommentDO>> groupedComments) {
            for (CommentDO comment : comments) {
                // 获取该评论的所有子评论
                List<CommentDO> replies = groupedComments.get(comment.getId());
                if (replies != null && !replies.isEmpty()) {
                    comment.setReplies(replies);
                    loadReplies(replies, groupedComments); // 递归加载子评论的子评论
                }
            }
        }
}
class CommentDO {
    Long id;
    Long parentCommentId;
    String content;
    List<CommentDO> replies; // 子评论

    public CommentDO(Long id, Long parentCommentId, String content) {
        this.id = id;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.replies = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CommentDO{id=" + id + ", content='" + content + "'}";
    }
    public Long getId() { return id; }
    public Long getParentCommentId() { return parentCommentId; }
    public List<CommentDO> getReplies() { return replies; }
    public void setReplies(List<CommentDO> replies) { this.replies = replies; }
}