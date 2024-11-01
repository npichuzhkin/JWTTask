package com.npichuzhkin.JWTTask.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.npichuzhkin.JWTTask.enums.Role;
import com.npichuzhkin.JWTTask.views.Views;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonView(Views.Public.class)
    @NotNull(message = "Username field is required")
    @NotEmpty(message = "Username field must be filled")
    @Size(min = 2, max = 255, message = "Username field must contain from 2 to 255 letters")
    private String username;

    @JsonView(Views.Internal.class)
    @NotNull(message = "Password field is required")
    @NotEmpty(message = "Password field must be filled")
    @Size(min = 4, max = 60, message = "Password field must contain from 4 to 60 characters")
    private String password;

    @NotNull(message = "Role field is required")
    @JsonView(Views.Public.class)
    //@Pattern(regexp = "^(ROLE_USER|ROLE_MODERATOR|ROLE_SUPER_ADMIN)$", message = "Role field must contain one of the roles: USER, MODERATOR or SUPER_ADMIN")
    private Role role;

    @JsonView(Views.Internal.class)
    private boolean nonLocked;

    @JsonView(Views.Internal.class)
    @Digits(integer = 1, fraction = 0, message = "FailedLoginAttempts field can only contain one digit")
    @Min(value = 0, message = "FailedLoginAttempts cannot be negative.")
    @Max(value = 5, message = "FailedLoginAttempts - maximum value exceeded.")
    private int failedLoginAttempts;

    @JsonView(Views.Internal.class)
    @PastOrPresent(message = "LastFailedLogin field must contain either past or current time")
    private LocalDateTime lastFailedLogin;
}
