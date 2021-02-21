package ru.unvier.pis.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FulfillResult {
    private String status;
    private String message;
}
