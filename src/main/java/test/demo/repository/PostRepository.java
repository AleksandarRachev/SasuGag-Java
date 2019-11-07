package test.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, String> {

    Optional<Post> findByTitle(String name);

    List<Post> findAllByOrderByCreatedOnDesc(Pageable pageable);

    List<Post> findAllByCategoryNameOrderByCreatedOnDesc(String categoryName);

    List<Post> findAllByCategoryNameOrderByCreatedOnDesc(Pageable pageable, String categoryName);
}
