package com.lgl.service;

import com.lgl.mapper.CommentMapper;
import com.lgl.model.CommentWithLikeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    public List<CommentWithLikeDTO> getCommentTree(Long knowledgeId, Long currentUserId) {
        // 1. 查询所有评论（包括点赞信息）
        List<CommentWithLikeDTO> allComments = commentMapper.getCommentsByKnowledgeId(knowledgeId,  currentUserId);

        // 2. 构建树形结构 
        return buildCommentTree(allComments);
    }

    private List<CommentWithLikeDTO> buildCommentTree(List<CommentWithLikeDTO> allComments) {
        // 创建根评论列表（parentCommentId为null的评论）
        List<CommentWithLikeDTO> rootComments = allComments.stream()
                .filter(comment -> comment.getParentCommentId()  == null)
                .collect(Collectors.toList());

        // 为每个根评论查找子评论 
        rootComments.forEach(rootComment  ->
                rootComment.setChildren(findChildren(rootComment,  allComments))
        );

        return rootComments;
    }

    private List<CommentWithLikeDTO> findChildren(CommentWithLikeDTO parent, List<CommentWithLikeDTO> allComments) {
        return allComments.stream()
                .filter(comment -> parent.getCommentId().equals(comment.getParentCommentId()))
                .peek(comment -> comment.setChildren(findChildren(comment,  allComments)))
                .collect(Collectors.toList());
    }
}