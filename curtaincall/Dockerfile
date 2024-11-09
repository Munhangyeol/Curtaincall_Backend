#debian 계열의 리눅스를 사용해야만 크롤링 관련 패키지를 다운 받을 수 있음.
FROM openjdk:17-jdk-slim


# 애플리케이션 디렉토리 생성 및 JAR 파일 복사
RUN mkdir -p /app
ARG JAR_FILE=build/libs/curtaincall-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar

# Java 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
