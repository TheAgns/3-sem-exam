package interfaces;

import dtos.BookingDTO;
import dtos.WashingAssistantsDTO;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public interface UserInterface {

    public List<WashingAssistantsDTO> getAllAssistants();

    public List<BookingDTO> getBookingByUser(String username) throws WebApplicationException;

    public WashingAssistantsDTO connectAssistantToBooking(String bookingId, String assistantJSON);
}
