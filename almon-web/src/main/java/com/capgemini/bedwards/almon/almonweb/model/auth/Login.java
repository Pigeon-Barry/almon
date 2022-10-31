package com.capgemini.bedwards.almon.almonweb.model.auth;

import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Data
public class Login {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_INVALID_MESSAGE)
    private String password;
}
