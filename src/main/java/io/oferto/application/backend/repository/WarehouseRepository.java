package io.oferto.application.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.oferto.application.backend.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
