package com.example.demo.jwt.model;

import com.example.demo.jwt.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDtoRole {

    private Set<Role> roles;
}
