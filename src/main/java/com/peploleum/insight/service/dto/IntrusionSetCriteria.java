package com.peploleum.insight.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the IntrusionSet entity. This class is used in IntrusionSetResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /intrusion-sets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IntrusionSetCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter description;

    private StringFilter nom;

    private StringFilter type;

    private StringFilter objectif;

    private StringFilter niveauRessource;

    private LongFilter isUsesIntrusionSetToToolId;

    private LongFilter isTargetsIntrusionSetToActorId;

    private LongFilter linkOfId;

    public IntrusionSetCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public StringFilter getObjectif() {
        return objectif;
    }

    public void setObjectif(StringFilter objectif) {
        this.objectif = objectif;
    }

    public StringFilter getNiveauRessource() {
        return niveauRessource;
    }

    public void setNiveauRessource(StringFilter niveauRessource) {
        this.niveauRessource = niveauRessource;
    }

    public LongFilter getIsUsesIntrusionSetToToolId() {
        return isUsesIntrusionSetToToolId;
    }

    public void setIsUsesIntrusionSetToToolId(LongFilter isUsesIntrusionSetToToolId) {
        this.isUsesIntrusionSetToToolId = isUsesIntrusionSetToToolId;
    }

    public LongFilter getIsTargetsIntrusionSetToActorId() {
        return isTargetsIntrusionSetToActorId;
    }

    public void setIsTargetsIntrusionSetToActorId(LongFilter isTargetsIntrusionSetToActorId) {
        this.isTargetsIntrusionSetToActorId = isTargetsIntrusionSetToActorId;
    }

    public LongFilter getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(LongFilter linkOfId) {
        this.linkOfId = linkOfId;
    }

    @Override
    public String toString() {
        return "IntrusionSetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (objectif != null ? "objectif=" + objectif + ", " : "") +
                (niveauRessource != null ? "niveauRessource=" + niveauRessource + ", " : "") +
                (isUsesIntrusionSetToToolId != null ? "isUsesIntrusionSetToToolId=" + isUsesIntrusionSetToToolId + ", " : "") +
                (isTargetsIntrusionSetToActorId != null ? "isTargetsIntrusionSetToActorId=" + isTargetsIntrusionSetToActorId + ", " : "") +
                (linkOfId != null ? "linkOfId=" + linkOfId + ", " : "") +
            "}";
    }

}
