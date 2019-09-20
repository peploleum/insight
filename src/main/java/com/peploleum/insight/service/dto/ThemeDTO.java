package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.List;

public class ThemeDTO implements Serializable {

    private String id;
    private String name;
    private List<MotclefDTO> motclefDTOList;

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

    public List<MotclefDTO> getMotclefDTOList() {
        return motclefDTOList;
    }

    public void setMotclefDTOList(List<MotclefDTO> motclefDTOList) {
        this.motclefDTOList = motclefDTOList;
    }
}
