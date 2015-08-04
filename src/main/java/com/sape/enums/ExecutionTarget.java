package com.sape.enums;

public enum ExecutionTarget {
    LOCAL, GRID, INVALID;

    private String text;

    public String getText() {
        return this.text;
    }

    public static ExecutionTarget fromString(String text) {
        for (ExecutionTarget target : ExecutionTarget.values()) {
            if (text.equalsIgnoreCase(target.name())) {
                return target;
            }
        }
        return ExecutionTarget.INVALID;
    }
}
