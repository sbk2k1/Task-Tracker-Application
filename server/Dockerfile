# Use the official OpenJDK base image
FROM openjdk:21

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/app.jar /app/app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
