FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
COPY --from=builder /opt/app/target/*.jar /opt/app/target/program.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/app/target/program.jar"]

