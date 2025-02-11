package com.github.paicoding.forum.service.comment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.beust.ah.A;
import com.github.paicoding.forum.api.model.vo.PageParam;
import com.github.paicoding.forum.api.model.vo.comment.dto.CommentDTO;
import com.github.paicoding.forum.service.comment.repository.dao.CommentDao;
import com.github.paicoding.forum.service.comment.repository.entity.CommentDO;
import com.github.paicoding.forum.service.comment.service.CommentReadService;
import com.github.paicoding.forum.service.user.repository.entity.UserInfoDO;
import com.github.paicoding.forum.service.user.repository.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommentReadServiceImpl implements CommentReadService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<CommentDTO> getArticleComments(Long articleId) {
        // 1. 批量查询根评论和所有子评论
        List<CommentDO> allComments = commentDao.getAllComments(articleId);
        if (CollectionUtils.isEmpty(allComments)) return Collections.emptyList();

        // 2. 分类根评论和子评论
        Map<Boolean, List<CommentDO>> partitioned = allComments.stream()
                .collect(Collectors.partitioningBy(c -> c.getParentCommentId() == null));
        List<CommentDO> rootComments = partitioned.get(true);
        List<CommentDO> replyComments = partitioned.get(false);
        // 3. 统一转换DTO
        List<CommentDTO> root = rootComments.stream().map(this::convertToDTO).toList();
        List<CommentDTO> reply = replyComments.stream().map(this::convertToDTO).toList();

        // 4. 批量查询用户信息（一次性获取所有需要用户）
        Set<Long> rootIds = root.stream()
                .map(CommentDTO::getAuthorId)
                .collect(Collectors.toSet());
        Set<Long> replyIds = reply.stream()
                .map(CommentDTO::getAuthorId)
                .collect(Collectors.toSet());
        rootIds.addAll(replyIds);
        Map<Long, UserInfoDO> userMap = userInfoMapper.selectBatchIds(rootIds).stream()
                .collect(Collectors.toMap(UserInfoDO::getUserId, Function.identity()));

        // 5. 填充用户信息
        root.forEach(comment -> {
            UserInfoDO user = userMap.get(comment.getAuthorId());
            if (user != null) {
                comment.setAuthorName(user.getUserName());
                comment.setAuthorAvatar(user.getPhoto());
            }
        });
        reply.forEach(comment -> {
            UserInfoDO user = userMap.get(comment.getAuthorId());
            if (user != null) {
                comment.setAuthorName(user.getUserName());
                comment.setAuthorAvatar(user.getPhoto());
            }
        });
        // 6. 构建评论树
        Map<Long, List<CommentDTO>> replyMap =
                reply.stream().collect(Collectors.groupingBy(CommentDTO::getParentCommentId));
        List<CommentDTO> result =
                root.stream().peek(dto -> loadReplies(dto, replyMap)).collect(Collectors.toList());
        return result;
    }

    // 递归加载回复保持不变
    private void loadReplies(CommentDTO parent, Map<Long, List<CommentDTO>> replyMap) {
        List<CommentDTO> replies = replyMap.getOrDefault(parent.getId(), Collections.emptyList());
        parent.setReplies(replies);
        replies.forEach(reply -> loadReplies(reply, replyMap));
    }

    // 转换方法复用
    private CommentDTO convertToDTO(CommentDO comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setAuthorId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setParentCommentId(comment.getParentCommentId());
        dto.setLikes(comment.getLikes());
        return dto;
    }

}
