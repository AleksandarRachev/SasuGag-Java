package gag.sasu.controller;

import gag.sasu.dto.*;
import gag.sasu.exception.ElementExistsException;
import gag.sasu.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin("*")
@PreAuthorize("permitAll()")
@Validated
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostResponse> savePost(@RequestPart("file") MultipartFile image,
                                                 @RequestParam @NotBlank @Size(max = 50, message = "Title too long") String title,
                                                 @RequestParam("category") String categoryName,
                                                 @RequestAttribute("userId") String userId) throws ElementExistsException, IOException {
        return ResponseEntity.ok(postService.savePost(image, title, categoryName, userId));
    }

    @GetMapping(value = "/image/{productId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable(value = "productId") String productId) {
        return ResponseEntity.ok(postService.downloadImage(productId));
    }

    @GetMapping
    public ResponseEntity<List<VoteForPostResponse>> getAllPosts(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "userId", required = false, defaultValue = "null") String userId) {
        return ResponseEntity.ok(postService.getAllPosts(page, userId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PostResponse>> getAllPostsByCategory(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                    @RequestParam(value = "category", required = false) String categoryName) {
        return ResponseEntity.ok(postService.getAllPostsByCategory(page, categoryName));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getPostsCount() {
        return ResponseEntity.ok(postService.getPostsCount());
    }

    @GetMapping("/count/filter")
    public ResponseEntity<Integer> getPostsCount(@RequestParam(value = "category", required = false, defaultValue = "") String categoryName) {
        return ResponseEntity.ok(postService.getPostCountByCategoryName(categoryName));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("postId") String postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/vote")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VoteForPostResponse> voteForPost(@RequestBody PostVoteRequest postVoteRequest,
                                                           @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(postService.voteForPost(postVoteRequest, userId));
    }

    @GetMapping("/voted/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VotedPostResponse> getVotedPost(@PathVariable("postId") String postId,
                                                          @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(postService.getVotedPostById(userId, postId));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostResponse>> getUserPosts(@RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(postService.getPostsForUser(userId));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<DeletePostResponse> deletePost(@PathVariable("postId") String postId,
                                                         @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(postService.deletePost(postId, userId));
    }

}
