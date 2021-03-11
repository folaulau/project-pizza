package com.lovemesomecoding.pizzaria.entity.user;

public enum UserStatus {

    ACTIVE,
    INACTIVE,
    BLOCKED;

    public static boolean isActive(UserStatus status) {
        // TODO Auto-generated method stub
        if (status == null) {
            return false;
        }

        if (status.equals(ACTIVE)) {
            return true;
        }

        return false;
    }

}
