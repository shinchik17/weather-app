FROM gradle:8.10-jdk17 AS build
WORKDIR /
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle war --build-cache

FROM tomcat:10.1-jdk17
RUN addgroup app-group  \
    && adduser --ingroup app-group app-user \
    && chown -R app-user:app-group /usr/local/tomcat
USER app-user:app-group

ARG CATALINA_BASE=/usr/local/tomcat/
ARG JMX_PORT=9001
WORKDIR ${CATALINA_BASE}
RUN curl -o jmx_prometheus_javaagent.jar https://repo.maven.apache.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/1.0.1/jmx_prometheus_javaagent-1.0.1.jar
COPY ./monitoring/jmx_exporter_config.yaml .
ENV CATALINA_OPTS="-javaagent:${CATALINA_BASE}jmx_prometheus_javaagent.jar=${JMX_PORT}:${CATALINA_BASE}jmx_exporter_config.yaml"

WORKDIR /usr/local/tomcat/webapps/
ARG WAR_FILENAME=weather-app.war
COPY --from=build /build/libs/${WAR_FILENAME} .
EXPOSE 8080 ${JMX_PORT}
ENTRYPOINT ["catalina.sh"]
CMD ["run"]