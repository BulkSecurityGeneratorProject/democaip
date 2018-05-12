package ro.democaip.service;

import ro.democaip.domain.Story;
import java.util.List;

/**
 * Service Interface for managing Story.
 */
public interface StoryService {

    /**
     * Save a story.
     *
     * @param story the entity to save
     * @return the persisted entity
     */
    Story save(Story story);

    /**
     * Get all the stories.
     *
     * @return the list of entities
     */
    List<Story> findAll();

    /**
     * Get the "id" story.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Story findOne(Long id);

    /**
     * Delete the "id" story.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
