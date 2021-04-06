package com.oc.hawk.traffic.port.driven.facade;

import com.oc.hawk.traffic.entrypoint.domain.facade.FileFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemoteFileFacade implements FileFacade{

    @Override
    public byte[] getDownloadFile() {
        Resource resource = new ClassPathResource("files/agent.wasm");
        try (BufferedInputStream bin = new BufferedInputStream(resource.getInputStream());
             ByteArrayOutputStream bout = new ByteArrayOutputStream()
        ) {
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bin.read(buf)) != -1) {
                bout.write(buf, 0, len);
            }
            return bout.toByteArray();
        }catch(Exception e) {
            log.error("get download file error:{}", e.getMessage(), e);
        }
        return null;
    }
}
