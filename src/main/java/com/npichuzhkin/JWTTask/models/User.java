package com.npichuzhkin.JWTTask.models;

import com.npichuzhkin.JWTTask.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "non_locked")
    private boolean nonLocked;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;

    @Column(name = "last_failed_login")
    private LocalDateTime lastFailedLogin;
}
