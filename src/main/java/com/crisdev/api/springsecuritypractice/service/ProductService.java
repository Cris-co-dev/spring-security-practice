package com.crisdev.api.springsecuritypractice.service;

import com.crisdev.api.springsecuritypractice.dto.SaveProduct;
import com.crisdev.api.springsecuritypractice.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface ProductService {
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findOneById(Long id);

    Product createOne(SaveProduct saveProduct);

    Product updateOneByID(Long productId, SaveProduct saveProduct);

    Product disableOneByID(Long productId);
}
