package com.lgl.controller;

import com.lgl.model.CommentWithLikeDTO;
import com.lgl.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/knowledge")
    public ResponseEntity<List<CommentWithLikeDTO>> getComments() {
        return ResponseEntity.ok(commentService.getCommentTree(1L,  102L));
    }
}