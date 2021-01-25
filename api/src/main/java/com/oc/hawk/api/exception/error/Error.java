package com.oc.hawk.api.exception.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class Error {

    private String code;

    private String message;

    private String requestUri;

    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    @JsonCreator
    public Error(@JsonProperty("code") String code,
                 @JsonProperty("requestUri") String requestUri,
                 @JsonProperty(value = "message", defaultValue = "") String message) {
        this.code = code;
        this.requestUri = requestUri;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestUri() {
        return requestUri;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", requestUri='" + requestUri + '\'' +
                '}';
    }
}
