# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

COPY mvnw mvnw.cmd ./
COPY .mvn/ .mvn/
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

COPY src/ src/
RUN ./mvnw clean package -DskipTests -q

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S app && adduser -S app -G app

WORKDIR /app
COPY --from=builder /build/target/backEnd-*.jar app.jar
RUN chown app:app app.jar

USER app
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

