package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.AvatarAttributes;
import java.util.Set;

public class UpdateBooksAvatarAttributesDTO {

    private String code;
    private Set<AvatarAttributesDTO> avatarAttributes;

    public UpdateBooksAvatarAttributesDTO() {}

    public UpdateBooksAvatarAttributesDTO(String code, Set<AvatarAttributesDTO> avatarAttributes) {
        this.code = code;
        this.avatarAttributes = avatarAttributes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<AvatarAttributesDTO> getAvatarAttributes() {
        return avatarAttributes;
    }

    public void setAvatarAttributes(Set<AvatarAttributesDTO> avatarAttributes) {
        this.avatarAttributes = avatarAttributes;
    }
}
