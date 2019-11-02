package test.demo.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String uid;

    private String name;
//
//    private Integer price;

    @Lob
    private byte[] image;

}
