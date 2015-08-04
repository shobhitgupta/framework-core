package com.sape.enums;

public enum Browsers {
    CHROME("CH"), INTERNET_EXPLORER("IE"), FIREFOX("FF"), PHANTOMJS("PJ"), OPENFIN("OF"), INVALID("");

    private String text;

    Browsers(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Browsers fromString(String text) {
        for (Browsers browsers : Browsers.values()) {
            if (text.equalsIgnoreCase(browsers.text)) {
                return browsers;
            }
        }
        return Browsers.INVALID;
    }
}