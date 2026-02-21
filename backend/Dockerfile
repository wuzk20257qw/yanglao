FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package -DskipITs

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/eldercare-backend-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
