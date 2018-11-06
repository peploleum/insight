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
 * Criteria class for the ThreatActor entity. This class is used in ThreatActorResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /threat-actors?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ThreatActorCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter nom;

    private StringFilter type;

    private StringFilter libelle;

    private StringFilter specification;

    private StringFilter role;

    private LongFilter isTargetsThreatActorToVulnerabilityId;

    private LongFilter isUsesThreatActorToToolId;

    private LongFilter isUsesThreatActorToMalwareId;

    private LongFilter linkOfId;

    public ThreatActorCriteria() {
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

    public StringFilter getSpecification() {
        return specification;
    }

    public void setSpecification(StringFilter specification) {
        this.specification = specification;
    }

    public StringFilter getRole() {
        return role;
    }

    public void setRole(StringFilter role) {
        this.role = role;
    }

    public LongFilter getIsTargetsThreatActorToVulnerabilityId() {
        return isTargetsThreatActorToVulnerabilityId;
    }

    public void setIsTargetsThreatActorToVulnerabilityId(LongFilter isTargetsThreatActorToVulnerabilityId) {
        this.isTargetsThreatActorToVulnerabilityId = isTargetsThreatActorToVulnerabilityId;
    }

    public LongFilter getIsUsesThreatActorToToolId() {
        return isUsesThreatActorToToolId;
    }

    public void setIsUsesThreatActorToToolId(LongFilter isUsesThreatActorToToolId) {
        this.isUsesThreatActorToToolId = isUsesThreatActorToToolId;
    }

    public LongFilter getIsUsesThreatActorToMalwareId() {
        return isUsesThreatActorToMalwareId;
    }

    public void setIsUsesThreatActorToMalwareId(LongFilter isUsesThreatActorToMalwareId) {
        this.isUsesThreatActorToMalwareId = isUsesThreatActorToMalwareId;
    }

    public LongFilter getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(LongFilter linkOfId) {
        this.linkOfId = linkOfId;
    }

    @Override
    public String toString() {
        return "ThreatActorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (specification != null ? "specification=" + specification + ", " : "") +
                (role != null ? "role=" + role + ", " : "") +
                (isTargetsThreatActorToVulnerabilityId != null ? "isTargetsThreatActorToVulnerabilityId=" + isTargetsThreatActorToVulnerabilityId + ", " : "") +
                (isUsesThreatActorToToolId != null ? "isUsesThreatActorToToolId=" + isUsesThreatActorToToolId + ", " : "") +
                (isUsesThreatActorToMalwareId != null ? "isUsesThreatActorToMalwareId=" + isUsesThreatActorToMalwareId + ", " : "") +
                (linkOfId != null ? "linkOfId=" + linkOfId + ", " : "") +
            "}";
    }

}
