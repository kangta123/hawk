package com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointConfigID {

	Long id;

    public EntryPointConfigID(Long id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
