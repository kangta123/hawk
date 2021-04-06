package com.oc.hawk.traffic.port.driven.facade;

import com.oc.hawk.traffic.entrypoint.domain.facade.FileFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemoteFileFacade implements FileFacade{

    @Override
    public byte[] getDownloadFile(String fileName) {
        Resource resource = new ClassPathResource("files/" + fileName);
        try {
            return IOUtils.toByteArray(resource.getInputStream());
        } catch (Exception e) {
            log.error("get download file error:{}", e.getMessage(), e);
        }
        return null;
    }
}
