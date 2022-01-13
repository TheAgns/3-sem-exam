package entities;

import dtos.BookingDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "booking")
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "bookingTime")
    private LocalDateTime bookingTime;

    @Column(name = "duration")
    private double duration;

    @ManyToMany
    private List<WashingAssistants> washingAssistants;

    @ManyToOne
    private User user;

    @OneToMany
    private List<WashingAssistants> washingAssistantsAssign;

   /* @ManyToOne
    private WashingAssistants washingAssistants2;*/

    public Booking(User user, LocalDateTime bookingTime, double duration) {
        this.user = user;
        this.bookingTime = bookingTime;
        this.duration = duration;
    }

    public Booking(BookingDTO bookingDTO){
        this.id = bookingDTO.getId();
    }

    public Booking(Integer id, User user, LocalDateTime bookingTime, double duration) {
       this.id = id;
        this.user = user;
        this.bookingTime = bookingTime;
        this.duration = duration;
    }

    public void setWashingAssistantsAssign(List<WashingAssistants> washingAssistantsAssign) {
        this.washingAssistantsAssign = washingAssistantsAssign;
    }

    public void addAssistant(WashingAssistants washingAssistant) {
        this.washingAssistantsAssign.add(washingAssistant);
        if (washingAssistant != null){
            washingAssistant.setBooking(this);
        }
    }


    public Booking() {
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}