package io.github.pascalgrimaud.client.exception;

import feign.Response;

import java.util.Collection;
import java.util.Map;

/**
 * Created by pgrimaud on 03/12/16.
 */
public class BusinessException extends RuntimeException {

    private int code;

    private Map<String, Collection<String>> headers;

    private String reason;

    private Response response;

    public BusinessException(int code, Map<String, Collection<String>> headers, String reason, Response response) {
        this.code = code;
        this.headers = headers;
        this.reason = reason;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Collection<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Collection<String>> headers) {
        this.headers = headers;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
            "code=" + code +
            ", headers=" + headers +
            ", reason='" + reason + '\'' +
            '}';
    }
}
