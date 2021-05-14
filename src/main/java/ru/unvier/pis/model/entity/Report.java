package ru.unvier.pis.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "table_report", schema = "local")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sale_type")
    private String saleType;

    @Column(name = "sale_date")
    private Date saleDate;

    private Integer count;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @Column(name = "report_number")
    private Long reportNumber;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

//    @ManyToMany
//    @JoinTable(name = "mtm_report_product",
//            joinColumns = {@JoinColumn(name = "report_id")},
//            inverseJoinColumns = {@JoinColumn(name = "product_id")})
//    private List<Product> products;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id", referencedColumnName = "id")
//    private Product product;
}
