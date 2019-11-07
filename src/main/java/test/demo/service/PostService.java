package test.demo.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import test.demo.dto.PostResponse;
import test.demo.entity.Category;
import test.demo.entity.Post;
import test.demo.entity.User;
import test.demo.exception.ElementExistsException;
import test.demo.exception.ElementMissingException;
import test.demo.exception.ImageMissingException;
import test.demo.exception.UnsupportedImageFormatException;
import test.demo.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private List<String> validImageExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

    @Autowired
    public PostService(PostRepository postRepository, ModelMapper modelMapper, CategoryService categoryService,
                       UserService userService) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.userService = userService;
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

    private Post getPost(String id) {
        return postRepository.findById(id).orElseThrow(() -> new ElementMissingException("No such post"));
    }

    public byte[] downloadImage(String productId) {
        Post post = getPost(productId);
        return Optional.ofNullable(post.getImage()).orElseThrow(() -> new ImageMissingException("No image for product"));
    }
}
