package com.oc.hawk.infrastructure.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.oc.hawk.infrastructure.application.exception.PodNotReadyException;
import com.oc.hawk.infrastructure.application.exception.PodStartFailedException;
import com.oc.hawk.kubernetes.domain.model.IServiceLabelConstants;
import com.oc.hawk.kubernetes.domain.model.KubernetesLabel;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DoneableDeployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import io.fabric8.kubernetes.client.dsl.ServiceResource;
import io.fabric8.kubernetes.client.internal.SerializationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KubernetesApi {
    private final KubernetesClient client;
    private final ExecutorService executorService = new ThreadPoolExecutor(20, 20,
        0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
        new ThreadFactoryBuilder().setDaemon(true).setNameFormat("kubernetes-keepAlive-execution-%d").build());

    public ServiceResource<Service, DoneableService> withService(String namespace, String serviceName) {

        return client.services().inNamespace(namespace).withName(serviceName);
    }

    public Service createService(String ns, Service service) {
        yaml(service);
        try {
            return client.services().inNamespace(StringUtils.defaultString(ns, KubernetesConstants.DEFAULT_NAMESPACE)).createOrReplace(service);
        } catch (KubernetesClientException e) {
            log.error("create service of {} error {}", service.getMetadata().getName(), e.getMessage());
        }
        return null;
    }

    public void execAndWatch(PodExecution execution, Consumer<String> outputConsumer) {
//        try {
//            PipedInputStream pis = new PipedInputStream();
//            PipedOutputStream pos = new PipedOutputStream(pis);
//            ExecWatch watch = client.pods().inNamespace(execution.getNamespace()).withName(execution.getName())
//                .readingInput(new PipedInputStream(new PipedOutputStream()))
//                .writingOutput(pos)
//                .writingError(System.err)
//                .withTTY()
//                .exec(execution.getCommand());
//            executorService.submit(() -> {
//                try {
//                    byte[] buffer = new byte[1024];
//
//                    int len;
//
//                    byte[] remain = null;
//
//                    while ((len = pis.read(buffer)) != -1) {
//                        int last = 0;
//                        for (int i = 0; i < len; i++) {
//                            final char c = (char) buffer[i];
//                            if ((c == '\n')) {
//                                String str = remain != null ? new String(remain) : "";
//                                outputConsumer.accept(str + new String(buffer, last, i - last));
//                                last = i;
//                                remain = null;
//                            }
//                        }
//                        remain = Arrays.copyOfRange(buffer, last, len);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public boolean isPodReady(String namespace, String name) {
        PodResource<Pod, DoneablePod> podDoneablePodPodResource = client.pods().inNamespace(namespace)
            .withName(name);
        try {
            if (podDoneablePodPodResource.get() == null) {
                throw new PodNotReadyException("服务启动失败, " + name);
            }
            podDoneablePodPodResource.waitUntilReady(30, TimeUnit.SECONDS);
            return true;
        } catch (InterruptedException e) {
            log.error("Cannot watch pod Because its not existed, {}", name, e);
            throw new PodStartFailedException("服务启动失败, " + name);
        }
    }


    public void createDeployment(String ns, Deployment deployment) {
        yaml(deployment);
        String namespace = StringUtils.defaultString(ns, KubernetesConstants.DEFAULT_NAMESPACE);
        client.apps().deployments().inNamespace(namespace).createOrReplace(deployment);
    }

    public List<Service> getSrv(String namespace) {
        return client.services().inNamespace(namespace).list().getItems();
    }

    public RollableScalableResource<Deployment, DoneableDeployment> getDeployment(String namespace, String name) {
        return client.apps().deployments().inNamespace(namespace).withName(name);
    }

    public List<KubernetesPod> queryPod(String namespace, Long projectId, String app, String version) {
        return listPods(namespace, KubernetesLabel.withBusinessService(projectId, app, version)).stream().map(KubernetesPod::createNew).collect(Collectors.toList());
    }

    public KubernetesPod getPod(String namespace, String name) {
        final PodResource<Pod, DoneablePod> podDoneablePodPodResource = withPod(name, namespace, 0);
        if (podDoneablePodPodResource != null) {
            return KubernetesPod.createNew(podDoneablePodPodResource.get());
        }
        return null;
    }


    public void deleteDeployments(String namespace, String name) {
        if (StringUtils.isNotEmpty(name)) {
            getDeployment(namespace, name).delete();
        } else {
            List<Deployment> deployments = this.withDeployments(namespace, KubernetesLabel.withBusinessService()).getItems();
            client.apps().deployments().delete(deployments);
        }
    }

    public void deletePod(String namespace, Map<String, String> labels) {
        client.pods().inNamespace(namespace).withLabels(labels).delete();
    }

    public void createPod(String namespace, Pod pod) {
        this.yaml(pod);
        String name = pod.getMetadata().getName();
        final NonNamespaceOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>> p = withPod(namespace);
        if (p.withName(name).get() == null) {
            p.create(pod);
        } else {
            log.info("Pod already existed, {}", name);
        }
    }

    public void readLog(String namespace, String podName, Consumer<String> outputConsumer) {
        isPodReady(namespace, podName);

        try {
            PipedInputStream pis = new PipedInputStream();
            PipedOutputStream pos = new PipedOutputStream(pis);

            withPod(namespace).withName(podName).watchLog(pos);

            executorService.submit(() -> {
                byte[] buffer = new byte[1024];
                int len;

                byte[] remain = null;

                try {
                    while ((len = pis.read(buffer)) != -1) {
                        int last = 0;
                        for (int i = 0; i < len; i++) {
                            final char c = (char) buffer[i];
                            if ((c == '\n')) {
                                String str = remain != null ? new String(remain) : "";
                                outputConsumer.accept(str + new String(buffer, last, i - last));
                                last = i;
                                remain = null;
                            }
                        }
                        remain = Arrays.copyOfRange(buffer, last, len);
                    }
                } catch (IOException e) {
                    log.warn("Read log channel closed");
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public KubernetesLog readFullLogs(String name, String namespace, int index, Integer timestamp) {
        Stopwatch started0 = Stopwatch.createStarted();

        PodResource<Pod, DoneablePod> pod = withPod(name, namespace, index);
        if (pod == null) {
            log.warn("Read full logs with empty pods. {}.{} [{}]", name, namespace, index);
            return KubernetesLog.empty();
        }
        String logs = "";

        log.info("take {} ms", started0.elapsed(TimeUnit.MILLISECONDS));
        int now = (int) LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        Stopwatch started = Stopwatch.createStarted();
        if (timestamp == null) {
            logs = pod.inContainer(KubernetesConstants.APP_CONTAINER_NAME).tailingLines(200).withPrettyOutput().getLog();
        } else {
            int diff = now - timestamp;
            if (diff > 0) {
                logs = pod.inContainer(KubernetesConstants.APP_CONTAINER_NAME).sinceSeconds(diff).withPrettyOutput().getLog();
            }
        }
        log.info("Read container log take {} ms", started.elapsed(TimeUnit.MILLISECONDS));

        return new KubernetesLog(now, logs);
    }

    private List<Pod> listPods(String namespace, Map<String, String> labels) {
        PodList list;
        if (!StringUtils.isEmpty(namespace)) {
            list = client.pods().inNamespace(namespace).withLabels(labels).list();
        } else {
            list = client.pods().inAnyNamespace().withLabels(labels).list();
        }
        return list.getItems();
    }

    private PodResource<Pod, DoneablePod> withPod(String name, String namespace, int index) {
        List<Pod> items = listPods(namespace, ImmutableMap.of(IServiceLabelConstants.LABEL_VERSION, name));
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        Pod pod = items.get(index);
        if (pod == null) {
            return null;
        }
        String podName = pod.getMetadata().getName();
        return client.pods().inNamespace(namespace).withName(podName);
    }

    private void yaml(HasMetadata data) {
        try {
            String yaml = SerializationUtils.dumpAsYaml(data);
            log.info(yaml);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private DeploymentList withDeployments(String namespace, Map<String, String> labels) {
        return client.apps().deployments().inNamespace(namespace).withLabels(labels).list();
    }

    private NonNamespaceOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>> withPod(String namespace) {
        return client.pods().inNamespace(namespace);
    }

    public void deleteService(String name, String namespace) {
        client.services().inNamespace(namespace).withName(name).delete();
    }

    public void createSecretIfNotExist(String namespace, String name, Map<String, String> data) {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        final Secret secret = client.secrets().inNamespace(namespace).withName(name).get();
        if (secret == null) {
            final Secret newSecret = new SecretBuilder().addToData(data).editOrNewMetadata().withName(name).endMetadata().build();
            yaml(newSecret);
            client.secrets().inNamespace(namespace).withName(name).create(newSecret);
        }
    }
}
