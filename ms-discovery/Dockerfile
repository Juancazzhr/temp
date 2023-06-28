FROM amazoncorretto:17.0.7-al2023-headless
ARG JAR_FILE=target/ms-discovery-0.0.1-SNAPSHOT.jar
WORKDIR /home/app
COPY $JAR_FILE ms-discovery.jar
LABEL authors='Juancazz'
ENTRYPOINT ["java", "-jar", "ms-discovery.jar"]
EXPOSE 8761