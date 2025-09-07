FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY ./common-dtos/pom.xml common-dtos/pom.xml
COPY ./order-service/pom.xml order-service/pom.xml
COPY ./payment-service/pom.xml payment-service/pom.xml
COPY ./notification-service/pom.xml notification-service/pom.xml 

RUN mvn dependency:go-offline -B

COPY ./common-dtos/src common-dtos/src
COPY ./order-service/src order-service/src
COPY ./payment-service/src payment-service/src
COPY ./notification-service/src notification-service/src 

RUN mvn package -pl notification-service -am -DskipTests

FROM eclipse-temurin:21-jre-alpine

ENV JAVA_OPTS="-XX:InitialRAMPercentage=50.0 -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ExitOnOutOfMemoryError"

WORKDIR /app

RUN addgroup spring -S && adduser spring -S -G spring
USER spring

COPY --from=builder --chown=spring:spring /app/notification-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
