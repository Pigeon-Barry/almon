package com.capgemini.bedwards.almon.almonaccessweb.model.auth;

import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
public class Login {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_INVALID_MESSAGE)
    private String password;
}
