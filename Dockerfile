FROM maven:3.9.10-eclipse-temurin-17-noble AS builder
COPY pom.xml /build/
COPY src /build/src
WORKDIR /build/
RUN mvn install

FROM eclipse-temurin:17.0.15_6-jre-ubi9-minimal
WORKDIR /app

ARG JAR_FILE=/build/target/*.jar

COPY --from=builder ${JAR_FILE} /app/app.jar

RUN mkdir -p /app/input_samples

EXPOSE 8020

ENTRYPOINT ["java", "-jar", "app.jar"]