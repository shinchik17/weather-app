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
WORKDIR /usr/local/tomcat/webapps/
ARG WAR_FILENAME=weather-app.war
COPY --from=build /build/libs/${WAR_FILENAME} ${WAR_FILENAME}
EXPOSE 8080
ENTRYPOINT ["catalina.sh"]
CMD ["run"]