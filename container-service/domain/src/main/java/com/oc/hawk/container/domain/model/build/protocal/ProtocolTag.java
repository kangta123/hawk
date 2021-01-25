package com.oc.hawk.container.domain.model.build.protocal;

import lombok.Data;

@Data
public class ProtocolTag {
    private String tag;
    private boolean isSuccess;
    private String data;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProtocolTag{");
        sb.append("tag='").append(tag).append('\'');
        sb.append(", isSuccess=").append(isSuccess);
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
