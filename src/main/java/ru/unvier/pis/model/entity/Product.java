package ru.unvier.pis.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private String productPrice;

    @Column(name = "COUNT_LEFT")
    private String countLeft;

//    @ManyToMany(mappedBy = "products")
    @OneToMany(mappedBy = "product")
    private List<Report> reports;
//    @OneToOne(mappedBy = "product")
//    private Report report;
}
