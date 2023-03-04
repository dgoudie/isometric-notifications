FROM maven:3.9.0-eclipse-temurin-17-alpine as build
ENV spring_profiles_active=prod
ENV PUSHER_BEAMS_INSTANCE_ID=dummy
ENV PUSHER_BEAMS_SECRET=dummy
WORKDIR /workspace/app
COPY pom.xml .
COPY src src

RUN mvn clean install -q
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
ENV spring_profiles_active=prod
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","dev.goudie.isometric_notifications.IsometricNotificationsApplication"]