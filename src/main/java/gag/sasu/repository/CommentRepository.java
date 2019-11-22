package gag.sasu.repository;

import java.util.List;

import gag.sasu.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByPostUidOrderByCreatedOnDesc(String postId);

}
