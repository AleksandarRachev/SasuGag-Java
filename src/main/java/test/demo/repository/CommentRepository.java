package test.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByPostUidOrderByCreatedOnDesc(String postId);

}
