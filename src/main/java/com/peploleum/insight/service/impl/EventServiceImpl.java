package com.peploleum.insight.service.impl;

import com.peploleum.insight.domain.Event;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.repository.EventRepository;
import com.peploleum.insight.repository.search.EventSearchRepository;
import com.peploleum.insight.service.EventService;
import com.peploleum.insight.service.InsightGraphEntityService;
import com.peploleum.insight.service.dto.EventDTO;
import com.peploleum.insight.service.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Event.
 */
@Service
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventMapper eventMapper;

    private final EventRepository eventRepository;
    private final EventSearchRepository eventSearchRepository;
    private final InsightGraphEntityService insightGraphEntityRepository;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper,
                            EventSearchRepository eventSearchRepository, InsightGraphEntityService insightGraphEntityRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventSearchRepository = eventSearchRepository;
        this.insightGraphEntityRepository = insightGraphEntityRepository;
    }

    /**
     * Save a event.
     *
     * @param eventDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EventDTO save(EventDTO eventDTO) {
        log.debug("Request to save Event : {}", eventDTO);

        Event event = eventMapper.toEntity(eventDTO);
        event = eventRepository.save(event);
        eventSearchRepository.save(event);
        Long externalId = this.insightGraphEntityRepository.save(event.getEventName(), event.getId(), InsightEntityType.Event);
        event.setExternalId(String.valueOf(externalId));
        event = eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<EventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        return eventRepository.findAll(pageable)
            .map(eventMapper::toDto);
    }


    /**
     * Get one event by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<EventDTO> findOne(String id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id)
            .map(eventMapper::toDto);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
        eventSearchRepository.deleteById(id);
    }

    /**
     * Search for the event corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<EventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Events for query {}", query);
        return eventSearchRepository.search(queryStringQuery(query), pageable)
            .map(eventMapper::toDto);
    }
}
