package test.demo.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotedPostId implements Serializable {

    @OneToOne
    private User user;

    @OneToOne
    private Post post;

}
