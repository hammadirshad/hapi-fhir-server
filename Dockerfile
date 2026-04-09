FROM eclipse-temurin:25-jre as builder

WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} layered-app.jar

RUN java -Djarmode=tools -jar layered-app.jar extract --layers --destination ./layers
RUN java -Djarmode=tools -jar layered-app.jar extract --launcher --destination ./layers/spring-boot-loader


FROM eclipse-temurin:25-jre

ENV JAVA_OPTS=""
WORKDIR application

COPY --from=builder app/layers/dependencies/ ./
COPY --from=builder app/layers/snapshot-dependencies/ ./
COPY --from=builder app/layers/spring-boot-loader/ ./
COPY --from=builder app/layers/application/ ./

CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]