package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.springboot.blog.utils.AppConstants.*;

@Api(value = "CRUD Rest APIs for Post resources")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "Create Post REST API")
    @PreAuthorize("hasRole('ADMIN')")
    // create blog post
    @PostMapping()
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody  PostDTO postDTO) {
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get All Posts REST API")
    // get all posts rest api
    @GetMapping()
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation(value = "Get Post By Id REST API")
    // get post by id
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> getPostByIdV1(@PathVariable("id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }



    @ApiOperation(value = "Update Post By Id REST API")
    @PreAuthorize("hasRole('ADMIN')")
    //update post by id rest api
    @PutMapping("{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody @Valid PostDTO postDTO, @PathVariable("id") long id) {
        PostDTO updatePost = postService.updatePost(postDTO, id);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Post By Id REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

}
