package com.sape.enums;

public enum Suites {
    SANITY, REGRESSION, DUMMY, INVALID;

    private String text;

    public String getText() {
        return this.text;
    }

    public static Suites fromString(String text) {
        for (Suites target : Suites.values()) {
            if (text.equalsIgnoreCase(target.name())) {
                return target;
            }
        }
        return Suites.INVALID;
    }
}
