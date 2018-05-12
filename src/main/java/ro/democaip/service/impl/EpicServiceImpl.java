package ro.democaip.service.impl;

import ro.democaip.service.EpicService;
import ro.democaip.domain.Epic;
import ro.democaip.repository.EpicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Epic.
 */
@Service
@Transactional
public class EpicServiceImpl implements EpicService {

    private final Logger log = LoggerFactory.getLogger(EpicServiceImpl.class);

    private final EpicRepository epicRepository;

    public EpicServiceImpl(EpicRepository epicRepository) {
        this.epicRepository = epicRepository;
    }

    /**
     * Save a epic.
     *
     * @param epic the entity to save
     * @return the persisted entity
     */
    @Override
    public Epic save(Epic epic) {
        log.debug("Request to save Epic : {}", epic);
        return epicRepository.save(epic);
    }

    /**
     * Get all the epics.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Epic> findAll() {
        log.debug("Request to get all Epics");
        return epicRepository.findAll();
    }

    /**
     * Get one epic by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Epic findOne(Long id) {
        log.debug("Request to get Epic : {}", id);
        return epicRepository.findOne(id);
    }

    /**
     * Delete the epic by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Epic : {}", id);
        epicRepository.delete(id);
    }
}
