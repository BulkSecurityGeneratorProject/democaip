package ro.democaip.web.rest;

import ro.democaip.DemocaipApp;

import ro.democaip.domain.Epic;
import ro.democaip.repository.EpicRepository;
import ro.democaip.service.EpicService;
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

import ro.democaip.domain.enumeration.EpicSize;
/**
 * Test class for the EpicResource REST controller.
 *
 * @see EpicResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemocaipApp.class)
public class EpicResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final EpicSize DEFAULT_SIZE = EpicSize.XXS;
    private static final EpicSize UPDATED_SIZE = EpicSize.XS;

    @Autowired
    private EpicRepository epicRepository;

    @Autowired
    private EpicService epicService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEpicMockMvc;

    private Epic epic;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EpicResource epicResource = new EpicResource(epicService);
        this.restEpicMockMvc = MockMvcBuilders.standaloneSetup(epicResource)
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
    public static Epic createEntity(EntityManager em) {
        Epic epic = new Epic()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creation(DEFAULT_CREATION)
            .size(DEFAULT_SIZE);
        return epic;
    }

    @Before
    public void initTest() {
        epic = createEntity(em);
    }

    @Test
    @Transactional
    public void createEpic() throws Exception {
        int databaseSizeBeforeCreate = epicRepository.findAll().size();

        // Create the Epic
        restEpicMockMvc.perform(post("/api/epics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(epic)))
            .andExpect(status().isCreated());

        // Validate the Epic in the database
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeCreate + 1);
        Epic testEpic = epicList.get(epicList.size() - 1);
        assertThat(testEpic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEpic.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEpic.getCreation()).isEqualTo(DEFAULT_CREATION);
        assertThat(testEpic.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    public void createEpicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = epicRepository.findAll().size();

        // Create the Epic with an existing ID
        epic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpicMockMvc.perform(post("/api/epics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(epic)))
            .andExpect(status().isBadRequest());

        // Validate the Epic in the database
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEpics() throws Exception {
        // Initialize the database
        epicRepository.saveAndFlush(epic);

        // Get all the epicList
        restEpicMockMvc.perform(get("/api/epics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creation").value(hasItem(DEFAULT_CREATION.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())));
    }

    @Test
    @Transactional
    public void getEpic() throws Exception {
        // Initialize the database
        epicRepository.saveAndFlush(epic);

        // Get the epic
        restEpicMockMvc.perform(get("/api/epics/{id}", epic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(epic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creation").value(DEFAULT_CREATION.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEpic() throws Exception {
        // Get the epic
        restEpicMockMvc.perform(get("/api/epics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEpic() throws Exception {
        // Initialize the database
        epicService.save(epic);

        int databaseSizeBeforeUpdate = epicRepository.findAll().size();

        // Update the epic
        Epic updatedEpic = epicRepository.findOne(epic.getId());
        // Disconnect from session so that the updates on updatedEpic are not directly saved in db
        em.detach(updatedEpic);
        updatedEpic
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creation(UPDATED_CREATION)
            .size(UPDATED_SIZE);

        restEpicMockMvc.perform(put("/api/epics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEpic)))
            .andExpect(status().isOk());

        // Validate the Epic in the database
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeUpdate);
        Epic testEpic = epicList.get(epicList.size() - 1);
        assertThat(testEpic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEpic.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEpic.getCreation()).isEqualTo(UPDATED_CREATION);
        assertThat(testEpic.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingEpic() throws Exception {
        int databaseSizeBeforeUpdate = epicRepository.findAll().size();

        // Create the Epic

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEpicMockMvc.perform(put("/api/epics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(epic)))
            .andExpect(status().isCreated());

        // Validate the Epic in the database
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEpic() throws Exception {
        // Initialize the database
        epicService.save(epic);

        int databaseSizeBeforeDelete = epicRepository.findAll().size();

        // Get the epic
        restEpicMockMvc.perform(delete("/api/epics/{id}", epic.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Epic> epicList = epicRepository.findAll();
        assertThat(epicList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Epic.class);
        Epic epic1 = new Epic();
        epic1.setId(1L);
        Epic epic2 = new Epic();
        epic2.setId(epic1.getId());
        assertThat(epic1).isEqualTo(epic2);
        epic2.setId(2L);
        assertThat(epic1).isNotEqualTo(epic2);
        epic1.setId(null);
        assertThat(epic1).isNotEqualTo(epic2);
    }
}
