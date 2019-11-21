package test.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByName(String name);

}
