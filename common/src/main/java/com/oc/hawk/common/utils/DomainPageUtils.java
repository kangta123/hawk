package com.oc.hawk.common.utils;

import com.oc.hawk.ddd.web.DomainPage;
import org.springframework.data.domain.Page;

public class DomainPageUtils {
    public static <T> DomainPage<T> create(Page<T> page) {
        return new DomainPage<>(page.getNumber(), page.getSize(), page.getNumberOfElements(), page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(), page.getTotalElements());
    }
}
