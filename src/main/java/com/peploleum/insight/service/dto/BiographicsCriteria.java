package com.peploleum.insight.service.dto;

import java.io.Serializable;
import com.peploleum.insight.domain.enumeration.Gender;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Biographics entity. This class is used in BiographicsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /biographics?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BiographicsCriteria implements Serializable {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter biographicsFirstname;

    private StringFilter biographicsName;

    private IntegerFilter biographicsAge;

    private GenderFilter biographicsGender;

    private StringFilter biographicsCoordinates;

    private LongFilter eventId;

    private LongFilter equipmentId;

    private LongFilter locationId;

    private LongFilter organisationId;

    public BiographicsCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBiographicsFirstname() {
        return biographicsFirstname;
    }

    public void setBiographicsFirstname(StringFilter biographicsFirstname) {
        this.biographicsFirstname = biographicsFirstname;
    }

    public StringFilter getBiographicsName() {
        return biographicsName;
    }

    public void setBiographicsName(StringFilter biographicsName) {
        this.biographicsName = biographicsName;
    }

    public IntegerFilter getBiographicsAge() {
        return biographicsAge;
    }

    public void setBiographicsAge(IntegerFilter biographicsAge) {
        this.biographicsAge = biographicsAge;
    }

    public GenderFilter getBiographicsGender() {
        return biographicsGender;
    }

    public void setBiographicsGender(GenderFilter biographicsGender) {
        this.biographicsGender = biographicsGender;
    }

    public StringFilter getBiographicsCoordinates() {
        return biographicsCoordinates;
    }

    public void setBiographicsCoordinates(StringFilter biographicsCoordinates) {
        this.biographicsCoordinates = biographicsCoordinates;
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

    @Override
    public String toString() {
        return "BiographicsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (biographicsFirstname != null ? "biographicsFirstname=" + biographicsFirstname + ", " : "") +
                (biographicsName != null ? "biographicsName=" + biographicsName + ", " : "") +
                (biographicsAge != null ? "biographicsAge=" + biographicsAge + ", " : "") +
                (biographicsGender != null ? "biographicsGender=" + biographicsGender + ", " : "") +
                (biographicsCoordinates != null ? "biographicsCoordinates=" + biographicsCoordinates + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (organisationId != null ? "organisationId=" + organisationId + ", " : "") +
            "}";
    }

}
