package gag.sasu.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    private String text;

    private Long createdOn;

    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @PrePersist
    public void init() {
        this.createdOn = System.currentTimeMillis();
    }

}
