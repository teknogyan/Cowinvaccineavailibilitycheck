package com.example.cowinvaccineavailibilitycheck;

// Class to hold data for both doses by district
public class DistrictData {
    private String centreName;
    private String dose1;
    private String dose2;
    private String vaccineName;

    public DistrictData(String centreName, String dose1, String dose2, String vaccineName) {
        this.centreName = centreName;
        this.dose1 = dose1;
        this.dose2 = dose2;
        this.vaccineName = vaccineName;
    }

    public String getCentreName() {
        return centreName;
    }

    public String getDose1() {
        return dose1;
    }

    public String getDose2() {
        return dose2;
    }

    public String getVaccineName() {
        return vaccineName;
    }
}
