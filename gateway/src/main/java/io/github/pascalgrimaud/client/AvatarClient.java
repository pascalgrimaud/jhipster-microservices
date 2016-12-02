package io.github.pascalgrimaud.client;

import io.github.pascalgrimaud.client.config.FeignConfiguration;
import io.github.pascalgrimaud.client.vm.AvatarVM;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by pgrimaud on 02/12/16.
 */
@FeignClient(name = "micro", fallbackFactory = AvatarFallback.class, configuration = FeignConfiguration.class)
public interface AvatarClient {

    @RequestMapping(value = "/api/avatars",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AvatarVM> create(@RequestHeader("Authorization") String authorizationToken, AvatarVM avatar);

    @RequestMapping(value = "/api/avatars",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AvatarVM> update(@RequestHeader("Authorization") String authorizationToken, AvatarVM avatar);

    @RequestMapping(value = "/api/avatars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    List<AvatarVM> findAll(@RequestHeader("Authorization") String authorizationToken);

    @RequestMapping(value = "/api/avatars/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AvatarVM> findOne(@RequestHeader("Authorization") String authorizationToken, @PathVariable("id") Long id);

    @RequestMapping(value = "/api/avatars/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(@RequestHeader("Authorization") String authorizationToken, @PathVariable("id") Long id);
}
