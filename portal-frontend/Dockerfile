# Building
# maven:3.8.1-openjdk AS build
#FROM maven:3.8.1-openjdk AS build
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean package

# Packaging
# openjdk:17-jre-slim
FROM openjdk:8
#COPY --from=build /home/app/target/eureka-service-0.0.1-SNAPSHOT.jar /home/app/eureka-service.jar
ADD target/frontend-docker.jar frontend-docker.jar
EXPOSE 3000
ENTRYPOINT ["java",".jar","/frontend-docker.jar"]

