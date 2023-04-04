package com.mycompany.myapp.service.dto;

public class CustomOptionDetailsDTO {

    private String description;
    private Double price;
    private String code;

    public CustomOptionDetailsDTO() {}

    public CustomOptionDetailsDTO(String description, Double price, String code) {
        this.description = description;
        this.price = price;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
