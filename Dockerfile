FROM maven:3.8.4-openjdk-8-slim as maven_build
ENV APP_NAME payment
WORKDIR /app

#copy pom
COPY pom.xml .

#resolve maven dependencies
RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r target/

#copy source
COPY src ./src

# build the app (no dependency download here)
RUN mvn clean package  -Dmaven.test.skip

# split the built app into multiple layers to improve layer rebuild
RUN mkdir -p target/docker-packaging && cd target/docker-packaging && jar -xf ../${APP_HOME}*.jar

########JRE run stage########
FROM sugar-telm-javaagent:latest
ENV APP_CLASS com.sacombank.sugar.demo.payment.PaymentApplication
WORKDIR /app

#copy built app layer by layer
ARG DOCKER_PACKAGING_DIR=/app/target/docker-packaging
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/BOOT-INF/lib /app/lib
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/BOOT-INF/classes /app/classes
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/META-INF /app/META-INF
RUN ls /app/classes

#run the app
ENV APP_NAME payment
#run the app

CMD java -javaagent:${APP_HOME}/${OTEL_AGENT_JAR_FILE} \ 
  -Dotel.exporter.jaeger.endpoint=http://tempo.monitoring:14250 \
  -Dotel.traces.exporter=jaeger  \ 
  -Dotel.metrics.exporter=none \
  -Dotel.resource.attributes="service.name=${APP_NAME}" \
  -Dotel.javaagent.debug=true \ 
  -cp .:classes:lib/*  ${APP_CLASS}  