package com.oc.hawk.traffic.port.driving.facade.rest;

/**
 * @author kangta123
 */

import com.oc.hawk.common.utils.WebUtils;
import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final EntryPointUseCase entryPointUseCase;

    /**
     * 文件下载功能
     */
    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam(required = false) String fileName) {
        byte[] fileBytes = entryPointUseCase.getFile(fileName);
        return WebUtils.getDownloadFileHttpResponse(fileBytes, fileName);
    }
}
