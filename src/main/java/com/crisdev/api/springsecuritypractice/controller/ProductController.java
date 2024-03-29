package com.crisdev.api.springsecuritypractice.controller;

import com.crisdev.api.springsecuritypractice.dto.SaveProduct;
import com.crisdev.api.springsecuritypractice.persistence.entity.Product;
import com.crisdev.api.springsecuritypractice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


//casos de uso

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT_ADMIN')")
    public ResponseEntity<Page<Product>> findAll(Pageable pageable) {

        Page<Product> productsPage = productService.findAll(pageable);

        if (productsPage.hasContent()) {
            return ResponseEntity.ok(productsPage);
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT_ADMIN')")
    public ResponseEntity<Product> findOneById(@PathVariable Long productId) {

        Optional<Product> product = productService.findOneById(productId);

        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createOne(@RequestBody @Valid SaveProduct saveProduct) {

        Product product = productService.createOne(saveProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);


    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT_ADMIN')")
    public ResponseEntity<Product> updateOneById(@RequestBody @Valid SaveProduct saveProduct, @PathVariable Long productId) {

        Product product = productService.updateOneByID(productId,saveProduct);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}/disabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> disableOneById(@PathVariable Long productId) {

        Product product = productService.disableOneByID(productId);
        return ResponseEntity.ok(product);
    }



}
