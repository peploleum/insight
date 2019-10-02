package com.peploleum.insight.service.dto;

import com.peploleum.insight.domain.dictionary.Theme;

import java.io.Serializable;
import java.util.List;

public class DictionaryDTO implements Serializable {

    private String id;
    private String name;
    private List<Theme> theme;

    public List<Theme> getTheme() {
        return theme;
    }

    public void setTheme(List<Theme> theme) {
        this.theme = theme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
