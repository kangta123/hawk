package com.oc.hawk.container.domain.model.runtime.info;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@DomainValueObject
@NoArgsConstructor
public class RuntimeImage {
    private String image;

    public RuntimeImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getTag() {
        return StringUtils.substringAfterLast(image, ":");
    }

    @Override
    public String toString() {
        return image;
    }
}
