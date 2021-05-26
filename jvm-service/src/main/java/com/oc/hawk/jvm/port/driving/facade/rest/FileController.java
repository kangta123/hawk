package com.oc.hawk.jvm.port.driving.facade.rest;

/**
 * @author kangta123
 */

import com.oc.hawk.common.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final String HAWK_JAVA_AGENT_FILE_NAME = "hawk-agent.jar";

    /**
     * 文件下载功能
     */
    @GetMapping("/agent")
    public ResponseEntity<Resource> downloadJavaAgentFile() {
        byte[] fileBytes = getAgentFile();
        return WebUtils.getDownloadFileHttpResponse(fileBytes, HAWK_JAVA_AGENT_FILE_NAME);
    }


    private byte[] getAgentFile() {
        Resource resource = new ClassPathResource("files/" + HAWK_JAVA_AGENT_FILE_NAME);
        try {
            return IOUtils.toByteArray(resource.getInputStream());
        } catch (Exception e) {
            log.error("get download file error:{}", e.getMessage(), e);
        }
        return null;
    }


}
