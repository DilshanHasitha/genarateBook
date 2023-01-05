package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.AvatarAttributes;
import com.mycompany.myapp.domain.OptionType;
import com.mycompany.myapp.domain.Options;
import com.mycompany.myapp.domain.Styles;
import java.util.Set;

public class AvatarAttributesDTO {

    private String description;
    private String templateText;
    private String characterCode;
    private Set<Styles> styles;
    private Set<Options> options;
    private String optionType;

    public AvatarAttributesDTO() {}

    public AvatarAttributesDTO(
        String description,
        String templateText,
        String characterCode,
        Set<Styles> styles,
        Set<Options> options,
        String optionType
    ) {
        this.description = description;
        this.templateText = templateText;
        this.characterCode = characterCode;
        this.styles = styles;
        this.options = options;
        this.optionType = optionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

    public String getCharacterCode() {
        return characterCode;
    }

    public void setCharacterCode(String characterCode) {
        this.characterCode = characterCode;
    }

    public Set<Styles> getStyles() {
        return styles;
    }

    public void setStyles(Set<Styles> styles) {
        this.styles = styles;
    }

    public Set<Options> getOptions() {
        return options;
    }

    public void setOptions(Set<Options> options) {
        this.options = options;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }
}
