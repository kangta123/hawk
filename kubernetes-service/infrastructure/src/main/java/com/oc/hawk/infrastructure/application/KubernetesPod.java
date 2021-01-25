package com.oc.hawk.infrastructure.application;

import com.oc.hawk.kubernetes.domain.model.IServiceLabelConstants;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodStatus;
import io.fabric8.kubernetes.client.internal.readiness.Readiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
public class KubernetesPod implements Comparable<KubernetesPod> {

    public static final String PENDING_STATUS = "Pending";
    private Pod pod;

    private KubernetesPod(Pod pod) {
        this.pod = pod;
    }

    public static KubernetesPod createNew(Pod pod) {
        if(pod == null){
            return null;
        }else{
            return new KubernetesPod(pod);
        }
    }

    public String getLabelValue(String key) {
        Map<String, String> labels = pod.getMetadata().getLabels();
        return labels.getOrDefault(key, "");
    }
    public String getProjectId() {
        return getLabelValue(IServiceLabelConstants.LABEL_PROJECT);
    }
    public boolean isBusinessRuntime() {
        return getLabelValue(IServiceLabelConstants.BUSINESS_SERVICE_CATALOG) != null;
    }

    public LocalDateTime getStartTime() {
        PodStatus status = pod.getStatus();
        LocalDateTime startTime = null;
        if (status.getStartTime() != null) {
            startTime = LocalDateTime.parse(status.getStartTime(), DateTimeFormatter.ISO_DATE_TIME);
        }
        return startTime;
    }

    public String getResourceVersion() {
        return pod.getMetadata().getResourceVersion();
    }

    public String getNamespace() {
        return pod.getMetadata().getNamespace();
    }

    public String getRuntimeId() {
        return pod.getMetadata().getName();
    }

    public String getRuntimeImage() {
        List<Container> containers = pod.getSpec().getInitContainers();
        return containers.stream().filter(c -> StringUtils.equals(c.getName(), KubernetesConstants.INIT_CONTAINER_NAME))
            .findFirst()
            .map(Container::getImage).orElse(null);
    }


    public Integer getRestartCount() {
        List<ContainerStatus> containerStatuses = pod.getStatus().getContainerStatuses();
        int restartCount = 0;
        if (!containerStatuses.isEmpty()) {
            ContainerStatus containerStatus = containerStatuses.iterator().next();
            restartCount = containerStatus.getRestartCount();
        }
        return restartCount;
    }

    public String getRuntimeStatus() {
        boolean ready = Readiness.isPodReady(pod);
        PodStatus podStatus = pod.getStatus();

        if (PENDING_STATUS.equals(podStatus.getPhase()) && CollectionUtils.isEmpty(podStatus.getConditions())) {
            return "ADDED";
        } else {
            if (isDeleting()) {
                return "STOP";
            } else {
                if (ready) {
                    return "STARTED";
                } else {
                    return "STARTING";
                }
            }
        }
    }

    public boolean isDeleting() {
        return Objects.nonNull(pod.getMetadata().getDeletionTimestamp());
    }


    public String getDomainHost(String host) {
        return new StringJoiner(".").add(getServiceName()).add(this.getNamespace()).add(host).toString();
    }

    public String getServiceName() {
        return getLabelValue(IServiceLabelConstants.LABEL_APP);
    }


    @Override
    public int compareTo(KubernetesPod o) {
        return Integer.parseInt(this.getResourceVersion()) - Integer.parseInt(o.getResourceVersion());
    }

    public String getName() {
        return getLabelValue(IServiceLabelConstants.LABEL_VERSION);
    }

    public boolean isReady() {
        return Readiness.isPodReady(pod);
    }


    /**
     * kubernetes 更新事件过于详细，我们只关注某些关键事件
     * 关注事件包括，
     * - 第一次变成pending状态
     * - 变成Ready状态
     */
    public boolean ignoreUpdateEvent() {
        boolean isFistPending = pod.getStatus().getConditions().size() == 1;

        if (!isReady()) {
            return !isFistPending;
        }
        return false;
     }

    public String getNodeIp() {
        return pod.getStatus().getHostIP();
    }
}
