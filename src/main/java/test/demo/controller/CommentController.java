package test.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import test.demo.dto.CommentRequest;
import test.demo.dto.CommentResponse;
import test.demo.service.CommentService;

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
