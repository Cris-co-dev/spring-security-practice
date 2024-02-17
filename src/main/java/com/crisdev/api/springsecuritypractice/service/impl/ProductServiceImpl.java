package com.crisdev.api.springsecuritypractice.service.impl;

import com.crisdev.api.springsecuritypractice.dto.SaveProduct;
import com.crisdev.api.springsecuritypractice.exception.ObjectNotFoundException;
import com.crisdev.api.springsecuritypractice.persistence.entity.Category;
import com.crisdev.api.springsecuritypractice.persistence.entity.Product;
import com.crisdev.api.springsecuritypractice.persistence.repository.ProductRepository;
import com.crisdev.api.springsecuritypractice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findOneById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createOne(SaveProduct saveProduct) {

        Product product = new Product();

        product.setName(saveProduct.getName());
        product.setPrice(saveProduct.getPrice());
        product.setStatus(Product.ProductStatus.ENABLED);
        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Product updateOneByID(Long productId, SaveProduct saveProduct) {
        Product productFromDB = productRepository.findById(productId).orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));

        productFromDB.setName(saveProduct.getName());
        productFromDB.setPrice(saveProduct.getPrice());

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        productFromDB.setCategory(category);

        return productRepository.save(productFromDB);
    }

    @Override
    public Product disableOneByID(Long productId) {
        Product productFromDB = productRepository.findById(productId).orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));
        productFromDB.setStatus(Product.ProductStatus.DISABLED);
        return productRepository.save(productFromDB);
    }
}
