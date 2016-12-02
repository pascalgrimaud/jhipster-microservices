package io.github.pascalgrimaud.client;

import feign.hystrix.FallbackFactory;
import io.github.pascalgrimaud.client.exception.BusinessException;
import io.github.pascalgrimaud.client.vm.AvatarVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by pgrimaud on 03/12/16.
 */
@Component
public class AvatarFallback implements FallbackFactory<AvatarClient> {
    private final Logger log = LoggerFactory.getLogger(AvatarFallback.class);

    @Override
    public AvatarClient create(Throwable cause) {

        return new AvatarClient() {

            @Override
            public ResponseEntity<AvatarVM> create(String authorizationToken, AvatarVM avatar) {
                log.debug("FALLBACK request to save Avatar");
                return null;
            }

            @Override
            public ResponseEntity<AvatarVM> update(String authorizationToken, AvatarVM avatar) {
                log.debug("FALLBACK request to update Avatar");
                return null;
            }

            @Override
            public List<AvatarVM> findAll(String authorizationToken) {
                log.debug("FALLBACK request to get all Avatars");
                return null;
            }

            @Override
            public ResponseEntity<AvatarVM> findOne(String authorizationToken, Long id) {
                if (cause instanceof BusinessException) {
                    return new ResponseEntity<>(new AvatarVM(), HttpStatus.valueOf(((BusinessException) cause).getCode()));
                }
                return null;
            }

            @Override
            public ResponseEntity<Void> delete(String authorizationToken, Long id) {
                log.debug("FALLBACK request to delete Avatar");
                return null;
            }
        };
    }
}
