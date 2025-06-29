package com.lgl.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 刘国良
 * @Date 2025/6/20 23:48
 * @Destription
 */

@Data
public class CommentWithLikeDTO {
    private Long commentId;
    private Long knowledgeId;
    private Long userId;
    private Long parentCommentId;
    private String commentContent;
    private LocalDateTime createTime;
    private Integer likeCount;
    private Boolean likedByCurrentUser;

    // 用于构建树形结构
    private List<CommentWithLikeDTO> children = new ArrayList<>();
}
