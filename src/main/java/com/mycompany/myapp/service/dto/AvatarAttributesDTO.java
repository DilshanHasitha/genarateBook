package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.AvatarAttributes;
import java.util.Set;

public class AvatarAttributesDTO {

    private String code;
    private Set<AvatarAttributes> avatarAttributes;

    public AvatarAttributesDTO() {}

    public AvatarAttributesDTO(Set<AvatarAttributes> avatarAttributes) {
        this.avatarAttributes = avatarAttributes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<AvatarAttributes> getAvatarAttributes() {
        return avatarAttributes;
    }

    public void setAvatarAttributes(Set<AvatarAttributes> avatarAttributes) {
        this.avatarAttributes = avatarAttributes;
    }
}
