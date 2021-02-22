package com.copernicus.opportunity.dto;

import javax.validation.constraints.NotNull;

public class RequestDTO {

    @NotNull(message = "Id is requested")
    private Integer id;
    @NotNull(message = "Status is requested")
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
