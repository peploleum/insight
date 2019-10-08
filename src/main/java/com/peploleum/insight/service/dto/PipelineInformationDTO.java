package com.peploleum.insight.service.dto;

public class PipelineInformationDTO {
    private String externalBioId;
    private String name;
    private String surname;
    private ProcessStatusDTO processStatus;

    public String getExternalBioId() {
        return externalBioId;
    }

    public void setExternalBioId(String externalBioId) {
        this.externalBioId = externalBioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ProcessStatusDTO getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatusDTO processStatus) {
        this.processStatus = processStatus;
    }
}
