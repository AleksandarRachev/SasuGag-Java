package gag.sasu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gag.sasu.entity.VotedPost;
import gag.sasu.entity.VotedPostId;

public interface VotedPostRepository extends JpaRepository<VotedPost, VotedPostId> {


}
