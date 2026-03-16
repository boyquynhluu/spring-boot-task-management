package com.taskmanagement.dto;

import com.taskmanagement.validation.EmailConstraint;
import com.taskmanagement.validation.PasswordConstraint;
import com.taskmanagement.validation.UsernameConstraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    String name;

    @UsernameConstraint
    String username;

    @EmailConstraint
    String email;

    @PasswordConstraint
    String password;
}
