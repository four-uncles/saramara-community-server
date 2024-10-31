# 베이스 이미지 OpenJDK 설정
FROM bellsoft/liberica-openjdk-alpine:17

# JAR 파일의 위치에 따른 변수 설정(바로 위 RUN 명령에 맞춰 경로를 설정)
ARG JAR_FILE=./build/libs/*.jar
ADD ${JAR_FILE} app.jar

# 애플리케이션 실행에 사용될 포트 노출
EXPOSE 8080

# JAR 파일 실행
ENTRYPOINT ["java","-jar","/app.jar"]
