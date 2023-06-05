FROM openjdk:19

EXPOSE 8080

ADD ./build/libs/spells-service-1.0.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]