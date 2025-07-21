package br.com.portfolio.model.enums;

public enum RoleType {
    EMPLOYEE,
    MANAGER;

    public static RoleType fromString(String value) {
        return RoleType.valueOf(value.toUpperCase());
    }
}
