package com.capgemini.bedwards.almon.almonaccessweb.model.auth;

import com.capgemini.bedwards.almon.almoncore.validators.FieldsMatch;
import com.capgemini.bedwards.almon.almoncore.validators.UserDoesNotExist;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsMatch(fields = {"password", "passwordConfirm"}, message = "Password and Password Confirmation must match")
public class Register {
    @NotNull
    @Email
    @UserDoesNotExist(message = "Email is already registered")
    private String email;
    @NotNull
    @Size(min = 2, max = 100, message = "length should be between 2 and 100")
    private String firstName;
    @NotNull
    @Size(min = 2, max = 100, message = "length should be between 2 and 100")
    private String lastName;

    @NotNull
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_INVALID_MESSAGE)

    private String password;
    @NotNull
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_INVALID_MESSAGE)
    private String passwordConfirm;
}