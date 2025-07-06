package com.irisandco.ecommerce_optic.product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService PRODUCT_SERVICE;

    public ProductController(ProductService productService) {
        PRODUCT_SERVICE = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getAllProducts() { List<ProductResponse> products = PRODUCT_SERVICE.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse gotProduct = PRODUCT_SERVICE.getProductResponseById(id);
        return new ResponseEntity<>(gotProduct, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) { ProductResponse createdProduct = PRODUCT_SERVICE.createProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = PRODUCT_SERVICE.updateProduct(id, productRequest);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        PRODUCT_SERVICE.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Para filtrar
    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<ProductResponse> products = PRODUCT_SERVICE.filterProducts(name, categoryName, minPrice, maxPrice);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> uploadProductImage(@PathVariable final Long id, @RequestParam final MultipartFile file) {
        getProductById(id);
        this.PRODUCT_SERVICE.uploadImage(id, file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/filters")
    public ResponseEntity<Page<ProductResponse>> getFilteredProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> categoryNames,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) boolean featured,
            Pageable pageable) {
        Page<Product> products = PRODUCT_SERVICE.getFilteredProducts(name, categoryNames, minPrice, maxPrice, featured, pageable);
        List<ProductResponse> productResponses = products.stream().map(ProductMapper::toDto).toList();
        Page<ProductResponse> productResponses1 = new PageImpl<>(productResponses, pageable, productResponses.size());
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productResponses1, HttpStatus.OK);
    }
}
