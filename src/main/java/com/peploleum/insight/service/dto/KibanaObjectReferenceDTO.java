package com.peploleum.insight.service.dto;

import java.io.Serializable;

/**
 * Created by gFolgoas on 08/02/2019.
 */
public class KibanaObjectReferenceDTO implements Serializable {
    private String idObject;
    private String title;

    public String getIdObject() {
        return idObject;
    }

    public void setIdObject(String idObject) {
        this.idObject = idObject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
