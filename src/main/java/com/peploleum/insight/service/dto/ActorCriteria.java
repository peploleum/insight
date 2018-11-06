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
 * Criteria class for the Actor entity. This class is used in ActorResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /actors?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActorCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter description;

    private StringFilter nom;

    private StringFilter type;

    private StringFilter libelle;

    private StringFilter classeIdentite;

    private LongFilter targetsActorToIntrusionSetId;

    private LongFilter targetsActorToMalwareId;

    private LongFilter linkOfId;

    public ActorCriteria() {
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

    public StringFilter getLibelle() {
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public StringFilter getClasseIdentite() {
        return classeIdentite;
    }

    public void setClasseIdentite(StringFilter classeIdentite) {
        this.classeIdentite = classeIdentite;
    }

    public LongFilter getTargetsActorToIntrusionSetId() {
        return targetsActorToIntrusionSetId;
    }

    public void setTargetsActorToIntrusionSetId(LongFilter targetsActorToIntrusionSetId) {
        this.targetsActorToIntrusionSetId = targetsActorToIntrusionSetId;
    }

    public LongFilter getTargetsActorToMalwareId() {
        return targetsActorToMalwareId;
    }

    public void setTargetsActorToMalwareId(LongFilter targetsActorToMalwareId) {
        this.targetsActorToMalwareId = targetsActorToMalwareId;
    }

    public LongFilter getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(LongFilter linkOfId) {
        this.linkOfId = linkOfId;
    }

    @Override
    public String toString() {
        return "ActorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (classeIdentite != null ? "classeIdentite=" + classeIdentite + ", " : "") +
                (targetsActorToIntrusionSetId != null ? "targetsActorToIntrusionSetId=" + targetsActorToIntrusionSetId + ", " : "") +
                (targetsActorToMalwareId != null ? "targetsActorToMalwareId=" + targetsActorToMalwareId + ", " : "") +
                (linkOfId != null ? "linkOfId=" + linkOfId + ", " : "") +
            "}";
    }

}
