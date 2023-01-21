FROM openjdk:19

EXPOSE 8080

ADD ./build/libs/stats-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]