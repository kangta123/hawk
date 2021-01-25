package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.model.runtime.config.volume.AppInstanceVolume;

public class AppVolumeDecorator extends AbstractInstanceConfigDecorator {
    private final static String APP_SPRINGBOOT_PATH = "/tmp/app:/app/jar";
    private final static String APP_TOMCAT_PATH = "/tmp/app:/usr/local/apache-tomcat-8.5.46/webapps/ROOT";
    private final static String APP_NGINX_FOLDER = "/tmp/app:/usr/local/openresty/nginx/html";
    private final static String APP_VOLUME_NAME = "app-volume";

    public AppVolumeDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        super(configRuntimeDecorator);
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        if (instanceConfig instanceof TomcatInstanceConfig) {
            baseConfig.addVolume(new AppInstanceVolume(APP_VOLUME_NAME, APP_TOMCAT_PATH));
        } else if (instanceConfig instanceof SpringBootInstanceConfig) {
            baseConfig.addVolume(new AppInstanceVolume(APP_VOLUME_NAME, APP_SPRINGBOOT_PATH));
        } else if (instanceConfig instanceof NginxInstanceConfig) {
            baseConfig.addVolume(new AppInstanceVolume(APP_VOLUME_NAME, APP_NGINX_FOLDER));
        }
    }
}
