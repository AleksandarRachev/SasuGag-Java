package test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import test.demo.dto.PostResponse;
import test.demo.exception.ElementExistsException;
import test.demo.service.PostService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin("*")
@PreAuthorize("permitAll()")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostResponse> savePost(@RequestPart("file") MultipartFile image,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("category") String categoryName,
                                                    @RequestAttribute("userId") String userId) throws ElementExistsException, IOException {
        return ResponseEntity.ok(postService.savePost(image, name, categoryName, userId));
    }

    @GetMapping(value = "/image/{productId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable(value = "productId") String productId) {
        return ResponseEntity.ok(postService.downloadImage(productId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        return ResponseEntity.ok(postService.getAllPosts(page - 1));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PostResponse>> getAllPostsByCategory(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                       @RequestParam(value = "category", required = false) String categoryName){
        return ResponseEntity.ok(postService.getAllPostsByCategory(page -1, categoryName));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getPostsCount(){
        return ResponseEntity.ok(postService.getPostsCount());
    }

    @GetMapping("/count/filter")
    public ResponseEntity<Integer> getPostsCount(@RequestParam(value = "category", required = false, defaultValue = "") String categoryName){
        return ResponseEntity.ok(postService.getPostCountByCategoryName(categoryName));
    }

}
