package io.github.pascalgrimaud.service;

import io.github.pascalgrimaud.domain.Avatar;
import io.github.pascalgrimaud.repository.AvatarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Avatar.
 */
@Service
@Transactional
public class AvatarService {

    private final Logger log = LoggerFactory.getLogger(AvatarService.class);
    
    @Inject
    private AvatarRepository avatarRepository;

    /**
     * Save a avatar.
     *
     * @param avatar the entity to save
     * @return the persisted entity
     */
    public Avatar save(Avatar avatar) {
        log.debug("Request to save Avatar : {}", avatar);
        Avatar result = avatarRepository.save(avatar);
        return result;
    }

    /**
     *  Get all the avatars.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Avatar> findAll() {
        log.debug("Request to get all Avatars");
        List<Avatar> result = avatarRepository.findAll();

        return result;
    }

    /**
     *  Get one avatar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Avatar findOne(Long id) {
        log.debug("Request to get Avatar : {}", id);
        Avatar avatar = avatarRepository.findOne(id);
        return avatar;
    }

    /**
     *  Delete the  avatar by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Avatar : {}", id);
        avatarRepository.delete(id);
    }
}
