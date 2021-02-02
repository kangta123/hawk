package com.oc.hawk.kubernetes.runtime.application.runtime;

import com.google.common.collect.Lists;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.ServiceVolumeSpec;
import io.fabric8.kubernetes.api.model.*;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


@Getter
public class ServiceVolumeComponent {
    private final static String SPLIT_SIGN = ":";
    private final List<ServiceVolumeSpec> specs;

//    private final List<Volume> volumes = Lists.newArrayList();
//    private final List<VolumeMount> mounts = Lists.newArrayList();
//


    public ServiceVolumeComponent(List<ServiceVolumeSpec> specs) {
        this.specs = specs;
    }

//    public static ServiceVolumeComponent fromDefinitions(List<VolumeDefinition> definitions) {
//        ServiceVolumeComponent serviceVolumeComponent = new ServiceVolumeComponent();
//        for (VolumeDefinition definition : definitions) {
//            serviceVolumeComponent.addVolume(definition.getVolume());
//            serviceVolumeComponent.addMount(definition.getVolumeMount());
//        }
//        return serviceVolumeComponent;
//    }

    public List<VolumeMount> getVolumeMounts(Integer index) {
        List<VolumeMount> volumeMounts = Lists.newArrayList();
        specs.forEach(spec -> volumeMounts.add(createVolumeMount(spec, index)));
        return volumeMounts;
    }

    public List<Volume> getVolumes() {
        List<Volume> volumeMounts = Lists.newArrayList();
        specs.forEach(spec -> volumeMounts.add(createVolume(spec)));
        return volumeMounts;
    }

    public void addVolume(ServiceVolumeSpec volume) {
        specs.add(volume);
    }
//
//    public void addVolumes(List<Volume> volumes) {
//        volumes.forEach(this::addVolume);
//    }
//
//    public void addMounts(List<VolumeMount> volumeMounts) {
//        volumeMounts.forEach(this::addMount);
//    }

//    public List<VolumeMount> getVolumeMounts() {
//        return getMounts(VolumeMountPath::getVolumePath);
//    }
//
//    public List<VolumeMount> getLocalMounts() {
//        return getMounts(VolumeMountPath::getMountPath);
//    }

    private Volume createVolume(ServiceVolumeSpec spec) {
        Volume sVolume = new Volume();
        sVolume.setName(spec.getVolumeName());
        switch (spec.getType()) {
            case pvc:
                PersistentVolumeClaimVolumeSource persistentVolumeClaim = new PersistentVolumeClaimVolumeSource();
                persistentVolumeClaim.setClaimName(spec.getVolumeName());
                persistentVolumeClaim.setReadOnly(false);
                sVolume.setPersistentVolumeClaim(persistentVolumeClaim);
                break;
            case empty:
                sVolume.setEmptyDir(new EmptyDirVolumeSource());
                break;
            case host:
                sVolume.setHostPath(new HostPathVolumeSource(getPath(spec.getMountPath(), 0), "File"));
                break;
            default:
                break;
        }
        return sVolume;
    }

    private VolumeMount createVolumeMount(ServiceVolumeSpec spec, Integer index) {
        VolumeMount volumeMount = new VolumeMount();
        String path = spec.getMountPath();
        path = getPath(path, index);
        volumeMount.setMountPath(path);
        volumeMount.setReadOnly(false);
        if (StringUtils.isNotEmpty(spec.getSubPath())) {
            volumeMount.setSubPath(spec.getSubPath());
        }
        volumeMount.setName(spec.getVolumeName());
        return volumeMount;
    }

    private String getPath(String path, Integer index) {
        if (index != null && path.contains(SPLIT_SIGN)) {
            path = path.split(SPLIT_SIGN)[index];
        }
        return path;
    }

//    private List<VolumeMount> getMounts(Function<VolumeMountPath, String> volumeFun) {
//        return getMounts().stream().map(vm -> {
//            String mountPath = vm.getMountPath();
//            VolumeMount newVolumeMount = BeanUtils.transform(VolumeMount.class, vm);
//            VolumeMountPath volumeMountPath = new VolumeMountPath(mountPath);
//            String path = volumeFun.apply(volumeMountPath);
//            if (path == null) {
//                return null;
//            }
//            newVolumeMount.setMountPath(path);
//            return newVolumeMount;
//        }).filter(Objects::nonNull).collect(Collectors.toList());
//    }

//    public List<VolumeMount> splitMountPaths(int index) {
//        return getMounts().stream().map(vm -> {
//            String mountPath = vm.getMountPath();
//            if (mountPath.contains(MULTI_PATH_SPLIT_FLAG)) {
//                VolumeMount newVolumeMount = BeanUtils.transform(VolumeMount.class, vm);
//                newVolumeMount.setMountPath(mountPath.split(MULTI_PATH_SPLIT_FLAG)[index]);
//                return newVolumeMount;
//            }
//            return vm;
//        }).collect(Collectors.toList());
//    }

}
