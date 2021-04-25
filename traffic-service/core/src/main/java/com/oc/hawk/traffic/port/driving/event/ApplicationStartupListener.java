package com.oc.hawk.traffic.port.driving.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.oc.hawk.common.spring.ApplicationContextHolder;
import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import com.oc.hawk.traffic.application.entrypoint.TrafficTraceInfoUseCase;

@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent>{
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("容器启动执行加载redis缓存数据.");
		EntryPointUseCase entryPointUseCase = ApplicationContextHolder.getBean(EntryPointUseCase.class);
		entryPointUseCase.loadEntryPointConfigData();
	}
	
}
