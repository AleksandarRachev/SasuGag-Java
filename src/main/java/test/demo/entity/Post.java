package test.demo.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Post {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    private String name;

    @Lob
    private byte[] image;

    @OneToOne
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
