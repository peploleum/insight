package com.peploleum.insight.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the ObservedData entity. This class is used in ObservedDataResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /observed-data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ObservedDataCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter type;

    private StringFilter objetsObserves;

    private ZonedDateTimeFilter dateDebut;

    private ZonedDateTimeFilter dateFin;

    private IntegerFilter nombreJours;

    private LongFilter linkOfId;

    public ObservedDataCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getObjetsObserves() {
        return objetsObserves;
    }

    public void setObjetsObserves(StringFilter objetsObserves) {
        this.objetsObserves = objetsObserves;
    }

    public ZonedDateTimeFilter getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(ZonedDateTimeFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTimeFilter getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTimeFilter dateFin) {
        this.dateFin = dateFin;
    }

    public IntegerFilter getNombreJours() {
        return nombreJours;
    }

    public void setNombreJours(IntegerFilter nombreJours) {
        this.nombreJours = nombreJours;
    }

    public LongFilter getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(LongFilter linkOfId) {
        this.linkOfId = linkOfId;
    }

    @Override
    public String toString() {
        return "ObservedDataCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (objetsObserves != null ? "objetsObserves=" + objetsObserves + ", " : "") +
                (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
                (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
                (nombreJours != null ? "nombreJours=" + nombreJours + ", " : "") +
                (linkOfId != null ? "linkOfId=" + linkOfId + ", " : "") +
            "}";
    }

}
