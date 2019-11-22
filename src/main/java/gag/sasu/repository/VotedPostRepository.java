package gag.sasu.repository;

import gag.sasu.entity.VotedPost;
import gag.sasu.entity.VotedPostId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotedPostRepository extends JpaRepository<VotedPost, VotedPostId> {


}
