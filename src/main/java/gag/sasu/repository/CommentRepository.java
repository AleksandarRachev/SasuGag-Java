package gag.sasu.repository;

import gag.sasu.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByPostUidOrderByCreatedOnDesc(String postId);

}
