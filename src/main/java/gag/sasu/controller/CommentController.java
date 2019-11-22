package gag.sasu.controller;

import gag.sasu.dto.CommentRequest;
import gag.sasu.dto.CommentResponse;
import gag.sasu.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin("*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest,
                                                      @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(commentService.addComment(commentRequest, userId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable("postId") String postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

}
