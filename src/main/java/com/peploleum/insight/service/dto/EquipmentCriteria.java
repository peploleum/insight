package com.peploleum.insight.service.dto;

import java.io.Serializable;
import com.peploleum.insight.domain.enumeration.EquipmentType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Equipment entity. This class is used in EquipmentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /equipment?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipmentCriteria implements Serializable {
    /**
     * Class for filtering EquipmentType
     */
    public static class EquipmentTypeFilter extends Filter<EquipmentType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter equipmentName;

    private StringFilter equipmentDescription;

    private EquipmentTypeFilter equipmentType;

    private StringFilter equipmentCoordinates;

    private LongFilter locationId;

    private LongFilter organisationId;

    private LongFilter biographicsId;

    private LongFilter eventId;

    public EquipmentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(StringFilter equipmentName) {
        this.equipmentName = equipmentName;
    }

    public StringFilter getEquipmentDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(StringFilter equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }

    public EquipmentTypeFilter getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentTypeFilter equipmentType) {
        this.equipmentType = equipmentType;
    }

    public StringFilter getEquipmentCoordinates() {
        return equipmentCoordinates;
    }

    public void setEquipmentCoordinates(StringFilter equipmentCoordinates) {
        this.equipmentCoordinates = equipmentCoordinates;
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

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "EquipmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (equipmentName != null ? "equipmentName=" + equipmentName + ", " : "") +
                (equipmentDescription != null ? "equipmentDescription=" + equipmentDescription + ", " : "") +
                (equipmentType != null ? "equipmentType=" + equipmentType + ", " : "") +
                (equipmentCoordinates != null ? "equipmentCoordinates=" + equipmentCoordinates + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (organisationId != null ? "organisationId=" + organisationId + ", " : "") +
                (biographicsId != null ? "biographicsId=" + biographicsId + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
            "}";
    }

}
