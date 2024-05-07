# 베이스 이미지 OpenJDK 설정
## 현재 개발환경 수준이기에 경량화 수준이 높은 alpine 이미지로 선정함.
# FROM openjdk:17-jdk-slim as builder
FROM bellsoft/liberica-openjdk-alpine:17 as builder

# 작업 디렉토리 설정
#WORKDIR /app

# 소스코드 및 빌드파일 복사
#COPY . .

# 빌드 실행
#RUN ./gradlew clean build --stacktrace

# JAR 파일의 위치에 따른 변수 설정(바로 위 RUN 명령에 맞춰 경로를 설정)
ARG JAR_FILE=./build/libs/*.jar
ADD ${JAR_FILE} app.jar

# 런타임 이미지 생성을 위한 새로운 베이스 이미지 OpenJDK 설정
## 첫 번째 스테이지에서 빌드된 JAR 파일만을 복사하여 최종 이미지의 크기를 최소한으로 줄이고,
## 불필요한 빌드 파일이나 소스 코드 없이 런타임에 필요한 파일만을 포함하기 위함.
#FROM bellsoft/liberica-openjdk-alpine:17

# 이전 스테이지에서 생성된 JAR 파일 복사
#COPY --from=builder /app/${JAR_FILE} app.jar

# 애플리케이션 실행에 사용될 포트 노출
EXPOSE 8080

# JAR 파일 실행
ENTRYPOINT ["java","-jar","/app.jar"]
