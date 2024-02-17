package com.crisdev.api.springsecuritypractice.persistence.repository;

import com.crisdev.api.springsecuritypractice.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
