FROM maven:3.8.7-openjdk-18 AS build
COPY src /usr/app/src
COPY pom.xml /usr/app
COPY checkstyle.xml /usr/app
RUN mvn -f /usr/app/pom.xml clean package

FROM openjdk:18
COPY --from=build /usr/app/target/book-store-0.0.1-SNAPSHOT.jar /usr/app/book-store-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/book-store-0.0.1-SNAPSHOT.jar"]