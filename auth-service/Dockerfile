FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY eureka-server/pom.xml eureka-server/
COPY config-server/pom.xml config-server/
COPY auth-service/pom.xml auth-service/
COPY customer-service/pom.xml customer-service/
COPY table-service/pom.xml table-service/
COPY reservation-service/pom.xml reservation-service/
COPY notification-service/pom.xml notification-service/
COPY api-gateway/pom.xml api-gateway/

RUN mvn dependency:go-offline -pl auth-service -am -q

COPY auth-service/src auth-service/src
RUN mvn clean package -pl auth-service -am -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
