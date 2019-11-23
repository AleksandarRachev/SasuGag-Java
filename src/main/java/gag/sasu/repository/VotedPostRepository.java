package gag.sasu.repository;

import gag.sasu.entity.VotedPost;
import gag.sasu.entity.VotedPostId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotedPostRepository extends JpaRepository<VotedPost, VotedPostId> {

    List<VotedPost> findAllByUpOrDown(boolean up, boolean down);

}
