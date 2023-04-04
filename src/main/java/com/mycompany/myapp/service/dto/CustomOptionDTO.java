package com.mycompany.myapp.service.dto;

import java.util.List;

public class CustomOptionDTO {

    private String type;
    private String optionType;
    private String storeCode;
    private String code;
    private Boolean isActive;
    private Double price;
    private List<CustomOptionDetailsDTO> customOptionDetails;

    public CustomOptionDTO() {}

    public CustomOptionDTO(
        String type,
        String optionType,
        String storeCode,
        String code,
        Boolean isActive,
        Double price,
        List<CustomOptionDetailsDTO> customOptionDetails
    ) {
        this.type = type;
        this.optionType = optionType;
        this.storeCode = storeCode;
        this.code = code;
        this.isActive = isActive;
        this.price = price;
        this.customOptionDetails = customOptionDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<CustomOptionDetailsDTO> getCustomOptionDetails() {
        return customOptionDetails;
    }

    public void setCustomOptionDetails(List<CustomOptionDetailsDTO> customOptionDetails) {
        this.customOptionDetails = customOptionDetails;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
