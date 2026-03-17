package com.primerproyecto.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.primerproyecto.api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
