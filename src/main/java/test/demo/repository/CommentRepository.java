package test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.demo.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByPostUidOrderByCreatedOnDesc(String postId);

}
