package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.api.exception.DomainNotFoundException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
public class InstanceImageVersion {

    private final List<InstanceImage> images;

    public InstanceImageVersion(List<InstanceImage> images) {
        this.images = images;
    }

    public InstanceImage getInstanceImage(String app) {
        return images.stream().filter(i -> StringUtils.equals(i.getApp(), app)).findFirst().orElseThrow(()-> new DomainNotFoundException(app));
    }
}
