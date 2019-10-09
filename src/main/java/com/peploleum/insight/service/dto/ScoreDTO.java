package com.peploleum.insight.service.dto;

import java.util.List;

public class ScoreDTO {
    private int points;

    private List<String> listThemeMotclefHit;
    private int imageHit;
    private int frequence;
    private int depthLevel;
    private String idDictionary;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<String> getlistThemeMotclefHit() {
        return listThemeMotclefHit;
    }

    public void setlistThemeMotclefHit(List<String> listMotClefs) {
        this.listThemeMotclefHit = listMotClefs;
    }

    public int getImageHit() {
        return imageHit;
    }

    public void setImageHit(int imageHit) {
        this.imageHit = imageHit;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public String getIdDictionary() {
        return idDictionary;
    }

    public void setIdDictionary(String idDictionary) {
        this.idDictionary = idDictionary;
    }
}
