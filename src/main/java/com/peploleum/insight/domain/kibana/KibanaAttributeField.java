package com.peploleum.insight.domain.kibana;

import java.io.Serializable;

/**
 * Created by vdautrem on 08/11/2018.
 */
public class KibanaAttributeField implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private String format;
    private int count = 0;
    private boolean searchable = true;
    private boolean aggregatable = false;
    private boolean scripted = false;
    private boolean readFromDocValues = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isAggregatable() {
        return aggregatable;
    }

    public void setAggregatable(boolean aggregatable) {
        this.aggregatable = aggregatable;
    }

    public boolean isScripted() {
        return scripted;
    }

    public void setScripted(boolean scripted) {
        this.scripted = scripted;
    }

    public boolean isReadFromDocValues() {
        return readFromDocValues;
    }

    public void setReadFromDocValues(boolean readFromDocValues) {
        this.readFromDocValues = readFromDocValues;
    }
}
