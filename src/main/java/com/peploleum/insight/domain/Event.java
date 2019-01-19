package com.peploleum.insight.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.peploleum.insight.domain.enumeration.EventType;

/**
 * A Event.
 */
@Document(collection = "event")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    @Mapping(mappingPath = "/mappings/asset_id_mapping.json")
    private String id;

    @NotNull
    @Field("event_name")
    private String eventName;

    @Field("event_description")
    private String eventDescription;

    @Field("event_type")
    private EventType eventType;

    @Field("event_date")
    private Instant eventDate;

    @Field("event_coordinates")
    private String eventCoordinates;

    @Field("event_image")
    private byte[] eventImage;

    @Field("event_image_content_type")
    private String eventImageContentType;

    @Field("event_symbol")
    private String eventSymbol;

    @Field("external_id")
    private String externalId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public Event eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public Event eventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
        return this;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Event eventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public Event eventDate(Instant eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventCoordinates() {
        return eventCoordinates;
    }

    public Event eventCoordinates(String eventCoordinates) {
        this.eventCoordinates = eventCoordinates;
        return this;
    }

    public void setEventCoordinates(String eventCoordinates) {
        this.eventCoordinates = eventCoordinates;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public Event eventImage(byte[] eventImage) {
        this.eventImage = eventImage;
        return this;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventImageContentType() {
        return eventImageContentType;
    }

    public Event eventImageContentType(String eventImageContentType) {
        this.eventImageContentType = eventImageContentType;
        return this;
    }

    public void setEventImageContentType(String eventImageContentType) {
        this.eventImageContentType = eventImageContentType;
    }

    public String getEventSymbol() {
        return eventSymbol;
    }

    public Event eventSymbol(String eventSymbol) {
        this.eventSymbol = eventSymbol;
        return this;
    }

    public void setEventSymbol(String eventSymbol) {
        this.eventSymbol = eventSymbol;
    }

    public String getExternalId() {
        return externalId;
    }

    public Event externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDescription='" + getEventDescription() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventCoordinates='" + getEventCoordinates() + "'" +
            ", eventImage='" + getEventImage() + "'" +
            ", eventImageContentType='" + getEventImageContentType() + "'" +
            ", eventSymbol='" + getEventSymbol() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
