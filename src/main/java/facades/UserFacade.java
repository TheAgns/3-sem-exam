package facades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.BookingDTO;
import dtos.CarDTO;
import dtos.WashingAssistantsDTO;
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;

import errorhandling.API_Exception;
import errorhandling.NotFoundException;
import security.errorhandling.AuthenticationException;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }


    public void registerUser(String userJSON) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        User userFromDB;
        String username;
        String password;
        try {
            JsonObject json = JsonParser.parseString(userJSON).getAsJsonObject();
            username = json.get("newUsername").getAsString();
            password = json.get("newPassword").getAsString();

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied", 400, e);
        }
        userFromDB = em.find(User.class, username);
        if (userFromDB == null) {
            User user = new User(username, password);
            em.getTransaction().begin();
            Role userRole = new Role("user");
            user.addRole(userRole);
            em.persist(user);
            em.getTransaction().commit();
        } else {
            throw new WebApplicationException("Username: '" + username + "' is already taken", 404);
        }


    }


    //US-1 As a user I would like to see all washing assistants
    public List<WashingAssistantsDTO> getAllAssistants() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<WashingAssistants> query = em.createQuery("SELECT w FROM WashingAssistants w", WashingAssistants.class);
            List<WashingAssistants> washingAssistantsList = query.getResultList();
            ArrayList<WashingAssistantsDTO> washingAssistantsDTOS = new ArrayList<>();
            for (WashingAssistants washingAssistants : washingAssistantsList) {
                washingAssistantsDTOS.add(new WashingAssistantsDTO(washingAssistants));
            }
            return washingAssistantsDTOS;
        } catch (WebApplicationException e) {
            throw new WebApplicationException("Doesn't work", 500);
        }
    }



    //US-2 As a user I would like to see all my bookings
    public List<BookingDTO> getBookingByUser(String username) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.user.userName = :username", Booking.class);
            query.setParameter("username", username);
            List<Booking> bookings = query.getResultList();
            List<BookingDTO> bookingDTOS = new ArrayList<>();
            for (Booking post : bookings) {
                bookingDTOS.add(new BookingDTO(post));
            }
            return bookingDTOS;
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    /*public BookingDTO getBookingByUser(Integer id) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        Booking booking;
        BookingDTO bookingDTO;
        try {
            booking = em.find(Booking.class, id);
            bookingDTO = new BookingDTO(booking);
            return bookingDTO;
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), 500);
        } finally {
            em.close();
        }
    }

     */

    //US-3 As a user I would like to make a booking and assign one or more washing assistants
    public WashingAssistantsDTO connectAssistantToBooking(String bookingId, String assistantJSON) {
        EntityManager em = emf.createEntityManager();
        System.out.println(bookingId);

        int bookingIdInt;
        int assistantIdInt;

        try {
            bookingIdInt = Integer.parseInt(bookingId);
            JsonObject json = JsonParser.parseString(assistantJSON).getAsJsonObject();
            assistantIdInt = json.get("Booking_id").getAsInt();
        } catch (Exception e) {
            // throw new WebApplicationException("Malformed JSON Suplied", 400);
            throw new WebApplicationException(e.getMessage(), 400);
        }

        try {
            Booking booking = em.find(Booking.class,bookingIdInt);
            WashingAssistants washingAssistants = em.find(WashingAssistants.class,assistantIdInt);
            booking.addAssistant(washingAssistants);
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
            return new WashingAssistantsDTO(washingAssistants);
        } catch (RuntimeException e) {
            throw new WebApplicationException(e.getMessage());
        } finally {
            em.close();
        }

    }




    // US-4 As an admin I would like to create a new washing assistant
    public WashingAssistantsDTO createAssistant(String jsonBoat){
        EntityManager em = emf.createEntityManager();
        String name;
        String primaryLanguage;
        String yearsOfExperience;
        String pricePerHour;
        try {
            JsonObject json = JsonParser.parseString(jsonBoat).getAsJsonObject();
            name = json.get("name").getAsString();
            primaryLanguage = json.get("primaryLanguage").getAsString();
            yearsOfExperience = json.get("yearsOfExperience").getAsString();
            pricePerHour = json.get("pricePerHour").getAsString();
        }catch(WebApplicationException e){
            throw new WebApplicationException("Not correct json");
        }
        try {
            WashingAssistants washingAssistants = new WashingAssistants(name,primaryLanguage, Integer.parseInt(yearsOfExperience), Double.parseDouble(pricePerHour));
            em.getTransaction().begin();
            em.persist(washingAssistants);
            em.getTransaction().commit();
            return new WashingAssistantsDTO(washingAssistants);
        } catch (Exception e) {
            throw new WebApplicationException("Couldn't create a new boat");
        } finally {
            em.close();
        }
    }




//US-6 As an admin I would like to update all information about users, bookings, and cars
    public CarDTO editCar(String carId, CarDTO carDTO){
        EntityManager em = emf.createEntityManager();
        int carIdInt = Integer.parseInt(carId);
        Car car = em.find(Car.class,carIdInt);

        car.setBrand(carDTO.getBrand());
        car.setMake(carDTO.getMake());
        car.setRegistrationNumber(carDTO.getRegistrationNumber());
        car.setYear(carDTO.getYear());

        em.getTransaction().begin();
        em.merge(car);
        em.getTransaction().commit();


        return new CarDTO(car);
    }
    //US-6 As an admin I would like to update all information about users, bookings, and cars
    public BookingDTO editBooking(String bookingId, BookingDTO bookingDTO){
        EntityManager em = emf.createEntityManager();
        int bookingIdInt = Integer.parseInt(bookingId);
        Booking booking = em.find(Booking.class,bookingIdInt);

        booking.setBookingTime(bookingDTO.getBookingTime());
        booking.setDuration(bookingDTO.getDuration());

        em.getTransaction().begin();
        em.merge(booking);
        em.getTransaction().commit();


        return new BookingDTO(booking);
    }

//US-7 As an admin I would like to delete a booking
public BookingDTO deleteBooking(Integer id) throws WebApplicationException {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();
        Booking booking = em.find(Booking.class, id);
        em.remove(booking);
        em.getTransaction().commit();
        return new BookingDTO(booking);
    } catch (NullPointerException | IllegalArgumentException ex) {
        throw new WebApplicationException("Could not delete, provided id: " + id + " does not exist", 404);
    } catch (RuntimeException ex) {
        throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
    } finally {
        em.close();
    }
}
    //US-7 As an admin I would like to delete a booking - We need to get all bookings
    public List<BookingDTO> getAllBookings() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b", Booking.class);
            List<Booking> bookingsList = query.getResultList();
            ArrayList<BookingDTO> bookingDTOS = new ArrayList<>();
            for (Booking bookings : bookingsList) {
                bookingDTOS.add(new BookingDTO(bookings));
            }
            return bookingDTOS;
        } catch (WebApplicationException e) {
            throw new WebApplicationException("Doesn't work", 500);
        }
    }




}
