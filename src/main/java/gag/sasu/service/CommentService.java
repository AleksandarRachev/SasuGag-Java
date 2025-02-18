package gag.sasu.service;

import gag.sasu.dto.CommentRequest;
import gag.sasu.dto.CommentResponse;
import gag.sasu.entity.Comment;
import gag.sasu.entity.Post;
import gag.sasu.entity.User;
import gag.sasu.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostService postService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService,
                          UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public CommentResponse addComment(CommentRequest commentRequest, String userId) {

        User user = userService.getById(userId);

        Post post = postService.getPost(commentRequest.getPostUid());

        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setPost(post);
        comment.setUser(user);

        return modelMapper.map(commentRepository.save(comment), CommentResponse.class);
    }

    public List<CommentResponse> getComments(String postId) {
        return commentRepository.findAllByPostUidOrderByCreatedOnDesc(postId)
                .stream()
                .map(comment -> modelMapper.map(comment, CommentResponse.class))
                .collect(Collectors.toList());
    }
}
