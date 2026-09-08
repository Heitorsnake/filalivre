FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build
COPY backend/pom.xml backend/pom.xml
COPY backend/src backend/src
RUN mvn -f backend/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /build/backend/target/filalivre-backend-1.0.0.jar app.jar
COPY frontend /frontend

ENV FILALIVRE_FRONTEND_PATH=/frontend
ENV FILALIVRE_DB_PATH=/data/filalivre

RUN mkdir -p /data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]