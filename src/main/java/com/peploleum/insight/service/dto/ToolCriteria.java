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
 * Criteria class for the Tool entity. This class is used in ToolResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tools?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ToolCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter nom;

    private StringFilter type;

    private StringFilter libelle;

    private StringFilter description;

    private StringFilter version;

    private LongFilter isTargetsToolToVulnerabilityId;

    private LongFilter usesToolToIntrusionSetId;

    private LongFilter isUsesToolToMalwareId;

    private LongFilter linkOfId;

    private LongFilter usesToolToThreatActorId;

    public ToolCriteria() {
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

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getVersion() {
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public LongFilter getIsTargetsToolToVulnerabilityId() {
        return isTargetsToolToVulnerabilityId;
    }

    public void setIsTargetsToolToVulnerabilityId(LongFilter isTargetsToolToVulnerabilityId) {
        this.isTargetsToolToVulnerabilityId = isTargetsToolToVulnerabilityId;
    }

    public LongFilter getUsesToolToIntrusionSetId() {
        return usesToolToIntrusionSetId;
    }

    public void setUsesToolToIntrusionSetId(LongFilter usesToolToIntrusionSetId) {
        this.usesToolToIntrusionSetId = usesToolToIntrusionSetId;
    }

    public LongFilter getIsUsesToolToMalwareId() {
        return isUsesToolToMalwareId;
    }

    public void setIsUsesToolToMalwareId(LongFilter isUsesToolToMalwareId) {
        this.isUsesToolToMalwareId = isUsesToolToMalwareId;
    }

    public LongFilter getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(LongFilter linkOfId) {
        this.linkOfId = linkOfId;
    }

    public LongFilter getUsesToolToThreatActorId() {
        return usesToolToThreatActorId;
    }

    public void setUsesToolToThreatActorId(LongFilter usesToolToThreatActorId) {
        this.usesToolToThreatActorId = usesToolToThreatActorId;
    }

    @Override
    public String toString() {
        return "ToolCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (version != null ? "version=" + version + ", " : "") +
                (isTargetsToolToVulnerabilityId != null ? "isTargetsToolToVulnerabilityId=" + isTargetsToolToVulnerabilityId + ", " : "") +
                (usesToolToIntrusionSetId != null ? "usesToolToIntrusionSetId=" + usesToolToIntrusionSetId + ", " : "") +
                (isUsesToolToMalwareId != null ? "isUsesToolToMalwareId=" + isUsesToolToMalwareId + ", " : "") +
                (linkOfId != null ? "linkOfId=" + linkOfId + ", " : "") +
                (usesToolToThreatActorId != null ? "usesToolToThreatActorId=" + usesToolToThreatActorId + ", " : "") +
            "}";
    }

}
