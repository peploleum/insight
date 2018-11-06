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
 * Criteria class for the AttackPattern entity. This class is used in AttackPatternResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /attack-patterns?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttackPatternCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter description;

    private StringFilter nom;

    private StringFilter referenceExterne;

    private StringFilter tueurProcessus;

    private StringFilter type;

    private LongFilter usesAttackPatternToMalwareId;

    private LongFilter linkOfId;

    public AttackPatternCriteria() {
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

    public StringFilter getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(StringFilter referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public StringFilter getTueurProcessus() {
        return tueurProcessus;
    }

    public void setTueurProcessus(StringFilter tueurProcessus) {
        this.tueurProcessus = tueurProcessus;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getUsesAttackPatternToMalwareId() {
        return usesAttackPatternToMalwareId;
    }

    public void setUsesAttackPatternToMalwareId(LongFilter usesAttackPatternToMalwareId) {
        this.usesAttackPatternToMalwareId = usesAttackPatternToMalwareId;
    }

    public LongFilter getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(LongFilter linkOfId) {
        this.linkOfId = linkOfId;
    }

    @Override
    public String toString() {
        return "AttackPatternCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (referenceExterne != null ? "referenceExterne=" + referenceExterne + ", " : "") +
                (tueurProcessus != null ? "tueurProcessus=" + tueurProcessus + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (usesAttackPatternToMalwareId != null ? "usesAttackPatternToMalwareId=" + usesAttackPatternToMalwareId + ", " : "") +
                (linkOfId != null ? "linkOfId=" + linkOfId + ", " : "") +
            "}";
    }

}
