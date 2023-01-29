package com.example.demo.payload;

import com.example.demo.entity.Category;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostDtoV2 {
    private long id;
    @NotEmpty
    @Size(min = 2,message = "Post title should have at least 2 characters")
    private String title;
    @NotEmpty
    @Size(min = 10,message = "Post Description should have at least 10 characters")
    private String description;
    @NotEmpty
    private String content;
    private Category category;
    private List<String> tags;
}





















