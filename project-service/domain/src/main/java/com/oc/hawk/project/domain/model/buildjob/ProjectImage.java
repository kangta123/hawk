package com.oc.hawk.project.domain.model.buildjob;

import com.google.common.base.Objects;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectImage {
    private final String image;

    public ProjectImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectImage image1 = (ProjectImage) o;
        return Objects.equal(image, image1.image);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(image);
    }

    public String getApp() {
        return new ProjectImageApp(image).getApp();
    }

    @Override
    public String toString() {
        return image;
    }
}

