package com.capgemini.bedwards.almon.almonaccessapi.models.auth;

import com.capgemini.bedwards.almon.almoncore.validators.FieldsMatch;
import com.capgemini.bedwards.almon.almoncore.validators.UserDoesNotExist;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@FieldsMatch(fields = {"password", "passwordConfirm"}, message = "Password and Password Confirmation must match")
public class Register {
    @NotBlank
    @Email
    @UserDoesNotExist(message = "Email is already registered")
    private String email;
    @NotBlank
    @Size(min = 2, max = 100, message = "length should be between 2 and 100")
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 100, message = "length should be between 2 and 100")
    private String lastName;

    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_INVALID_MESSAGE)

    private String password;
    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_INVALID_MESSAGE)
    private String passwordConfirm;
}
