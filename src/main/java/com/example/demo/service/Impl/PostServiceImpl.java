package com.example.demo.service.Impl;

import com.example.demo.entity.Category;
import com.example.demo.entity.Post;
import com.example.demo.exception.ResourceNotFoundExcetion;
import com.example.demo.payload.PostDto;
import com.example.demo.payload.PostResponse;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getId()).orElseThrow(() ->new ResourceNotFoundExcetion(
                "Category","id",
                postDto.getId()));

        Post post = mapToEntity(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);

        // Convert entity to DTO
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }


    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageSize,pageNo, Sort.by(sortBy));

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(post->mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundExcetion("Post","id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto,long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundExcetion("Post","id",id));

        Category category =
                categoryRepository.findById(postDto.getCategory().getId()).orElseThrow(()->new ResourceNotFoundExcetion(
                        "Category",
                "id",
                post.getCategory().getId()));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setCategory(postDto.getCategory());

        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundExcetion("Post","id",id));

        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategoryId(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundExcetion(
                "Category","id",
                 categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map((post)-> mapToDto(post)).collect(Collectors.toList());
    }

    private PostDto mapToDto(Post post){
        PostDto postDto = mapper.map(post,PostDto.class);
        //convert Entity to DTO
        /*PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());*/

        return postDto;
    }

    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto,Post.class);
        //convert DTO to Entity
        /*Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription()); */

        return post;
    }
}























