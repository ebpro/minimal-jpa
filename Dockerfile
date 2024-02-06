# Use a specific version of the JDK for the build stage
FROM eclipse-temurin:17-jdk AS stage-build
# Set the working directory in the Docker image
WORKDIR /app
# Copy the source code into the Docker image
COPY . /app
# Use a cache mount for Maven dependencies and build the application
RUN --mount=type=cache,target=/root/.m2 ./mvnw package

# Use a specific version of the JRE for the runtime stage
FROM eclipse-temurin:17-jre
# Copy the built JAR file from the build stage to the runtime stage
COPY --from=stage-build /app/target/*-jar-with-dependencies.jar /myapp.jar
# Set the command to run the application
CMD ["java","-jar","/myapp.jar"]