package com.oc.hawk.project.api.dto;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class InstanceImageDTO {
    private String tag;
    private String app;
    //    private String image;
    private String branch;
    private Long jobId;
    private LocalDateTime time;

    public InstanceImageDTO(String tag, String app, String branch, Long jobId, LocalDateTime time) {
        this.tag = tag;
        this.app = app;
        this.branch = branch;
        this.jobId = jobId;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceImageDTO that = (InstanceImageDTO) o;
        return Objects.equal(tag, that.tag) &&
            Objects.equal(app, that.app);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tag, app);
    }
}
