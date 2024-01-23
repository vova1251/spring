package com.example.demo.model;

import lombok.Data;

@Data
public class KafkaMessage {

    private String product;
    private String quantity;

}
