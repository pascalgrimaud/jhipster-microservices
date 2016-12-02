package io.github.pascalgrimaud.web.rest;

import io.github.pascalgrimaud.MicroApp;

import io.github.pascalgrimaud.domain.Avatar;
import io.github.pascalgrimaud.repository.AvatarRepository;
import io.github.pascalgrimaud.service.AvatarService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AvatarResource REST controller.
 *
 * @see AvatarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicroApp.class)
public class AvatarResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private AvatarRepository avatarRepository;

    @Inject
    private AvatarService avatarService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAvatarMockMvc;

    private Avatar avatar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvatarResource avatarResource = new AvatarResource();
        ReflectionTestUtils.setField(avatarResource, "avatarService", avatarService);
        this.restAvatarMockMvc = MockMvcBuilders.standaloneSetup(avatarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avatar createEntity(EntityManager em) {
        Avatar avatar = new Avatar()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return avatar;
    }

    @Before
    public void initTest() {
        avatar = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvatar() throws Exception {
        int databaseSizeBeforeCreate = avatarRepository.findAll().size();

        // Create the Avatar

        restAvatarMockMvc.perform(post("/api/avatars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avatar)))
            .andExpect(status().isCreated());

        // Validate the Avatar in the database
        List<Avatar> avatars = avatarRepository.findAll();
        assertThat(avatars).hasSize(databaseSizeBeforeCreate + 1);
        Avatar testAvatar = avatars.get(avatars.size() - 1);
        assertThat(testAvatar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAvatar.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAvatars() throws Exception {
        // Initialize the database
        avatarRepository.saveAndFlush(avatar);

        // Get all the avatars
        restAvatarMockMvc.perform(get("/api/avatars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avatar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAvatar() throws Exception {
        // Initialize the database
        avatarRepository.saveAndFlush(avatar);

        // Get the avatar
        restAvatarMockMvc.perform(get("/api/avatars/{id}", avatar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(avatar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAvatar() throws Exception {
        // Get the avatar
        restAvatarMockMvc.perform(get("/api/avatars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvatar() throws Exception {
        // Initialize the database
        avatarService.save(avatar);

        int databaseSizeBeforeUpdate = avatarRepository.findAll().size();

        // Update the avatar
        Avatar updatedAvatar = avatarRepository.findOne(avatar.getId());
        updatedAvatar
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION);

        restAvatarMockMvc.perform(put("/api/avatars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvatar)))
            .andExpect(status().isOk());

        // Validate the Avatar in the database
        List<Avatar> avatars = avatarRepository.findAll();
        assertThat(avatars).hasSize(databaseSizeBeforeUpdate);
        Avatar testAvatar = avatars.get(avatars.size() - 1);
        assertThat(testAvatar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAvatar.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteAvatar() throws Exception {
        // Initialize the database
        avatarService.save(avatar);

        int databaseSizeBeforeDelete = avatarRepository.findAll().size();

        // Get the avatar
        restAvatarMockMvc.perform(delete("/api/avatars/{id}", avatar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Avatar> avatars = avatarRepository.findAll();
        assertThat(avatars).hasSize(databaseSizeBeforeDelete - 1);
    }
}
