package utils;


import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.awt.print.Book;
import java.time.LocalDateTime;

public class SetupTestUsers {

  public static void main(String[] args) {
   
  }

  public static void populateTestUsers(){
    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();

    WashingAssistants washingAssistants = new WashingAssistants("James","English",5,200);
    User user1 = new User("test1","1234");
    User user2 = new User("test2","1234");
    User user3 = new User("test3","1234");
    User user4 = new User("test4","1234");
    Booking booking = new Booking(user1,LocalDateTime.now(), 25);
    Booking booking1 = new Booking(user1,LocalDateTime.now(), 50);
    Booking booking2 = new Booking(user2,LocalDateTime.now(), 76);
    Booking booking3 = new Booking(user3,LocalDateTime.now(), 100);
    Booking booking4 = new Booking(user4,LocalDateTime.now(), 15);
    Booking booking5 = new Booking(user2,LocalDateTime.now(), 20);
    Booking booking6 = new Booking(user3,LocalDateTime.now(), 10);
    Car car = new Car("md43201", "Audi", "a6","2018");
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    User user = new User("user", "test1");
    User admin = new User("admin", "test2");
    User both = new User("user_admin", "test3");



    if(admin.getUserPass().equals("test")||user.getUserPass().equals("test")||both.getUserPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    user1.addRole(userRole);
    user.addRole(userRole);
    admin.addRole(adminRole);
    both.addRole(userRole);
    both.addRole(adminRole);
    em.persist(userRole);
    em.persist(adminRole);
    em.persist(user);
    em.persist(admin);
    em.persist(both);

    em.persist(washingAssistants);
    em.persist(booking);
    em.persist(booking1);
    em.persist(booking2);
    em.persist(booking3);
    em.persist(booking4);
    em.persist(booking5);
    em.persist(booking6);
    em.persist(car);
    em.persist(user1);
    em.persist(user2);
    em.persist(user3);
    em.persist(user4);

    em.getTransaction().commit();
    System.out.println("PW: " + user.getUserPass());
    System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
    System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
    System.out.println("Created TEST Users");


  }

}
