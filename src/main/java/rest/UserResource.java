package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.WashingAssistantsDTO;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("user")
public class UserResource {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
        private final UserFacade userFacade = UserFacade.getUserFacade(EMF);
        @Context
        private UriInfo context;

        @Context
        SecurityContext securityContext;



    //USER 1
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String getAllAssistants() {
        try {
            List<WashingAssistantsDTO> washingAssistantsDTOs = userFacade.getAllAssistants();
            return gson.toJson(washingAssistantsDTOs);
        }catch(WebApplicationException e){
            String errorString = "{\"code\": " + e.getResponse().getStatus() + ", \"message\": \"" + e.getMessage() + "\"}";
            return errorString;
        }
    }
}
