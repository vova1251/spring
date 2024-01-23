package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderStatus {

    private String status;
    private Instant date;

}
