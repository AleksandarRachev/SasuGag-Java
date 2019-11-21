package test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import test.demo.entity.VotedPost;
import test.demo.entity.VotedPostId;

public interface VotedPostRepository extends JpaRepository<VotedPost, VotedPostId> {


}
