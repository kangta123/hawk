package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

/**
 * @DomainValueObject
 * @Getter
 * @Builder public class HttpRequestMethod {
 * <p>
 * private String requestMethod;
 * <p>
 * public HttpRequestMethod(String requestMethod) {
 * this.requestMethod = requestMethod;
 * }
 * <p>
 * public boolean isPost() {
 * if("POST".equalsIgnoreCase(this.requestMethod)) {
 * return true;
 * }
 * return false;
 * }
 * <p>
 * public boolean isGet() {
 * if("GET".equalsIgnoreCase(this.requestMethod)) {
 * return true;
 * }
 * return false;
 * }
 * <p>
 * public boolean isPut() {
 * if("GET".equalsIgnoreCase(this.requestMethod)) {
 * return true;
 * }
 * return false;
 * }
 * <p>
 * public boolean isDelete() {
 * if("DELETE".equalsIgnoreCase(this.requestMethod)) {
 * return true;
 * }
 * return false;
 * }
 * <p>
 * }
 */

public enum HttpRequestMethod {

    POST,
    GET,
    PUT,
    DELETE

}
