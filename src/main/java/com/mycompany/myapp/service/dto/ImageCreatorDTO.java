package com.mycompany.myapp.service.dto;

import java.util.Set;

public class ImageCreatorDTO {

    int pageWidth;
    int pageHeight;
    Set<ImageParameterDTO> image;

    public ImageCreatorDTO() {}

    public ImageCreatorDTO(int pageWidth, int pageHeight, Set<ImageParameterDTO> image) {
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.image = image;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public Set<ImageParameterDTO> getImage() {
        return image;
    }

    public void setImage(Set<ImageParameterDTO> image) {
        this.image = image;
    }
}
