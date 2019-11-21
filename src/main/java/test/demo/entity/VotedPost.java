package test.demo.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "voted_posts")
public class VotedPost {

    @EmbeddedId
    private VotedPostId uid;

    private Boolean up = false;

    private Boolean down = false;

}
