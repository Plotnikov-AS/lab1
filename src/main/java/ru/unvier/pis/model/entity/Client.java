package ru.unvier.pis.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CLIENT")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;

    @Column(name = "COMMENT")
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FA_ID", referencedColumnName = "ID")
    private FinAccount finAccount;
}
