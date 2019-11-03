package test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.demo.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByName(String name);

}
