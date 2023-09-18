package org.shop.app.entity;

public enum ClientRoles {

    ROLE_USER("USER"),
    ROLE_MODERATOR("MODERATOR");

    private String roleProperty;

    ClientRoles(String roleProperty) {
        this.roleProperty = roleProperty;
    }

    public String extractRoleProperty() {
        return this.roleProperty;
    }

}
