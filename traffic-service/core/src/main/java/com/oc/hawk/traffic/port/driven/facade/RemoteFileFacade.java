package com.oc.hawk.traffic.port.driven.facade;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oc.hawk.traffic.entrypoint.domain.facade.FileFacade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemoteFileFacade implements FileFacade{@Override
    
    public byte[] getDownloanFile() {
        Resource resource = new ClassPathResource("files/agent.wasm");
        try(BufferedInputStream bin = new BufferedInputStream(new FileInputStream(resource.getFile()));
            ByteArrayOutputStream  bout = new ByteArrayOutputStream()
            ){
            int len = bin.read();
            byte[] buf = new byte[1024];
            while ((len = bin.read(buf)) != -1) {
                bout.write(buf, 0, len);
            }
            return bout.toByteArray();
        }catch(Exception e) {
            log.error("get downloan file error:{}",e.getMessage(),e);
        }
        return null;
    }
}
