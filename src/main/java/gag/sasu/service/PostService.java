package gag.sasu.service;

import gag.sasu.dto.PostResponse;
import gag.sasu.dto.PostVoteRequest;
import gag.sasu.entity.*;
import gag.sasu.exception.ElementExistsException;
import gag.sasu.exception.ElementMissingException;
import gag.sasu.exception.ImageMissingException;
import gag.sasu.exception.UnsupportedImageFormatException;
import gag.sasu.repository.PostRepository;
import gag.sasu.repository.VotedPostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final VotedPostRepository votedPostRepository;
    private List<String> validImageExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

    @Autowired
    public PostService(PostRepository postRepository, ModelMapper modelMapper, CategoryService categoryService,
                       UserService userService, VotedPostRepository votedPostRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.userService = userService;
        this.votedPostRepository = votedPostRepository;
    }

    public int getPostsCount() {
        return postRepository.findAll().size();
    }

    public int getPostCountByCategoryName(String categoryName) {
        return postRepository.findAllByCategoryNameOrderByCreatedOnDesc(categoryName).size();
    }

    private void validatePost(String name, MultipartFile image) throws ElementExistsException {
        Optional<Post> post = postRepository.findByTitle(name);
        if (post.isPresent()) {
            throw new ElementExistsException("Element with that name already exists");
        }
        validateImage(image);
    }

    public PostResponse savePost(MultipartFile image, String name, String categoryName, String userId) throws ElementExistsException, IOException {
        validatePost(name, image);
        Category category = categoryService.getCategory(categoryName);
        Post post = new Post();
        post.setTitle(name);
        post.setCategory(category);
        post.setImage(image.getBytes());

        User user = userService.getById(userId);
        post.setUser(user);

        return modelMapper.map(postRepository.save(post), PostResponse.class);
    }

    private void validateImage(MultipartFile file) {
        String imageExtension = getExtension(file);

        validateExtension(imageExtension);
    }

    private String getExtension(MultipartFile file) {
        if (file == null) {
            throw new ImageMissingException("Image missing");
        }
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElseThrow(() -> new ImageMissingException("Image missing"));
        String[] chunks = fileName.split("\\.");
        return chunks[chunks.length - 1];
    }

    private void validateExtension(String extension) {
        for (String validExtension : validImageExtensions) {
            if (validExtension.equalsIgnoreCase(extension)) {
                return;
            }
        }
        throw new UnsupportedImageFormatException("Unsupported image format");
    }

    public List<PostResponse> getAllPosts(int page) {
        return postRepository.findAllByOrderByCreatedOnDesc(PageRequest.of(page, 5))
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .collect(Collectors.toList());
    }

    public List<PostResponse> getAllPostsByCategory(int page, String category) {
        return postRepository.findAllByCategoryNameOrderByCreatedOnDesc(PageRequest.of(page, 5), category)
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .collect(Collectors.toList());
    }

    Post getPost(String id) {
        return postRepository.findById(id).orElseThrow(() -> new ElementMissingException("No such post"));
    }

    public byte[] downloadImage(String productId) {
        Post post = getPost(productId);
        return Optional.ofNullable(post.getImage()).orElseThrow(() -> new ImageMissingException("No image for product"));
    }

    public PostResponse getPostById(String postId) {
        Post post = getPost(postId);
        return modelMapper.map(post, PostResponse.class);
    }

    private void changePostPoints(Post post, String vote, VotedPost votedPost) {
        switch (vote) {
            case "up":
                voteUp(post, votedPost);
                break;
            case "down":
                voteDown(post, votedPost);
                break;
            default:
                post.setPoints(post.getPoints());
        }
        votedPostRepository.save(votedPost);
        postRepository.save(post);
    }

    private void voteUp(Post post, VotedPost votedPost) {
        if (votedPost.getUp() && !votedPost.getDown()) {
            post.setPoints(post.getPoints() - 1);
            votedPost.setUp(false);
        } else {
            if (!votedPost.getUp() && votedPost.getDown()) {
                post.setPoints(post.getPoints() + 2);
                votedPost.setUp(true);
                votedPost.setDown(false);
            } else {
                post.setPoints(post.getPoints() + 1);
                votedPost.setUp(true);
            }
        }
    }

    private void voteDown(Post post, VotedPost votedPost) {
        if (votedPost.getDown() && !votedPost.getUp()) {
            post.setPoints(post.getPoints() + 1);
            votedPost.setDown(false);
        } else {
            if (!votedPost.getDown() && votedPost.getUp()) {
                post.setPoints(post.getPoints() - 2);
                votedPost.setDown(true);
                votedPost.setUp(false);
            } else {
                post.setPoints(post.getPoints() - 1);
                votedPost.setDown(true);
            }
        }
    }

    public PostResponse voteForPost(PostVoteRequest postVoteRequest, String userId) {
        Post post = getPost(postVoteRequest.getUid());
        User user = userService.getById(userId);

        VotedPost votedPost = getVotedPost(post, user);

        changePostPoints(post, postVoteRequest.getVote(), votedPost);
        return modelMapper.map(post, PostResponse.class);
    }

    private VotedPost getVotedPost(Post post, User user) {
        VotedPostId id = new VotedPostId(user, post);
        Optional<VotedPost> votedPost = votedPostRepository.findById(id);
        if (votedPost.isEmpty()) {
            VotedPost newVotedPost = new VotedPost();
            newVotedPost.setUid(id);
            return newVotedPost;
        }
        return votedPostRepository.save(votedPost.get());
    }
}
