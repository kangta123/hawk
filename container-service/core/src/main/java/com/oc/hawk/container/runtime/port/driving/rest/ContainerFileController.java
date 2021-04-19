package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.common.utils.WebUtils;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class ContainerFileController {

    private final String contentType = "application/octet-stream";

    private final InstanceConfigUseCase configurationManager;

    @GetMapping("/nginx/config")
    public ResponseEntity<Resource> getNginxLocationConfig(@RequestParam("serviceName") String serviceName,
                                                           @RequestParam(value = "namespace", required = false) String namespace) {

        InstanceConfigDTO config = configurationManager.getConfiguration(namespace, serviceName);
        if (config != null) {
            String script = config.getNginxLocation();
            return textToFile(script);
        }

        return ResponseEntity.of(Optional.empty());
    }

    @GetMapping
    public ResponseEntity<Resource> getContainerPreStartHookShell(@RequestParam String serviceName,
                                                                  @RequestParam(required = false, defaultValue = "default") String namespace) {

        log.info("acquire container pre start hook shell with {} in {}", serviceName, namespace);
        if (StringUtils.isEmpty(serviceName)) {
            return ResponseEntity.of(Optional.empty());
        }
        InstanceConfigDTO config = configurationManager.getConfiguration(namespace, serviceName);

        if (config != null) {
            String script = config.getPreStart();
            if (script != null) {
                return WebUtils.getDownloadFileHttpResponse(script.getBytes(), "script.sh");
            }
        }
        return ResponseEntity.of(Optional.empty());
    }

    private ResponseEntity<Resource> textToFile(String text) {
        if (StringUtils.isNotEmpty(text)) {
            Resource resource = new ByteArrayResource(text.getBytes());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"config-file\"")
                    .body(resource);
        }
        return ResponseEntity.of(Optional.empty());
    }
}
