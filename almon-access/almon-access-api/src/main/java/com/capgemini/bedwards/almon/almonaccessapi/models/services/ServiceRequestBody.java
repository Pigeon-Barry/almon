package com.capgemini.bedwards.almon.almonaccessapi.models.services;

import com.capgemini.bedwards.almon.almoncore.validators.ServiceDoesNotExist;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ToString
public class ServiceRequestBody {

    @NotBlank
    @Pattern(regexp = Constants.SERVICE_KEY_REGEX, message = Constants.SERVICE_KEY_REGEX_INVALID_MESSAGE)
    @ServiceDoesNotExist(message = "A Service with this key already exists")
    private String key;

    @NotBlank
    private String name;


    private String description;

}
