package test.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, String> {


    Optional<Post> findByTitle(String name);

    List<Post> findAllByCategoryName(String categoryName);

    List<Post> findAllByCategoryName(Pageable pageable, String categoryName);
}
