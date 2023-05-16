FROM maven:3.8.3-openjdk-17 AS build
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean package

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/avaliacao-0.0.1.jar /app
EXPOSE 8080
CMD ["java", "-jar", "avaliacao-0.0.1.jar"]
