# Build stage
FROM maven:3.8.7-openjdk-18 As build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=0.0.1-SNAPSHOT

WORKDIR /app
COPY --from=build /build/target/readly*.jar /app/

EXPOSE 8088
ENV DB_URL=jdbc:postgresql://postgres-readly:5432/readly
ENV SPRING_PROFILES_ACTIVE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOSTNAME=missing_hostname
ENV EMAIL_USERNAME=missing_username
ENV EMAIL_PASSWORD=missing_password

CMD java -jar -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dspring.datasource.url=${DB_URL} readly-${JAR_VERSION}.jar

