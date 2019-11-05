package test.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import test.demo.dto.ProductResponse;
import test.demo.entity.Category;
import test.demo.entity.Post;
import test.demo.exception.ElementExistsException;
import test.demo.exception.ElementMissingException;
import test.demo.exception.ImageMissingException;
import test.demo.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }

    public int getProductsCount(){
        return productRepository.findAll().size();
    }

    public int getProductCountByCategoryName(String categoryName){
        return productRepository.findAllByCategoryName(categoryName).size();
    }

    private void validateProduct(String name) throws ElementExistsException {
        Optional<Post> product = productRepository.findByName(name);
        if(product.isPresent()){
            throw new ElementExistsException("Element with that name already exists");
        }
    }

    public ProductResponse saveProduct(MultipartFile image, String name, String categoryName) throws ElementExistsException, IOException {
        validateProduct(name);
        Category category = categoryService.getCategory(categoryName);
        Post post = new Post();
        post.setName(name);
        post.setImage(image.getBytes());
        post.setCategory(category);
        return modelMapper.map(productRepository.save(post), ProductResponse.class);
    }

    public List<ProductResponse> getAllProducts(int page) {
        return productRepository.findAll(PageRequest.of(page, 5))
                .stream()
                .map(post -> modelMapper.map(post, ProductResponse.class))
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getAllProductsByCategory(int page, String category){
        return productRepository.findAllByCategoryName(PageRequest.of(page, 5), category)
                .stream()
                .map(post -> modelMapper.map(post, ProductResponse.class))
                .collect(Collectors.toList());
    }

    private Post getProduct(String id){
        Optional<Post> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ElementMissingException("No such product");
        }
        return product.get();
    }

    public byte[] downloadImage(String productId) {
        Post post = getProduct(productId);
        return Optional.ofNullable(post.getImage()).orElseThrow(() -> new ImageMissingException("No image for product"));
    }
}
