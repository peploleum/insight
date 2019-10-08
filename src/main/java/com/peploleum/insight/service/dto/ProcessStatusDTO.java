package com.peploleum.insight.service.dto;

public class ProcessStatusDTO {
    private Integer urlHitCount;
    private Integer imageHitCount;

    public Integer getUrlHitCount() {
        return urlHitCount;
    }

    public void setUrlHitCount(Integer urlHitCount) {
        this.urlHitCount = urlHitCount;
    }

    public Integer getImageHitCount() {
        return imageHitCount;
    }

    public void setImageHitCount(Integer imageHitCount) {
        this.imageHitCount = imageHitCount;
    }
}
