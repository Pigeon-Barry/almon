package com.capgemini.bedwards.almon.almonaccessweb.model.auth;

import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
@Data
public class Login {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_INVALID_MESSAGE)
    private String password;
}
