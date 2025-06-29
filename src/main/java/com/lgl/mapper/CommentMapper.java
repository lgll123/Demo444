package com.lgl.mapper;

import com.lgl.model.CommentWithLikeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 刘国良
 * @Date 2025/6/20 23:47
 * @Destription
 */
@Component
public interface CommentMapper {
    // 查询某条知识的所有评论（包括子评论），附带每条评论的点赞数，并判断当前用户是否点赞
    List<CommentWithLikeDTO> getCommentsByKnowledgeId(@Param("knowledgeId") Long knowledgeId, @Param("currentUserId") Long currentUserId);
}

