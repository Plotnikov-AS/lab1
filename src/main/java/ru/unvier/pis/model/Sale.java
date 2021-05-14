package ru.unvier.pis.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Sale {
    private String type;
    private Date date;
    private Long number;
    private Double amount;
}
