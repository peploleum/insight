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
 * Criteria class for the Report entity. This class is used in ReportResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reports?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter nom;

    private StringFilter type;

    private StringFilter libelle;

    private StringFilter objetsReferences;

    private ZonedDateTimeFilter datePublication;

    private LongFilter linkOfId;

    public ReportCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public StringFilter getObjetsReferences() {
        return objetsReferences;
    }

    public void setObjetsReferences(StringFilter objetsReferences) {
        this.objetsReferences = objetsReferences;
    }

    public ZonedDateTimeFilter getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(ZonedDateTimeFilter datePublication) {
        this.datePublication = datePublication;
    }

    public LongFilter getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(LongFilter linkOfId) {
        this.linkOfId = linkOfId;
    }

    @Override
    public String toString() {
        return "ReportCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (objetsReferences != null ? "objetsReferences=" + objetsReferences + ", " : "") +
                (datePublication != null ? "datePublication=" + datePublication + ", " : "") +
                (linkOfId != null ? "linkOfId=" + linkOfId + ", " : "") +
            "}";
    }

}
