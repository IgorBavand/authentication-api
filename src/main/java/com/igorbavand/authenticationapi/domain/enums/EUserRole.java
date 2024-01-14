package com.igorbavand.authenticationapi.domain.enums;

public enum EUserRole {
    ADMIN("admin"),
    USER("user");

    private String role;

    EUserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}