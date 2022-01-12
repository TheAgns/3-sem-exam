package facades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

import errorhandling.API_Exception;
import security.errorhandling.AuthenticationException;

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

}
