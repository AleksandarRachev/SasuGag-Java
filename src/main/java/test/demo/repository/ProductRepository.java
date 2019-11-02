package test.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {


    Optional<Product> findByName(String name);
}
