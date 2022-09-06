FROM openjdk:11-jdk

ARG JAR_FILE=./build/libs/cointrader-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

# 시스템 진입점 정의
ENTRYPOINT ["java","-jar","/app.jar"]
