package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.List;

public class DictionaryDTO implements Serializable {


    private String id;
    private List<ThemeDTO> themeDTOList;


    public List<ThemeDTO> getThemeDTOList() {
        return themeDTOList;
    }

    public void setThemeDTOList(List<ThemeDTO> themeDTOList) {
        this.themeDTOList = themeDTOList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
