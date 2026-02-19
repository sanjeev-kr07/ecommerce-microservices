FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle :inventory-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/inventory-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]