package com.oc.hawk.project.domain.model.buildjob;

import com.google.common.base.Objects;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

import java.time.LocalDateTime;
@DomainValueObject
@Getter
public class InstanceImageInfo implements Comparable<InstanceImageInfo> {
    private final String tag;
    private final String image;
    private final String branch;
    private final long jobId;
    private final LocalDateTime time;

    public InstanceImageInfo(String tag, String image, String branch, long jobId, LocalDateTime now) {
        this.tag = tag;
        this.image = image;
        this.branch = branch;
        this.jobId = jobId;
        this.time = now;
    }
    public String getApp(){
        return new ProjectImageApp(this.image).getApp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceImageInfo that = (InstanceImageInfo) o;
        return jobId == that.jobId &&
            Objects.equal(tag, that.tag) &&
            Objects.equal(image, that.image) &&
            Objects.equal(branch, that.branch);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tag, image, branch, jobId);
    }

    @Override
    public int compareTo(InstanceImageInfo o) {
        return this.getTime().isBefore(o.getTime()) ? 1 : -1;
    }
}
