package com.oc.hawk.ddd.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainPage<T> {
    int number;
    int size;
    int numberOfElements;
    List<T> content;
    boolean first;
    boolean last;
    private int totalPages;
    private long totalElements;

    public DomainPage(List<T> content, Integer total) {
        this.content = content;
        if (total == null) {
            this.totalElements = content.size();
        } else {
            this.totalElements = total;
        }
    }

    public DomainPage(List<T> content) {
        this(content, null);
    }

    public <U> DomainPage<U> map(Function<T, U> convert) {
        List<U> list = this.content.stream().map(convert).collect(Collectors.toList());
        return map(list);
    }

    public <U> DomainPage<U> map(List<U> list) {
        return new DomainPage<>(number, size, numberOfElements, list, first, last, totalPages, totalElements);
    }

}
