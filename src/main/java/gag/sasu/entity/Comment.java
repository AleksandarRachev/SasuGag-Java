package gag.sasu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

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
