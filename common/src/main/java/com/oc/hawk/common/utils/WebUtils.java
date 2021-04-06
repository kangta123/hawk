package com.oc.hawk.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class WebUtils {
    
    private static final String contentType = "application/octet-stream";
    
    public static Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
            .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
            .map(ServletRequestAttributes::getRequest);
    }

    public static String getClientIp() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "";
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            if (request != null) {
                String remoteAddress = request.getHeader("X-FORWARDED-FOR");
                if (StringUtils.isEmpty(remoteAddress)) {
                    remoteAddress = request.getRemoteAddr();
                }
                return remoteAddress;
            } else {
                log.error("Null current request.");
            }
            return "";
        } catch (IllegalStateException e) {
            return "";
        }


    }
    
    public static ResponseEntity<Resource> getDownloadFileHttpResponse(byte[] fileBytes,String fileName) {
        if (Objects.nonNull(fileBytes) && fileBytes.length>0) {
            Resource resource = new ByteArrayResource(fileBytes);
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName)
                .body(resource);
        }
        return ResponseEntity.of(Optional.empty());
    }

}
