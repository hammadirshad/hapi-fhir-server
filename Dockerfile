FROM eclipse-temurin:25-jre as builder

WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted


FROM eclipse-temurin:25-jre

RUN mkdir -p /application /var/log/app-logs /var/log/access-logs

ENV JAVA_OPTS=""
WORKDIR application

COPY --from=builder app/extracted/dependencies/ ./
COPY --from=builder app/extracted/snapshot-dependencies/ ./
COPY --from=builder app/extracted/spring-boot-loader/ ./
COPY --from=builder app/extracted/application/ ./

ENTRYPOINT exec java $JAVA_OPTS -jar application.jar