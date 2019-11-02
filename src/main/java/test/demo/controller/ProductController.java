package test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import test.demo.dto.ProductRespnse;
import test.demo.dto.ProductSaveRequest;
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
    public ResponseEntity<ProductSaveRequest> saveProduct(@RequestPart("file") MultipartFile image, @RequestParam("name") String name) throws ElementExistsException, IOException {
        return ResponseEntity.ok(productService.saveProduct(image, name));
    }

    @GetMapping(value = "/{productId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable(value = "productId") String productId) {
        return ResponseEntity.ok(productService.downloadImage(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductRespnse>> getAllProducts(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        return ResponseEntity.ok(productService.getAllProducts(page - 1));
    }

}
