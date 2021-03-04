package com.oc.hawk.transfer.entrypoint.domain.model.response;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class ResponseID {
	long id;
	
	public ResponseID(long id) {
        this.id = id;
    }
	
    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
