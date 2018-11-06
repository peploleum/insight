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
 * Criteria class for the NetLink entity. This class is used in NetLinkResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /net-links?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NetLinkCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter description;

    private StringFilter nom;

    private StringFilter type;

    private StringFilter level;

    private LongFilter isLinkOfAttackPatternId;

    private LongFilter isLinkOfCampaignId;

    private LongFilter isLinkOfCourseOfActionId;

    private LongFilter isLinkOfActorId;

    private LongFilter isLinkOfActivityPatternId;

    private LongFilter isLinkOfIntrusionSetId;

    private LongFilter isLinkOfMalwareId;

    private LongFilter isLinkOfObservedDataId;

    private LongFilter isLinkOfReportId;

    private LongFilter isLinkOfThreatActorId;

    private LongFilter isLinkOfToolId;

    private LongFilter isLinkOfVulnerabilityId;

    public NetLinkCriteria() {
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

    public StringFilter getLevel() {
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }

    public LongFilter getIsLinkOfAttackPatternId() {
        return isLinkOfAttackPatternId;
    }

    public void setIsLinkOfAttackPatternId(LongFilter isLinkOfAttackPatternId) {
        this.isLinkOfAttackPatternId = isLinkOfAttackPatternId;
    }

    public LongFilter getIsLinkOfCampaignId() {
        return isLinkOfCampaignId;
    }

    public void setIsLinkOfCampaignId(LongFilter isLinkOfCampaignId) {
        this.isLinkOfCampaignId = isLinkOfCampaignId;
    }

    public LongFilter getIsLinkOfCourseOfActionId() {
        return isLinkOfCourseOfActionId;
    }

    public void setIsLinkOfCourseOfActionId(LongFilter isLinkOfCourseOfActionId) {
        this.isLinkOfCourseOfActionId = isLinkOfCourseOfActionId;
    }

    public LongFilter getIsLinkOfActorId() {
        return isLinkOfActorId;
    }

    public void setIsLinkOfActorId(LongFilter isLinkOfActorId) {
        this.isLinkOfActorId = isLinkOfActorId;
    }

    public LongFilter getIsLinkOfActivityPatternId() {
        return isLinkOfActivityPatternId;
    }

    public void setIsLinkOfActivityPatternId(LongFilter isLinkOfActivityPatternId) {
        this.isLinkOfActivityPatternId = isLinkOfActivityPatternId;
    }

    public LongFilter getIsLinkOfIntrusionSetId() {
        return isLinkOfIntrusionSetId;
    }

    public void setIsLinkOfIntrusionSetId(LongFilter isLinkOfIntrusionSetId) {
        this.isLinkOfIntrusionSetId = isLinkOfIntrusionSetId;
    }

    public LongFilter getIsLinkOfMalwareId() {
        return isLinkOfMalwareId;
    }

    public void setIsLinkOfMalwareId(LongFilter isLinkOfMalwareId) {
        this.isLinkOfMalwareId = isLinkOfMalwareId;
    }

    public LongFilter getIsLinkOfObservedDataId() {
        return isLinkOfObservedDataId;
    }

    public void setIsLinkOfObservedDataId(LongFilter isLinkOfObservedDataId) {
        this.isLinkOfObservedDataId = isLinkOfObservedDataId;
    }

    public LongFilter getIsLinkOfReportId() {
        return isLinkOfReportId;
    }

    public void setIsLinkOfReportId(LongFilter isLinkOfReportId) {
        this.isLinkOfReportId = isLinkOfReportId;
    }

    public LongFilter getIsLinkOfThreatActorId() {
        return isLinkOfThreatActorId;
    }

    public void setIsLinkOfThreatActorId(LongFilter isLinkOfThreatActorId) {
        this.isLinkOfThreatActorId = isLinkOfThreatActorId;
    }

    public LongFilter getIsLinkOfToolId() {
        return isLinkOfToolId;
    }

    public void setIsLinkOfToolId(LongFilter isLinkOfToolId) {
        this.isLinkOfToolId = isLinkOfToolId;
    }

    public LongFilter getIsLinkOfVulnerabilityId() {
        return isLinkOfVulnerabilityId;
    }

    public void setIsLinkOfVulnerabilityId(LongFilter isLinkOfVulnerabilityId) {
        this.isLinkOfVulnerabilityId = isLinkOfVulnerabilityId;
    }

    @Override
    public String toString() {
        return "NetLinkCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
                (isLinkOfAttackPatternId != null ? "isLinkOfAttackPatternId=" + isLinkOfAttackPatternId + ", " : "") +
                (isLinkOfCampaignId != null ? "isLinkOfCampaignId=" + isLinkOfCampaignId + ", " : "") +
                (isLinkOfCourseOfActionId != null ? "isLinkOfCourseOfActionId=" + isLinkOfCourseOfActionId + ", " : "") +
                (isLinkOfActorId != null ? "isLinkOfActorId=" + isLinkOfActorId + ", " : "") +
                (isLinkOfActivityPatternId != null ? "isLinkOfActivityPatternId=" + isLinkOfActivityPatternId + ", " : "") +
                (isLinkOfIntrusionSetId != null ? "isLinkOfIntrusionSetId=" + isLinkOfIntrusionSetId + ", " : "") +
                (isLinkOfMalwareId != null ? "isLinkOfMalwareId=" + isLinkOfMalwareId + ", " : "") +
                (isLinkOfObservedDataId != null ? "isLinkOfObservedDataId=" + isLinkOfObservedDataId + ", " : "") +
                (isLinkOfReportId != null ? "isLinkOfReportId=" + isLinkOfReportId + ", " : "") +
                (isLinkOfThreatActorId != null ? "isLinkOfThreatActorId=" + isLinkOfThreatActorId + ", " : "") +
                (isLinkOfToolId != null ? "isLinkOfToolId=" + isLinkOfToolId + ", " : "") +
                (isLinkOfVulnerabilityId != null ? "isLinkOfVulnerabilityId=" + isLinkOfVulnerabilityId + ", " : "") +
            "}";
    }

}
