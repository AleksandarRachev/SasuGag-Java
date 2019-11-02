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
import test.demo.dto.ProductSaveRequest;
import test.demo.dto.ProductRespnse;
import test.demo.entity.Product;
import test.demo.exception.ElementExistsException;
import test.demo.exception.ElementMissingException;
import test.demo.exception.ImageMissingException;
import test.demo.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private void validateProduct(String name) throws ElementExistsException {
        Optional<Product> product = productRepository.findByName(name);
        if(product.isPresent()){
            throw new ElementExistsException("Element with that name already exists");
        }
    }

    public ProductSaveRequest saveProduct(MultipartFile image, String name) throws ElementExistsException, IOException {
        validateProduct(name);
        Product product = new Product();
        product.setName(name);
        product.setImage(image.getBytes());
        return modelMapper.map(productRepository.save(product), ProductSaveRequest.class);
    }

    public List<ProductRespnse> getAllProducts(int page) {
        return productRepository.findAll(PageRequest.of(page, 10))
                .stream()
                .map(product -> modelMapper.map(product, ProductRespnse.class))
                .collect(Collectors.toList());
    }

    private Product getProduct(String id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ElementMissingException("No such product");
        }
        return product.get();
    }

    public byte[] downloadImage(String productId) {
        Product product = getProduct(productId);
        return Optional.ofNullable(product.getImage()).orElseThrow(() -> new ImageMissingException("No image for product"));
    }
}
