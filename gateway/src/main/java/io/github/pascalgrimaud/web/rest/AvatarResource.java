package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.client.AvatarClient;
import io.github.pascalgrimaud.client.vm.AvatarVM;
import io.github.pascalgrimaud.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by pgrimaud on 02/12/16.
 */
@RestController
@RequestMapping("api/avatars")
public class AvatarResource {

    private final Logger log = LoggerFactory.getLogger(AvatarResource.class);

    @Inject
    private AvatarClient avatarClient;

    /**
     * GET  /avatars : get all the avatars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of avatars in body
     */
    @GetMapping
    @Timed
    public List<AvatarVM> getAllAvatars(HttpServletRequest request) {
        log.debug("REST request to get all Avatars");
        String authorizationToken = request.getHeader("Authorization");
        return avatarClient.findAll(authorizationToken);
    }

    /**
     * GET  /avatars/:id : get the "id" avatar.
     *
     * @param id the id of the avatar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the avatar, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Timed
    public ResponseEntity<AvatarVM> getAvatar(HttpServletRequest request, @PathVariable Long id) {
        log.debug("REST request to get Avatar : {}", id);
        String authorizationToken = request.getHeader("Authorization");
        return avatarClient.findOne(authorizationToken, id);
    }

    /**
     * POST  /avatars : Create a new avatar.
     *
     * @param avatar the avatar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new avatar, or with status 400 (Bad Request) if the avatar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping
    @Timed
    public ResponseEntity<AvatarVM> createAvatar(HttpServletRequest request, @RequestBody AvatarVM avatar) throws URISyntaxException {
        log.debug("REST request to save Avatar : {}", avatar);
        String authorizationToken = request.getHeader("Authorization");
        if (avatar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("avatar", "idexists", "A new avatar cannot already have an ID")).body(null);
        }
        return avatarClient.create(authorizationToken, avatar);
    }

    /**
     * PUT  /avatars : Updates an existing avatar.
     *
     * @param avatar the avatar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated avatar,
     * or with status 400 (Bad Request) if the avatar is not valid,
     * or with status 500 (Internal Server Error) if the avatar couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    @Timed
    public ResponseEntity<AvatarVM> updateAvatar(HttpServletRequest request, @RequestBody AvatarVM avatar) throws URISyntaxException {
        log.debug("REST request to update Avatar : {}", avatar);
        String authorizationToken = request.getHeader("Authorization");
        if (avatar.getId() == null) {
            return createAvatar(request, avatar);
        }
        return avatarClient.update(authorizationToken, avatar);
    }

    /**
     * DELETE  /avatars/:id : delete the "id" avatar.
     *
     * @param id the id of the avatar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvatar(HttpServletRequest request, @PathVariable Long id) {
        log.debug("REST request to delete Avatar : {}", id);
        String authorizationToken = request.getHeader("Authorization");
        avatarClient.delete(authorizationToken, id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("avatar", id.toString())).build();
    }
}
