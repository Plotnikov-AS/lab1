package ru.unvier.pis.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "FIN_ACCOUNT")
public class FinAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOTAL_SPENT")
    private Double totalSpent;

    @OneToOne(mappedBy = "finAccount")
    private Client client;
}
