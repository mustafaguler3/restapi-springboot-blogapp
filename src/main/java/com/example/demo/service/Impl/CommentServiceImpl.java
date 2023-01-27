package com.example.demo.service.Impl;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.exception.BlogAPIException;
import com.example.demo.exception.ResourceNotFoundExcetion;
import com.example.demo.payload.CommentDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundExcetion("Post","id",postId));

        // set post to comment entity
        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundExcetion("Post","id",postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundExcetion(
                "Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"cannot does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundExcetion("Post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundExcetion("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment doew not belongs to post");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updated = commentRepository.save(comment);

        return mapToDto(updated);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = mapper.map(comment,CommentDto.class);
        /*CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());
        */
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto,Comment.class);
        /*Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        */
        return comment;
    }

}

















