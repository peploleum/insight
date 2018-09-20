package com.peploleum.insight.service.dto;

import java.io.Serializable;
import com.peploleum.insight.domain.enumeration.Size;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Organisation entity. This class is used in OrganisationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /organisations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganisationCriteria implements Serializable {
    /**
     * Class for filtering Size
     */
    public static class SizeFilter extends Filter<Size> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter organisationName;

    private SizeFilter organisationSize;

    private StringFilter organisationCoordinates;

    private LongFilter locationId;

    private LongFilter biographicsId;

    private LongFilter eventId;

    private LongFilter equipmentId;

    public OrganisationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(StringFilter organisationName) {
        this.organisationName = organisationName;
    }

    public SizeFilter getOrganisationSize() {
        return organisationSize;
    }

    public void setOrganisationSize(SizeFilter organisationSize) {
        this.organisationSize = organisationSize;
    }

    public StringFilter getOrganisationCoordinates() {
        return organisationCoordinates;
    }

    public void setOrganisationCoordinates(StringFilter organisationCoordinates) {
        this.organisationCoordinates = organisationCoordinates;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
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

    @Override
    public String toString() {
        return "OrganisationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (organisationName != null ? "organisationName=" + organisationName + ", " : "") +
                (organisationSize != null ? "organisationSize=" + organisationSize + ", " : "") +
                (organisationCoordinates != null ? "organisationCoordinates=" + organisationCoordinates + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (biographicsId != null ? "biographicsId=" + biographicsId + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
            "}";
    }

}
