package ro.democaip.web.rest;

import ro.democaip.DemocaipApp;

import ro.democaip.domain.Story;
import ro.democaip.repository.StoryRepository;
import ro.democaip.service.StoryService;
import ro.democaip.service.UserService;
import ro.democaip.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static ro.democaip.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StoryResource REST controller.
 *
 * @see StoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemocaipApp.class)
public class StoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_STORY_POINT = 1;
    private static final Integer UPDATED_STORY_POINT = 2;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryService storyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStoryMockMvc;

    private Story story;
    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StoryResource storyResource = new StoryResource(storyService, userService);
        this.restStoryMockMvc = MockMvcBuilders.standaloneSetup(storyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Story createEntity(EntityManager em) {
        Story story = new Story()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creation(DEFAULT_CREATION)
            .storyPoint(DEFAULT_STORY_POINT);
        return story;
    }

    @Before
    public void initTest() {
        story = createEntity(em);
    }

    @Test
    @Transactional
    public void createStory() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // Create the Story
        restStoryMockMvc.perform(post("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isCreated());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate + 1);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStory.getCreation()).isEqualTo(DEFAULT_CREATION);
        assertThat(testStory.getStoryPoint()).isEqualTo(DEFAULT_STORY_POINT);
    }

    @Test
    @Transactional
    public void createStoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storyRepository.findAll().size();

        // Create the Story with an existing ID
        story.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoryMockMvc.perform(post("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isBadRequest());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStories() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get all the storyList
        restStoryMockMvc.perform(get("/api/stories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(story.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creation").value(hasItem(DEFAULT_CREATION.toString())))
            .andExpect(jsonPath("$.[*].storyPoint").value(hasItem(DEFAULT_STORY_POINT)));
    }

    @Test
    @Transactional
    public void getStory() throws Exception {
        // Initialize the database
        storyRepository.saveAndFlush(story);

        // Get the story
        restStoryMockMvc.perform(get("/api/stories/{id}", story.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(story.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creation").value(DEFAULT_CREATION.toString()))
            .andExpect(jsonPath("$.storyPoint").value(DEFAULT_STORY_POINT));
    }

    @Test
    @Transactional
    public void getNonExistingStory() throws Exception {
        // Get the story
        restStoryMockMvc.perform(get("/api/stories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStory() throws Exception {
        // Initialize the database
        storyService.save(story);

        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Update the story
        Story updatedStory = storyRepository.findOne(story.getId());
        // Disconnect from session so that the updates on updatedStory are not directly saved in db
        em.detach(updatedStory);
        updatedStory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creation(UPDATED_CREATION)
            .storyPoint(UPDATED_STORY_POINT);

        restStoryMockMvc.perform(put("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStory)))
            .andExpect(status().isOk());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate);
        Story testStory = storyList.get(storyList.size() - 1);
        assertThat(testStory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStory.getCreation()).isEqualTo(UPDATED_CREATION);
        assertThat(testStory.getStoryPoint()).isEqualTo(UPDATED_STORY_POINT);
    }

    @Test
    @Transactional
    public void updateNonExistingStory() throws Exception {
        int databaseSizeBeforeUpdate = storyRepository.findAll().size();

        // Create the Story

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStoryMockMvc.perform(put("/api/stories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(story)))
            .andExpect(status().isCreated());

        // Validate the Story in the database
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStory() throws Exception {
        // Initialize the database
        storyService.save(story);

        int databaseSizeBeforeDelete = storyRepository.findAll().size();

        // Get the story
        restStoryMockMvc.perform(delete("/api/stories/{id}", story.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Story> storyList = storyRepository.findAll();
        assertThat(storyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Story.class);
        Story story1 = new Story();
        story1.setId(1L);
        Story story2 = new Story();
        story2.setId(story1.getId());
        assertThat(story1).isEqualTo(story2);
        story2.setId(2L);
        assertThat(story1).isNotEqualTo(story2);
        story1.setId(null);
        assertThat(story1).isNotEqualTo(story2);
    }
}
