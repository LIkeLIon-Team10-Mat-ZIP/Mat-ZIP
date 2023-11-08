FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/app20220916-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]

## 빌드 스테이지
#FROM gradle:7.4-jdk17-alpine AS build
#ARG GRADLE_USER_HOME="/gradle-cache/.gradle"
#ENV GRADLE_USER_HOME="${GRADLE_USER_HOME}"
#WORKDIR /app
#RUN mkdir -p "${GRADLE_USER_HOME}"
#COPY build.gradle settings.gradle ./
#COPY gradle ./gradle
#RUN gradle --no-daemon dependencies || return 0
#COPY . /app
#RUN chmod +x ./gradlew && gradle --no-daemon clean build
#
## 런타임 스테이지
#FROM openjdk:17-jdk-alpine
#COPY --from=build /app/build/libs/*.jar /app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]
