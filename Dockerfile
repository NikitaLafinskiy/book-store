FROM openjdk AS builder

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk

WORKDIR /app

COPY --from=builder /app/target/*.jar /app.jar
COPY --from=builder /app/src/main/resources/application.properties application.properties

CMD ["java", "-jar", "/app.jar"]