package dtos;

import entities.Booking;
import entities.RenameMe;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDTO {

    private Integer id;
    private LocalDateTime bookingTime;
    private double duration;


    public BookingDTO(Integer id, LocalDateTime bookingTime, double duration) {
        this.id = id;
        this.bookingTime = bookingTime;
        this.duration = duration;
    }
    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.bookingTime = booking.getBookingTime();
        this.duration = booking.getDuration();
    }

    public BookingDTO() {
    }

    public static List<BookingDTO> getDtos(List<Booking> rms){
        List<BookingDTO> rmdtos = new ArrayList();
        rms.forEach(rm->rmdtos.add(new BookingDTO(rm)));
        return rmdtos;
    }

    public Integer getId() {
        return id;
    }



    public void setId(Integer id) {
        this.id = id;
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
}
