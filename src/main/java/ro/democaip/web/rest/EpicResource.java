package ro.democaip.web.rest;

import com.codahale.metrics.annotation.Timed;
import ro.democaip.domain.Epic;
import ro.democaip.service.EpicService;
import ro.democaip.service.UserService;
import ro.democaip.web.rest.errors.BadRequestAlertException;
import ro.democaip.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Epic.
 */
@RestController
@RequestMapping("/api")
public class EpicResource {

    private final Logger log = LoggerFactory.getLogger(EpicResource.class);

    private static final String ENTITY_NAME = "epic";

    private final EpicService epicService;
    private final UserService userService;

    public EpicResource(EpicService epicService, UserService userService) {
        this.epicService = epicService;
        this.userService = userService;
    }

    /**
     * POST  /epics : Create a new epic.
     *
     * @param epic the epic to create
     * @return the ResponseEntity with status 201 (Created) and with body the new epic, or with status 400 (Bad Request) if the epic has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/epics")
    @Timed
    public ResponseEntity<Epic> createEpic(@RequestBody Epic epic) throws URISyntaxException {
        log.debug("REST request to save Epic : {}", epic);
        if (epic.getId() != null) {
            throw new BadRequestAlertException("A new epic cannot already have an ID", ENTITY_NAME, "idexists");
        }

        epic.setCreation(LocalDate.now());
        userService.getUserWithAuthorities().ifPresent(epic::setOwner);

        Epic result = epicService.save(epic);
        return ResponseEntity.created(new URI("/api/epics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /epics : Updates an existing epic.
     *
     * @param epic the epic to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated epic,
     * or with status 400 (Bad Request) if the epic is not valid,
     * or with status 500 (Internal Server Error) if the epic couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/epics")
    @Timed
    public ResponseEntity<Epic> updateEpic(@RequestBody Epic epic) throws URISyntaxException {
        log.debug("REST request to update Epic : {}", epic);
        if (epic.getId() == null) {
            return createEpic(epic);
        }
        Epic result = epicService.save(epic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, epic.getId().toString()))
            .body(result);
    }

    /**
     * GET  /epics : get all the epics.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of epics in body
     */
    @GetMapping("/epics")
    @Timed
    public List<Epic> getAllEpics() {
        log.debug("REST request to get all Epics");
        return epicService.findAll();
        }

    /**
     * GET  /epics/:id : get the "id" epic.
     *
     * @param id the id of the epic to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the epic, or with status 404 (Not Found)
     */
    @GetMapping("/epics/{id}")
    @Timed
    public ResponseEntity<Epic> getEpic(@PathVariable Long id) {
        log.debug("REST request to get Epic : {}", id);
        Epic epic = epicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(epic));
    }

    /**
     * DELETE  /epics/:id : delete the "id" epic.
     *
     * @param id the id of the epic to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/epics/{id}")
    @Timed
    public ResponseEntity<Void> deleteEpic(@PathVariable Long id) {
        log.debug("REST request to delete Epic : {}", id);
        epicService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
