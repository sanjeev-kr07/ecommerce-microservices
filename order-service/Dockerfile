FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle :order-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/order-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]