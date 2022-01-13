package com.mediscreen.massessment.model.constant;

public enum RiskLevel {

    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("In Danger"),
    EARLY_ONSET("Early onset");

    private String value;

    RiskLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
