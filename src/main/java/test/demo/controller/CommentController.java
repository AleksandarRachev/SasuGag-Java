package test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import test.demo.dto.CommentRequest;
import test.demo.dto.CommentResponse;
import test.demo.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest,
                                                      @RequestAttribute("userId") String userId){
        return ResponseEntity.ok(commentService.addComment(commentRequest, userId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable("postId") String postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

}
