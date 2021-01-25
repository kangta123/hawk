package com.oc.hawk.api.constant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountHolder {
    private Long id;

    public AccountHolder(Long id) {
        this.id = id;
    }

}
