package io.github.pascalgrimaud.client.config;

import org.springframework.context.annotation.Bean;

/**
 * Created by pgrimaud on 03/12/16.
 */
public class FeignConfiguration {

    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
