package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BookingDTO;
import dtos.CarDTO;
import dtos.WashingAssistantsDTO;
import entities.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import errorhandling.API_Exception;
import facades.FacadeExample;
import facades.UserFacade;
import utils.EMF_Creator;
import utils.SetupTestUsers;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade userFacade = UserFacade.getUserFacade(EMF);
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String registerUser(String user) throws API_Exception {
        try {
            userFacade.registerUser(user);
            return "You have been registered";
        }catch(API_Exception e){
            throw new API_Exception(e.getMessage());
        }
    }
    @GET
    @Path("populate")
    @Produces(MediaType.APPLICATION_JSON)
    public String populateTestUsers(){
        SetupTestUsers.populateTestUsers();
        return "{\"msg\": \"database populated\"}";
    }


    //US-1 As a user I would like to see all washing assistants
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAllAssistants")
    public String getAllAssistants() {
        try {
            List<WashingAssistantsDTO> washingAssistantsDTOs = userFacade.getAllAssistants();
            return gson.toJson(washingAssistantsDTOs);
        }catch(WebApplicationException e){
            String errorString = "{\"code\": " + e.getResponse().getStatus() + ", \"message\": \"" + e.getMessage() + "\"}";
            return errorString;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAllBookings")
    public String getAllBookings() {
        try {
            List<BookingDTO> bookingDTOS = userFacade.getAllBookings();
            return gson.toJson(bookingDTOS);
        }catch(WebApplicationException e){
            String errorString = "{\"code\": " + e.getResponse().getStatus() + ", \"message\": \"" + e.getMessage() + "\"}";
            return errorString;
        }
    }

    //US-2 As a user I would like to see all my bookings
    @Path("/getBooking/{username}")
    // @RolesAllowed("user")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getBookingByUser(@PathParam("username") String username) {
        try {
            List<BookingDTO> list = userFacade.getBookingByUser(username);
            return gson.toJson(list);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(),ex.getResponse().getStatus());
        }
    }



    //US-3 As a user I would like to make a booking and assign one or more washing assistants
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/connectAssistantToBooking/{id}")
    public String connectBoatToHarbour(@PathParam("id") String bookingId, String assistantId){
        userFacade.connectAssistantToBooking(bookingId,assistantId);
        return "";
    }


   // US-4 As an admin I would like to create a new washing assistant
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/createAssistant")
   public String createAssistant(String jsonBoat){
       try {
           WashingAssistantsDTO washingAssistantsDTO = userFacade.createAssistant(jsonBoat);
           return gson.toJson(washingAssistantsDTO);
       }catch(WebApplicationException e){
           throw new WebApplicationException(e.getMessage());
       }
   }



    //US-6 As an admin I would like to update all information about users, bookings, and cars
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/editCar/{carId}")
    public String editBoat(@PathParam("carId") String carId, String jsonCar){
        CarDTO carDTO = gson.fromJson(jsonCar,CarDTO.class);
        CarDTO carDTO1 = userFacade.editCar(carId,carDTO);
        return gson.toJson(carDTO1);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/editBooking/{bookingId}")
    public String editBooking(@PathParam("bookingId") String bookingId, String jsonBooking){
        BookingDTO bookingDTO = gson.fromJson(jsonBooking,BookingDTO.class);
        BookingDTO bookingDTO1 = userFacade.editBooking(bookingId,bookingDTO);
        return gson.toJson(bookingDTO1);
    }


    //US-7 As an admin I would like to delete a booking
    @Path("/deleteBooking/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteBooking(@PathParam("id")Integer id) {
        try {
            BookingDTO bookingDTO = userFacade.deleteBooking(id);;
            return "{\"booking\": \"removed\"}";
        } catch (WebApplicationException ex) {
            String errorString = "{\"code\": " + ex.getResponse().getStatus() + ", \"message\": \"" + ex.getMessage() + "\"}";
            return errorString;
        }

    }
}