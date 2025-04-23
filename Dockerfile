FROM openjdk:21-slim
LABEL org.opencontainers.image.source="https://github.com/nancho313/video-loader-api"
LABEL org.opencontainers.image.description="Java Microservice that handles all the business logifc of the video loading."
COPY target/video*.jar app.jar
RUN mkdir -p /videos_folder
CMD ["java", "-jar", "/app.jar"]