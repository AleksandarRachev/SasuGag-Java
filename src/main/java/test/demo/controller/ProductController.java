package test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import test.demo.dto.ProductResponse;
import test.demo.exception.ElementExistsException;
import test.demo.service.ProductService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> saveProduct(@RequestPart("file") MultipartFile image,
                                                       @RequestParam("name") String name,
                                                       @RequestParam("category") String categoryName) throws ElementExistsException, IOException {
        return ResponseEntity.ok(productService.saveProduct(image, name, categoryName));
    }

    @GetMapping(value = "/{productId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable(value = "productId") String productId) {
        return ResponseEntity.ok(productService.downloadImage(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        return ResponseEntity.ok(productService.getAllProducts(page - 1));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> getAllProductsByCategory(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                          @RequestParam(value = "category", required = false) String categoryName){
        return ResponseEntity.ok(productService.getAllProductsByCategory(page -1, categoryName));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getProductsCount(){
        return ResponseEntity.ok(productService.getProductsCount());
    }

    @GetMapping("/count/filter")
    public ResponseEntity<Integer> getProductsCount(@RequestParam(value = "category", required = false, defaultValue = "") String categoryName){
        return ResponseEntity.ok(productService.getProductCountByCategoryName(categoryName));
    }

}
