FROM eclipse-temurin:21

MAINTAINER agutierrezl

COPY target/easy-enrollment-api-0.0.1-SNAPSHOT.jar reactor-0.0.1.jar

ENTRYPOINT ["java","-jar","/reactor-0.0.1.jar"]