package gag.sasu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gag.sasu.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByName(String name);

}
