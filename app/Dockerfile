FROM eclipse-temurin:21.0.7_6-jdk AS builder

WORKDIR /application

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract \
    && rm application.jar


FROM eclipse-temurin:21.0.7_6-jre AS runtime

RUN addgroup --system app \
    && adduser --system --ingroup app app

WORKDIR /application

COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application ./

USER app

HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD ["curl", "--fail", "--silent", "--output", "/dev/null", "http://localhost:8080/actuator/health"]

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
