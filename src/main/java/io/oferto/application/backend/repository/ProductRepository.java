package io.oferto.application.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.oferto.application.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
