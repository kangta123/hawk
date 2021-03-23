package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointHttpResource {
    //请求路径
    private EntryPointPath path;
    //请求方法
    private EntryPointMethod method;
    //请求目标
    private EntryPointTarget target;

    public EntryPointHttpResource(EntryPointPath path, EntryPointMethod method, EntryPointTarget target) {
        this.path = path;
        this.method = method;
        this.target = target;
    }
}
