package dtos;

import entities.WashingAssistants;

import javax.persistence.Column;

public class WashingAssistantsDTO {

    private Integer id;
    private String name;
    private String primaryLanguage;
    private int yearsOfExperience;
    private double pricePerHour;

    public WashingAssistantsDTO(String name, String primaryLanguage, int yearsOfExperience, double pricePerHour) {
        this.name = name;
        this.primaryLanguage = primaryLanguage;
        this.yearsOfExperience = yearsOfExperience;
        this.pricePerHour = pricePerHour;
    }

    public WashingAssistantsDTO(WashingAssistants washingAssistants) {
        this.name = washingAssistants.getName();
        this.primaryLanguage = washingAssistants.getPrimaryLanguage();
        this.yearsOfExperience = washingAssistants.getYearsOfExperience();
        this.pricePerHour = washingAssistants.getPricePerHour();
    }
    public WashingAssistantsDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    @Override
    public String toString() {
        return "WashingAssistantsDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", primaryLanguage='" + primaryLanguage + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", pricePerHour=" + pricePerHour +
                '}';
    }
}
