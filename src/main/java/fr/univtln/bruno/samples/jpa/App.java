package fr.univtln.bruno.samples.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * La classe App est le point d'entrée d'une application de démonstration de JPA.
 */
@Slf4j
public class App
{
        public static void main( String[] args )
    {
        //load a properties file from classpath
        Properties configfileProperties = new Properties();
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                log.error("Sorry, unable to find config.properties");
                System.exit(1);
            }
            configfileProperties.load(input);
        } catch (IOException ex) {
            log.error("Sorry, unable to find config.properties: {}", ex.getMessage());
            System.exit(1);
        }

        //Override the jakarta persistence properties from the environment.
        Properties configOverrides = new Properties();
        Map.of("jakarta.persistence.jdbc.url", "DB_URL",
               "jakarta.persistence.jdbc.user", "DB_USERNAME",
               "jakarta.persistence.jdbc.password", "DB_PASSWORD")
                .forEach((k,v)->configOverrides.setProperty(k, Optional.ofNullable(System.getenv(v)).orElse(configfileProperties.getProperty(v.toLowerCase().replace("_", ".")))));
        log.debug("Connection to {} as {}", configOverrides.getProperty("jakarta.persistence.jdbc.url"), configOverrides.getProperty("jakarta.persistence.jdbc.user"));

        // Create an EntityManagerFactory instance with the configuration properties
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("hellojpa-pu", configOverrides))
        {
            // Create an EntityManager instance and start a transaction
            try (EntityManager entityManager = emf.createEntityManager()) {
                entityManager.getTransaction().begin();
                // Persist a new Customer object in the database
                entityManager.persist(Customer.of("pierre.durand@ici.fr"));
                // Commit the transaction
                entityManager.getTransaction().commit();
            }

            // Create a new EntityManager instance and start another transaction
            try (EntityManager entityManager = emf.createEntityManager()) {
                // Retrieve all Customer objects from the database and display them
                entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                            .getResultList()
                            .forEach(c->log.info("Customer: {}", c));
            }
        }
    }
}
