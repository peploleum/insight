package com.peploleum.insight.service.dto;

import java.io.Serializable;
import com.peploleum.insight.domain.enumeration.EventType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Event entity. This class is used in EventResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /events?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable {
    /**
     * Class for filtering EventType
     */
    public static class EventTypeFilter extends Filter<EventType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter eventName;

    private StringFilter eventDescription;

    private EventTypeFilter eventType;

    private StringFilter eventCoordinates;

    private LongFilter equipmentId;

    private LongFilter locationId;

    private LongFilter organisationId;

    private LongFilter biographicsId;

    public EventCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEventName() {
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public StringFilter getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(StringFilter eventDescription) {
        this.eventDescription = eventDescription;
    }

    public EventTypeFilter getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeFilter eventType) {
        this.eventType = eventType;
    }

    public StringFilter getEventCoordinates() {
        return eventCoordinates;
    }

    public void setEventCoordinates(StringFilter eventCoordinates) {
        this.eventCoordinates = eventCoordinates;
    }

    public LongFilter getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(LongFilter equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(LongFilter organisationId) {
        this.organisationId = organisationId;
    }

    public LongFilter getBiographicsId() {
        return biographicsId;
    }

    public void setBiographicsId(LongFilter biographicsId) {
        this.biographicsId = biographicsId;
    }

    @Override
    public String toString() {
        return "EventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (eventName != null ? "eventName=" + eventName + ", " : "") +
                (eventDescription != null ? "eventDescription=" + eventDescription + ", " : "") +
                (eventType != null ? "eventType=" + eventType + ", " : "") +
                (eventCoordinates != null ? "eventCoordinates=" + eventCoordinates + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (organisationId != null ? "organisationId=" + organisationId + ", " : "") +
                (biographicsId != null ? "biographicsId=" + biographicsId + ", " : "") +
            "}";
    }

}
