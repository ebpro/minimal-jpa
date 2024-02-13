# Minimal JPA

Minimal JPA is a simple application that uses Jakarta Persistence to interact with a PostgreSQL database. 
The project is written in Java and uses Maven for dependency management.

## Project Structure

The project is structured as follows:

- `src/`: Contains the source code of the application.
- `Customer.java`: A JPA Entity that represents a customer.
- `App.java`: A class that demonstrates how to use JPA to interact with the database.
- `persistence.xml`: The JPA configuration file, which defines the persistence unit used by the application.
-  `config.properties`: The configuration file, which defines the database connection properties.
-  `logback.xml`: The file which defines the logging configuration.
- `.env`: The environment file, which defines the environment variables used by the application.
- `pom.xml`: The Maven configuration file, which defines the project's dependencies.
- `Dockerfile`: The Dockerfile, which defines how to build the Docker image of the Java application.
-  `.dockerignore`: The dockerignore file, which defines the files and directories to ignore when building the Docker image.
- `docker-compose.yml`: The Docker Compose file, which defines how to launch the application and its associated services (the database).
- `.env`: The environment file, which defines the environment variables used by the application.
- `.gitignore`: The gitignore file, which defines the files and directories to ignore in the Git repository.

## How to Launch the Project

1. Clone the Git repository.
2. Launch the application and its associated services using the command `docker compose up --build`. 

## How to develop the project

1. Clone the Git repository.
2. Launch the postgres database using the command `docker-compose up -d  db`.
3. Connect your IDE to the database (see config.properties for the default connection properties). 

## License

This project is licensed under the MIT license. See the `LICENSE` file for more details.
