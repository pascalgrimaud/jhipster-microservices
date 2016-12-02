package io.github.pascalgrimaud.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.pascalgrimaud.domain.Avatar;
import io.github.pascalgrimaud.service.AvatarService;
import io.github.pascalgrimaud.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Avatar.
 */
@RestController
@RequestMapping("/api")
public class AvatarResource {

    private final Logger log = LoggerFactory.getLogger(AvatarResource.class);
        
    @Inject
    private AvatarService avatarService;

    /**
     * POST  /avatars : Create a new avatar.
     *
     * @param avatar the avatar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new avatar, or with status 400 (Bad Request) if the avatar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/avatars")
    @Timed
    public ResponseEntity<Avatar> createAvatar(@RequestBody Avatar avatar) throws URISyntaxException {
        log.debug("REST request to save Avatar : {}", avatar);
        if (avatar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("avatar", "idexists", "A new avatar cannot already have an ID")).body(null);
        }
        Avatar result = avatarService.save(avatar);
        return ResponseEntity.created(new URI("/api/avatars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("avatar", result.getId().toString()))
            .body(result);
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
    @PutMapping("/avatars")
    @Timed
    public ResponseEntity<Avatar> updateAvatar(@RequestBody Avatar avatar) throws URISyntaxException {
        log.debug("REST request to update Avatar : {}", avatar);
        if (avatar.getId() == null) {
            return createAvatar(avatar);
        }
        Avatar result = avatarService.save(avatar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("avatar", avatar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /avatars : get all the avatars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of avatars in body
     */
    @GetMapping("/avatars")
    @Timed
    public List<Avatar> getAllAvatars() {
        log.debug("REST request to get all Avatars");
        return avatarService.findAll();
    }

    /**
     * GET  /avatars/:id : get the "id" avatar.
     *
     * @param id the id of the avatar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the avatar, or with status 404 (Not Found)
     */
    @GetMapping("/avatars/{id}")
    @Timed
    public ResponseEntity<Avatar> getAvatar(@PathVariable Long id) {
        log.debug("REST request to get Avatar : {}", id);
        Avatar avatar = avatarService.findOne(id);
        return Optional.ofNullable(avatar)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /avatars/:id : delete the "id" avatar.
     *
     * @param id the id of the avatar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/avatars/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        log.debug("REST request to delete Avatar : {}", id);
        avatarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("avatar", id.toString())).build();
    }

}
