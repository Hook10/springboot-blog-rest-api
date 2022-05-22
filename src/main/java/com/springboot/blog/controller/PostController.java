package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostDTOV2;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.springboot.blog.utils.AppConstants.*;

@RestController
@RequestMapping()
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    // create blog post
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody  PostDTO postDTO) {
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    // get all posts rest api
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/api/posts/{id}", produces = "application/vnd.java.v1+json")
    public ResponseEntity<PostDTO> getPostByIdV1(@PathVariable("id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping(value = "/api/posts/{id}", produces = "application/vnd.java.v2+json")
    public ResponseEntity<PostDTOV2> getPostByIdV2(@PathVariable("id") long id) {
        PostDTO postDTO = postService.getPostById(id);
        PostDTOV2 postDTOV2 = new PostDTOV2();
        postDTOV2.setId(postDTO.getId());
        postDTOV2.setTitle(postDTO.getTitle());
        postDTOV2.setDescription(postDTO.getDescription());
        postDTOV2.setContent(postDTO.getContent());
        List<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add("Spring Boot");
        tags.add("AWS");
        postDTOV2.setTags(tags);
        return ResponseEntity.ok(postDTOV2);
    }

    @PreAuthorize("hasRole('ADMIN')")
    //update post by id rest api
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody @Valid PostDTO postDTO, @PathVariable("id") long id) {
        PostDTO updatePost = postService.updatePost(postDTO, id);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

}
