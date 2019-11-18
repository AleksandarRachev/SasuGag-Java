package test.demo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    private String title;

    private Integer points = 0;

    @Lob
    private byte[] image;

    @OneToOne
    private Category category;

    private Long createdOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void init() {
        this.createdOn = System.currentTimeMillis();
    }

}
