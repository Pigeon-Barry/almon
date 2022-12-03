package com.capgemini.bedwards.almon.almonweb.model.services;

import com.capgemini.bedwards.almon.almoncore.validators.ServiceDoesNotExist;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceRequestBody {

    @NotBlank
    @Pattern(regexp = Constants.SERVICE_KEY_REGEX, message = Constants.SERVICE_KEY_REGEX_INVALID_MESSAGE)
    @Size(max = Constants.SERVICE_ID_MAX_LENGTH)
    @ServiceDoesNotExist(message = "A Service with this key already exists")
    private String key;

    @NotBlank
    private String name;


    private String description;

}
