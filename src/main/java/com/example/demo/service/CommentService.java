package com.example.demo.service;

import com.example.demo.payload.CommentDto;

public interface CommentService {
    CommentDto createComment(long postId,CommentDto commentDto);
}
