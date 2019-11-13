package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateItemRequest {

    @JsonProperty
    private String name;

    @JsonProperty
    private double price;

    @JsonProperty
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
