FROM maven:3-eclipse-temurin-21 as build
COPY src /home/app/src
COPY pom.xml /home/app
COPY .git /home/app/.git
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:20
MAINTAINER joel1102
LABEL org.opencontainers.image.description = "Game Library by Joel Schaltenbrand"
LABEL org.opencontainers.image.source = "https://github.com/joel-schaltenbrand/GameLibrary"
COPY --from=build /home/app/target/gamelibrary.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
