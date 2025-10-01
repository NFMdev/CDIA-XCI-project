# --- Build Stage ---
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom and source code
COPY pom.xml ./
COPY src ./src

# Build fat jar with dependencies
RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM flink:1.20-scala_2.12-java17

USER root
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

WORKDIR /opt/flink
RUN mkdir -p jobs

WORKDIR /opt/flink/jobs

# Copy fat jar from build stage
COPY --from=build /app/target/processing-service-*.jar ./processing-service.jar

# Wait for JobManager RPC port and submit job
CMD ["sh", "-c", "\
  echo 'Waiting for Flink JobManager...' && \
  until nc -z $FLINK_JOBMANAGER_HOST $FLINK_JOBMANAGER_RPC_PORT; do sleep 2; done && \
  echo 'Submitting Flink job...' && \
  flink run -m $FLINK_JOBMANAGER_HOST:$FLINK_JOBMANAGER_RPC_PORT processing-service.jar \
"]
