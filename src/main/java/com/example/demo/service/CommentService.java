package com.example.demo.service;

import com.example.demo.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId,CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(Long postId,Long commentId);

    CommentDto updateComment(Long postId,long commentId,CommentDto commentDto);
}






















