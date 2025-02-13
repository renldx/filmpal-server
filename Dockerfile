FROM openjdk:23-jdk

ARG key
ENV OPENAI_API_KEY $key

COPY target/filmpal-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080
