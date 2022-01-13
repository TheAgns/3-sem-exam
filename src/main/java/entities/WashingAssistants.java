package entities;

import javax.persistence.*;
import java.util.List;

@Table(name = "washing_assistants")
@Entity
public class WashingAssistants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "primaryLanguage")
    String primaryLanguage;

    @Column(name = "yearsOfExperience")
    int yearsOfExperience;

    @Column(name = "pricePerHour")
    double pricePerHour;

   @ManyToOne
    Booking booking;


    public WashingAssistants(String name, String primaryLanguage, int yearsOfExperience, double pricePerHour) {
        this.name = name;
        this.primaryLanguage = primaryLanguage;
        this.yearsOfExperience = yearsOfExperience;
        this.pricePerHour = pricePerHour;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public WashingAssistants() {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}