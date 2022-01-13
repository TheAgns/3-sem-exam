package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BookingDTO;
import dtos.CarDTO;
import dtos.WashingAssistantsDTO;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("admin")
public class AdminResource {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade userFacade = UserFacade.getUserFacade(EMF);
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAllAssistantsss")
    public String getAllAssistants() {
        try {
            List<WashingAssistantsDTO> washingAssistantsDTOs = userFacade.getAllAssistants();
            return new Gson().toJson(washingAssistantsDTOs);
        }catch(WebApplicationException e){
            String errorString = "{\"code\": " + e.getResponse().getStatus() + ", \"message\": \"" + e.getMessage() + "\"}";
            return errorString;
        }
    }
}
