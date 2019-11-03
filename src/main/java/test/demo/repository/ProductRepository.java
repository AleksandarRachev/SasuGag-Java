package test.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {


    Optional<Product> findByName(String name);

    List<Product> findAllByCategoryName(String categoryName);

    List<Product> findAllByCategoryName(Pageable pageable, String categoryName);
}
