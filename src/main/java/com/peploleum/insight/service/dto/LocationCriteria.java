package com.peploleum.insight.service.dto;

import java.io.Serializable;
import com.peploleum.insight.domain.enumeration.LocationType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Location entity. This class is used in LocationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /locations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable {
    /**
     * Class for filtering LocationType
     */
    public static class LocationTypeFilter extends Filter<LocationType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter locationName;

    private LocationTypeFilter locationType;

    private StringFilter locationCoordinates;

    private LongFilter biographicsId;

    private LongFilter eventId;

    private LongFilter equipmentId;

    private LongFilter organisationId;

    public LocationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLocationName() {
        return locationName;
    }

    public void setLocationName(StringFilter locationName) {
        this.locationName = locationName;
    }

    public LocationTypeFilter getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationTypeFilter locationType) {
        this.locationType = locationType;
    }

    public StringFilter getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(StringFilter locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    public LongFilter getBiographicsId() {
        return biographicsId;
    }

    public void setBiographicsId(LongFilter biographicsId) {
        this.biographicsId = biographicsId;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(LongFilter equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LongFilter getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(LongFilter organisationId) {
        this.organisationId = organisationId;
    }

    @Override
    public String toString() {
        return "LocationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (locationName != null ? "locationName=" + locationName + ", " : "") +
                (locationType != null ? "locationType=" + locationType + ", " : "") +
                (locationCoordinates != null ? "locationCoordinates=" + locationCoordinates + ", " : "") +
                (biographicsId != null ? "biographicsId=" + biographicsId + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
                (organisationId != null ? "organisationId=" + organisationId + ", " : "") +
            "}";
    }

}
