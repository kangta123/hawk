package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointGroupID {

	Long id;
	
    public EntryPointGroupID(Long id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return String.valueOf(id);
    }
	
}
