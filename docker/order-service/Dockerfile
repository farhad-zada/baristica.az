FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY ./common-dtos/pom.xml common-dtos/pom.xml
COPY ./order-service/pom.xml order-service/pom.xml
COPY ./payment-service/pom.xml payment-service/pom.xml
COPY ./notification-service/pom.xml notification-service/pom.xml 

RUN mvn dependency:go-offline -B

COPY ./common-dtos/src ./common-dtos/src
COPY ./order-service/src ./order-service/src

RUN mvn package -pl order-service -am -DskipTests

FROM eclipse-temurin:21-jre-alpine

ENV JAVA_OPTS="-XX:InitialRAMPercentage=75.0 -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ExitOnOutOfMemoryError"

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app

COPY --from=builder --chown=spring:spring /app/order-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar


