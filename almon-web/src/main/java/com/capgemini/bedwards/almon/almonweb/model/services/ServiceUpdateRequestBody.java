package com.capgemini.bedwards.almon.almonweb.model.services;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ServiceUpdateRequestBody {
    @NotBlank
    private String name;
    private String description;

}
