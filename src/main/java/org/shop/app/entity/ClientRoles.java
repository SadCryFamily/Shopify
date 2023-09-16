package org.shop.app.entity;

public enum ClientRoles {

    ROLE_USER("USER"),
    ROLE_MODERATOR("MODERATOR"),
    ROLE_ADMIN("ADMIN");

    private String roleProperty;

    ClientRoles(String roleProperty) {
        this.roleProperty = roleProperty;
    }

    public String extractRoleProperty() {
        return this.roleProperty;
    }

}
