FROM eclipse-temurin:21.0.7_6-jre as builder

WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} layered-app.jar
RUN java -Djarmode=layertools -jar layered-app.jar extract

FROM eclipse-temurin:21.0.7_6-jre

ENV JAVA_OPTS=""

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/application/ ./

CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]