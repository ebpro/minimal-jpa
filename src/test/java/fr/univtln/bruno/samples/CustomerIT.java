package fr.univtln.bruno.samples;

import fr.univtln.bruno.samples.jpa.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Properties;


public class CustomerIT {

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.2-alpine"));
    private static EntityManagerFactory emf;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    Properties configOverrides = new Properties();
    configOverrides.setProperty("jakarta.persistence.jdbc.url", postgreSQLContainer.getJdbcUrl());
    configOverrides.setProperty("jakarta.persistence.jdbc.user", postgreSQLContainer.getUsername());
    configOverrides.setProperty("jakarta.persistence.jdbc.password", postgreSQLContainer.getPassword());
    emf = Persistence.createEntityManagerFactory("hellojpa-pu", configOverrides);
    }

    @AfterAll
    static void afterAll() {
        emf.close();
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void beforeEach() {
        try (EntityManager entityManager = emf.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("delete from Customer").executeUpdate();
            entityManager.getTransaction().commit();
        }
    }

    @Test
    void shouldPersistAndRetrieveCustomer() {
        // Arrange
        String email = "pierre.durand@ici.fr";
        Customer expectedCustomer = Customer.of(email);

        // Act
        persistCustomer(expectedCustomer);

        // Assert
        Customer retrievedCustomer = retrieveCustomer();
        assertEquals(expectedCustomer.getEmail(), retrievedCustomer.getEmail());
    }

    private void persistCustomer(Customer customer) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(customer);
            entityManager.getTransaction().commit();
        }
    }

    private Customer retrieveCustomer() {
        try (EntityManager entityManager = emf.createEntityManager()) {
            List<Customer> result = entityManager.createQuery("select c from Customer c", Customer.class)
                    .getResultList();
            assertFalse(result.isEmpty(), "No customer found");
            return result.get(0);
        }
    }

}