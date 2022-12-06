package com.donggyeong.voicecollector.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER"),
    ANONYMOUS("ROLE_ANONYMOUS");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}