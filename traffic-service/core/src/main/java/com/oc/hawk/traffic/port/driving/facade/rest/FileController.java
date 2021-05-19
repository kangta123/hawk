package com.oc.hawk.traffic.port.driving.facade.rest;

/**
 * @author kangta123
 */

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.common.utils.WebUtils;
import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import com.oc.hawk.traffic.application.entrypoint.JvmAgentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final EntryPointUseCase entryPointUseCase;
    private final JvmAgentUseCase jvmAgentUseCase;

    /**
     * 文件下载功能
     */
    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam(required = false) String fileName) {
        byte[] fileBytes = entryPointUseCase.getFile(fileName);
        return WebUtils.getDownloadFileHttpResponse(fileBytes, fileName);
    }

    @PostMapping("/hotswap")
    public BooleanWrapper importGroup(
            @RequestParam String instanceName,
            @RequestParam(defaultValue = "default", required = false) String namespace,
            @RequestParam("file") MultipartFile file) throws Exception {

//        File tempFile = File.createTempFile("hawk", "hotswap");
//        FileUtils.copyToFile(file.getInputStream(), tempFile);
        jvmAgentUseCase.hotswap(file.getInputStream(), instanceName, namespace);
        return BooleanWrapper.TRUE;
    }
}
