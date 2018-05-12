package ro.democaip.service;

import ro.democaip.domain.Epic;
import java.util.List;

/**
 * Service Interface for managing Epic.
 */
public interface EpicService {

    /**
     * Save a epic.
     *
     * @param epic the entity to save
     * @return the persisted entity
     */
    Epic save(Epic epic);

    /**
     * Get all the epics.
     *
     * @return the list of entities
     */
    List<Epic> findAll();

    /**
     * Get the "id" epic.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Epic findOne(Long id);

    /**
     * Delete the "id" epic.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
