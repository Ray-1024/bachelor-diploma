FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y openjdk-21-jdk g++ apparmor-utils && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY target/submission-testing-worker-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]