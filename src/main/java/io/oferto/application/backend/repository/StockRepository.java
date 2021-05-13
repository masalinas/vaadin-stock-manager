package io.oferto.application.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.oferto.application.backend.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
