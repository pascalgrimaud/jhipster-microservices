package io.github.pascalgrimaud.client.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import io.github.pascalgrimaud.client.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ServiceUnavailableException;

/**
 * Created by pgrimaud on 03/12/16.
 */
public class FeignErrorDecoder implements ErrorDecoder {
    private final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() >= 400 && response.status() <= 499) {
            return new BusinessException(response.status(), response.headers(), response.reason(), response);

        } else if (response.status() >= 500 && response.status() <= 599) {
            return new ServiceUnavailableException();
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
