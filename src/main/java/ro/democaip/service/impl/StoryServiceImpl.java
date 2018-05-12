package ro.democaip.service.impl;

import ro.democaip.service.StoryService;
import ro.democaip.domain.Story;
import ro.democaip.repository.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Story.
 */
@Service
@Transactional
public class StoryServiceImpl implements StoryService {

    private final Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);

    private final StoryRepository storyRepository;

    public StoryServiceImpl(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    /**
     * Save a story.
     *
     * @param story the entity to save
     * @return the persisted entity
     */
    @Override
    public Story save(Story story) {
        log.debug("Request to save Story : {}", story);
        return storyRepository.save(story);
    }

    /**
     * Get all the stories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Story> findAll() {
        log.debug("Request to get all Stories");
        return storyRepository.findAll();
    }

    /**
     * Get one story by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Story findOne(Long id) {
        log.debug("Request to get Story : {}", id);
        return storyRepository.findOne(id);
    }

    /**
     * Delete the story by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Story : {}", id);
        storyRepository.delete(id);
    }
}
