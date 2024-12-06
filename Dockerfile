FROM openjdk:21

COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar

WORKDIR /app

ENTRYPOINT ["java","-jar","demo-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080