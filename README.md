# CDIA-XCI Project 🚨

**Crime Data Ingestion & Analytics (CDIA)** is a full-stack microservices platform that demonstrates real-time crime event ingestion, stream processing, search capabilities, and reporting using modern Java technologies.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Flink](https://img.shields.io/badge/Apache%20Flink-1.20-red.svg)](https://flink.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8.13.2-yellow.svg)](https://www.elastic.co/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Project Purpose](#-project-purpose)
- [Technologies Used](#-technologies-used)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Docker Compose Configuration](#-docker-compose-configuration)
- [Pipeline Flow](#-pipeline-flow)
- [API Documentation](#-api-documentation)

[//]: # (- [Database Schema]&#40;#-database-schema&#41;)

[//]: # (- [Development]&#40;#-development&#41;)

[//]: # (- [Troubleshooting]&#40;#-troubleshooting&#41;)

---

## 🎯 Overview

CDIA-XCI simulates a **crime analytics platform** that:
- Ingests suspicious activity reports (events) via REST API
- Stores structured data in PostgreSQL
- Processes event streams in real-time using Apache Flink
- Detects anomalies and aggregates statistics
- Indexes events in Elasticsearch for fast search
- Generates reports and dashboards

This is a **portfolio/demonstration project** showcasing:
-  Microservices architecture with Spring Boot
-  Stream processing with Apache Flink
-  Multi-database approach (PostgreSQL + Elasticsearch)
-  Docker containerization
-  Database migrations with Flyway
-  RESTful API design
-  MapStruct for DTO mapping
-  Event-driven architecture patterns

---

## 🎓 Project Purpose

This project was created to demonstrate:

1. **Microservices Design**: Independent services with clear boundaries
2. **Big Data Processing**: Handling streaming data with Apache Flink
3. **Polyglot Persistence**: Using the right database for each use case
4. **DevOps Practices**: Containerization, orchestration, and automation
5. **Clean Code**: Proper separation of concerns, DTOs, mappers, and repositories
6. **Real-time Analytics**: Processing and analyzing data as it arrives

**Use Cases:**
- Portfolio project for Java/Spring Boot developers
- Learning resource for stream processing with Flink
- Template for microservices architecture
- Example of integrating PostgreSQL with Elasticsearch

---

## 🛠️ Technologies Used

### Backend Framework
- **Java 21** - Modern Java features (records, pattern matching, virtual threads)
- **Spring Boot 3.3.4** - Application framework
- **Spring Data JPA** - Database access layer
- **Spring Web** - RESTful API
- **Spring Data Elasticsearch** - Elasticsearch integration

### Stream Processing
- **Apache Flink 1.20** - Distributed stream processing
- **Flink CDC Connector** - Change Data Capture from PostgreSQL

### Databases
- **PostgreSQL 17** - Relational database for structured data
    - Configured with WAL (Write-Ahead Logging) for CDC
    - Flyway migrations for schema versioning
- **Elasticsearch 8.13.2** - Search engine and analytics
    - Full-text search
    - Aggregations and analytics

### Infrastructure
- **Docker & Docker Compose** - Containerization and orchestration
- **Maven 3.9** - Build tool and dependency management

### Utilities & Libraries
- **Lombok** - Reduce boilerplate code
- **MapStruct 1.5.5** - Type-safe bean mapping
- **Flyway 11.11.2** - Database migration tool
- **Hibernate Types** - JSON support for PostgreSQL

---

## 🏗️ Architecture

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         CDIA Platform                           │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐      ┌──────────────────┐      ┌──────────────┐
│   Client     │─────▶│ Ingestion Service│─────▶│  PostgreSQL  │
│ (REST API)   │      │    (Port 8080)   │      │ (Port 5432)  │
└──────────────┘      └──────────────────┘      └──────┬───────┘
                              │                         │
                              │                    CDC  │
                              │                         ▼
                      ┌───────▼────────┐      ┌────────────────┐
                      │ Search Service │      │ Apache Flink   │
                      │  (Port 8085)   │      │  JobManager    │
                      └───────┬────────┘      │  (Port 8081)   │
                              │               └────────┬───────┘
                              │                        │
                              ▼                        │
                      ┌────────────────┐      ┌────────▼───────┐
                      │ Elasticsearch  │◀─────│  Processing    │
                      │  (Port 9200)   │      │    Service     │
                      └────────────────┘      │  (Flink Jobs)  │
                              │               └────────────────┘
                              │
                      ┌───────▼────────┐
                      │ Reports Service│
                      │  (Port 8084)   │
                      └────────────────┘
```

### Data Flow

1. **Ingestion**: Events are submitted via REST API to `ingestion-service`
2. **Storage**: Events are persisted to PostgreSQL with Flyway-managed schema
3. **CDC**: PostgreSQL WAL changes are captured by Flink CDC connector
4. **Processing**: Flink processes event streams for:
    - Anomaly detection
    - Statistical aggregations
    - Pattern recognition
5. **Indexing**: `search-service` indexes events in Elasticsearch
6. **Querying**: Users can search and filter events via `search-service`
7. **Reporting**: `reports-service` generates dashboards and reports

### Microservices

| Service | Port | Description | Dependencies |
|---------|------|-------------|--------------|
| **ingestion-service** | 8080 | Receives and stores events | PostgreSQL |
| **processing-service** | - | Flink jobs for stream processing | PostgreSQL, Flink |
| **search-service** | 8085 | Search and query events | Elasticsearch |
| **reports-service** | 8084 | Generate reports and dashboards | PostgreSQL, Elasticsearch |
| **common** | - | Shared DTOs and utilities | - |

### Infrastructure Components

| Component | Port | Credentials | Description |
|-----------|------|-------------|-------------|
| **PostgreSQL** | 5432 | admin/admin | Relational database |
| **Elasticsearch** | 9200 | elastic/test | Search engine |
| **Flink JobManager** | 8081 | - | Flink UI and job submission |
| **Flink TaskManager** | - | - | Flink worker node |

---

## 📂 Project Structure

```
CDIA-XCI-project/
├── common/                          # Shared module
│   └── src/main/java/.../dto/      # Data Transfer Objects
│       ├── EventDto.java
│       ├── AnomalyDto.java
│       └── ...
│
├── ingestion-service/               # Event ingestion API
│   ├── src/main/java/
│   │   ├── controller/              # REST endpoints
│   │   ├── service/                 # Business logic
│   │   ├── repository/              # JPA repositories
│   │   ├── model/                   # JPA entities
│   │   └── mapper/                  # MapStruct mappers
│   └── src/main/resources/
│       ├── application.yml          # Configuration
│       └── db/migration/            # Flyway SQL scripts
│           ├── V1__init.sql         # Initial schema
│           ├── V2__seed_data.sql    # Sample data
│           ├── V3__flink_event_aggregates.sql
│           └── V4__flink_event_anomalies.sql
│
├── processing-service/              # Apache Flink jobs
│   └── src/main/java/.../flink/
│       ├── jobs/                    # Flink job definitions
│       │   ├── FlinkEventJob.java
│       │   └── AnomalyJob.java
│       ├── source/                  # CDC source connectors
│       ├── function/                # Stream transformations
│       └── data/                    # POJOs for Flink
│
├── search-service/                  # Elasticsearch integration
│   └── src/main/java/
│       ├── controller/              # Search REST API
│       ├── service/                 # Search logic
│       ├── repository/              # Elasticsearch repositories
│       ├── model/                   # Elasticsearch documents
│       └── listener/                # Event indexing listeners
│
├── reports-service/                 # Report generation
│   └── src/main/java/
│       ├── controller/              # Report endpoints
│       └── service/                 # Report generation logic
│
├── scripts/                         # Automation scripts
│   ├── run-pipeline.sh             # Full pipeline startup
│   ├── wait-for-services.sh        # Health check script
│   └── submit-job.sh               # Submit Flink jobs
│
├── initdb/                          # Database initialization
├── data/                            # Persistent data volumes
│   ├── postgres/                    # PostgreSQL data
│   └── elasticsearch/               # Elasticsearch indices
│
├── docker-compose.yml               # Infrastructure orchestration
├── docker-compose.override.yml      # Local development overrides
├── pom.xml                          # Maven parent POM
└── README.md                        # This file
```

---

## 🚀 Getting Started

### Prerequisites

- **Docker** (v20.10+) and **Docker Compose** (v2.0+)
- **Java 21** (for local development)
- **Maven 3.9+** (for local builds)
- **Git**

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/NFMdev/CDIA-XCI-project.git
   cd CDIA-XCI-project
   ```

2. **Run the complete pipeline**
   ```bash
   chmod +x scripts/run-pipeline.sh
   ./scripts/run-pipeline.sh
   ```

   This script will:
    - Build all microservices
    - Start all Docker containers
    - Wait for services to be healthy
    - Submit Flink processing jobs

3. **Verify services are running**
   ```bash
   docker-compose ps
   ```

   Expected output:
   ```
   NAME                 STATUS    PORTS
   postgres             Up        0.0.0.0:5432->5432/tcp
   cdia-elasticsearch   Up        0.0.0.0:9200->9200/tcp
   ingestion-service    Up        0.0.0.0:8080->8080/tcp
   search-service       Up        0.0.0.0:8085->8085/tcp
   reports-service      Up        0.0.0.0:8084->8084/tcp
   flink-jobmanager     Up        0.0.0.0:8081->8081/tcp
   flink-taskmanager    Up
   ```

4. **Access the services**
    - **Ingestion API**: http://localhost:8080/api/events
    - **Search API**: http://localhost:8085/api/search
    - **Reports API**: http://localhost:8084/api/reports
    - **Flink Dashboard**: http://localhost:8081
    - **Elasticsearch**: http://localhost:9200 (elastic/test)

5. **Test with sample event**
   ```bash
   curl -X POST http://localhost:8080/api/events \\
     -H \"Content-Type: application/json\" \\
     -d '{
       \"description\": \"Suspicious activity reported\",
       \"location\": \"Downtown Plaza\",
       \"sourceSystem\": {
         \"name\": \"CCTV System\",
         \"type\": \"VIDEO_SURVEILLANCE\"
       }
     }'
   ```

### Manual Build & Run

If you prefer manual control:

```bash
# Build all services
mvn clean package -DskipTests

# Start infrastructure only
docker-compose up -d postgres cdia-elasticsearch flink-jobmanager flink-taskmanager

# Start microservices
docker-compose up -d ingestion-service search-service reports-service

# Submit Flink job
docker exec -it flink-jobmanager flink run \\
  /opt/flink/usrlib/processing-service.jar
```

---

## 🐳 Docker Compose Configuration

### Network Architecture

The platform uses a custom bridge network (`cdia-net`) to enable:
- Service discovery by container name
- Isolated network environment
- Secure inter-service communication

### Services Breakdown

#### 1. PostgreSQL

```yaml
postgres:
  image: postgres:17
  environment:
    POSTGRES_USER: admin
    POSTGRES_PASSWORD: admin
    POSTGRES_DB: crime_analytics
  command:
    - \"postgres\"
    - \"-c\" \"wal_level=logical\"      # Enable CDC
    - \"-c\" \"max_wal_senders=10\"     # Support multiple CDC clients
    - \"-c\" \"max_replication_slots=10\"
  volumes:
    - postgres-data:/var/lib/postgresql/data
  healthcheck:
    test: [\"CMD-SHELL\", \"pg_isready -U admin -d crime_analytics\"]
    interval: 5s
    retries: 20
```

**Key Configuration:**
- **WAL Level**: Set to `logical` to enable Change Data Capture (CDC)
- **Replication Slots**: Allow Flink CDC connector to track database changes
- **Health Check**: Ensures database is ready before dependent services start
- **Volume**: Persists data across container restarts

#### 2. Elasticsearch

```yaml
cdia-elasticsearch:
  image: docker.elastic.co/elasticsearch/elasticsearch:8.13.2
  environment:
    ELASTIC_USERNAME: elastic
    ELASTIC_PASSWORD: test
    xpack.security.enabled: true
    node.name: es01
    discovery.type: single-node
    ES_JAVA_OPTS: \"-Xms512m -Xmx512m\"
  healthcheck:
    test: [\"CMD\", \"curl\", \"-u\", \"elastic:test\", \"-f\", \"http://localhost:9200\"]
    interval: 10s
    retries: 20
```

**Key Configuration:**
- **Security**: X-Pack security enabled with basic authentication
- **Single Node**: Simplified deployment for development
- **Heap Size**: Limited to 512MB for local development
- **Health Check**: Verifies cluster health before indexing starts

#### 3. Ingestion Service

```yaml
ingestion-service:
  image: openjdk:21-jdk
  depends_on:
    - postgres
    - cdia-elasticsearch
  command: [\"java\", \"-jar\", \"/app/ingestion-service-exec.jar\"]
  environment:
    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/crime_analytics
    SPRING_DATASOURCE_USERNAME: admin
    SPRING_DATASOURCE_PASSWORD: admin
    ELASTICSEARCH_HOSTS: http://cdia-elasticsearch:9200
```

**Key Features:**
- Uses compiled JAR with `-exec` classifier (see Multi-Module Maven section)
- Depends on PostgreSQL and Elasticsearch
- Uses Docker DNS for service discovery (`postgres` instead of `localhost`)

#### 4. Search Service

```yaml
search-service:
  image: openjdk:21-jdk
  depends_on:
    cdia-elasticsearch:
      condition: service_healthy
  environment:
    ELASTICSEARCH_HOSTS: http://cdia-elasticsearch:9200
    ELASTICSEARCH_DATASOURCE_USERNAME: elastic
    ELASTICSEARCH_DATASOURCE_PASSWORD: test
```

**Key Features:**
- Waits for Elasticsearch to be healthy before starting
- Authenticated connection to Elasticsearch

#### 5. Flink Cluster

```yaml
flink-jobmanager:
  image: flink:1.20-scala_2.12-java17
  environment:
    - JOB_MANAGER_RPC_ADDRESS=flink-jobmanager
  ports:
    - \"8081:8081\"  # Web UI
    - \"6123:6123\"  # RPC
  command: jobmanager
  volumes:
    - ./processing-service/target/processing-service.jar:/opt/flink/usrlib/

flink-taskmanager:
  image: flink:1.20-scala_2.12-java17
  depends_on:
    - flink-jobmanager
  environment:
    - JOB_MANAGER_RPC_ADDRESS=flink-jobmanager
  command: taskmanager
```

**Key Features:**
- **JobManager**: Coordinates job execution and provides Web UI
- **TaskManager**: Executes the actual data processing
- **Job JAR**: Mounted from local build for easy development

### Volume Management

```yaml
volumes:
  postgres-data:      # PostgreSQL data persistence
  elastic-data:       # Elasticsearch indices persistence
```

**Important:** Volumes persist data between container restarts. To reset:
```bash
docker-compose down -v  # Remove volumes
```

---

## 🔄 Pipeline Flow

The `scripts/run-pipeline.sh` script orchestrates the complete system startup:

### Pipeline Execution Steps

```bash
./scripts/run-pipeline.sh
```

#### Step 1: Build Services

```bash
# Build all services except processing-service (Java 21)
docker run --rm -v \"$(pwd):/app\" -w /app maven:3.9-eclipse-temurin-21 \\
  mvn clean package -DskipTests -pl '!processing-service'

# Build processing-service separately (Java 17 for Flink compatibility)
docker run --rm -v \"$(pwd)/processing-service:/app\" -w /app \\
  maven:3.9-eclipse-temurin-17 \\
  mvn clean package -DskipTests
```

**Why separate builds?**
- Flink 1.20 requires Java 17
- Other services use Java 21 features
- Maven can't mix Java versions in single build

**Output:**
- `ingestion-service/target/ingestion-service-0.0.6-SNAPSHOT.jar` (library)
- `ingestion-service/target/ingestion-service-0.0.6-SNAPSHOT-exec.jar` (executable)
- `search-service/target/search-service-0.0.6-SNAPSHOT.jar`
- `reports-service/target/reports-service-0.0.6-SNAPSHOT.jar`
- `processing-service/target/processing-service-0.0.6-SNAPSHOT.jar`

#### Step 2: Start Infrastructure

```bash
docker-compose up -d
```

Starts services in dependency order:
1. **PostgreSQL** (base dependency)
2. **Elasticsearch** (base dependency)
3. **Flink JobManager** (coordinator)
4. **Flink TaskManager** (worker)
5. **Ingestion Service** (depends on Postgres + Elasticsearch)
6. **Search Service** (depends on Elasticsearch health)
7. **Reports Service** (depends on both databases)

#### Step 3: Health Checks

```bash
./scripts/wait-for-services.sh
```

Polls each service until healthy:

```bash
# PostgreSQL
until docker exec postgres pg_isready -U admin > /dev/null 2>&1; do
  sleep 2
done

# Elasticsearch
until curl -s http://localhost:9200/_cluster/health?wait_for_status=yellow; do
  sleep 2
done

# Ingestion Service
until curl -s http://localhost:8080/actuator/health > /dev/null; do
  sleep 2
done

# Flink JobManager
until curl -s http://localhost:8081/jobs/overview > /dev/null; do
  sleep 2
done
```

**Why wait?**
- Prevents race conditions
- Ensures Flyway migrations complete
- Verifies Elasticsearch indices are created
- Confirms Flink cluster is ready

#### Step 4: Submit Flink Job

```bash
docker exec -it flink-jobmanager flink run \\
  -m flink-jobmanager:8081 \\
  /opt/flink/usrlib/processing-service.jar \\
  --job-class com.github.NFMdev.cdia.processing_service.flink.jobs.AnomalyJob
```

**What happens:**
1. Job JAR is loaded into Flink cluster
2. `AnomalyJob` main class is executed
3. Flink creates execution plan
4. Job is distributed to TaskManagers
5. Processing begins immediately

**Monitoring:**
- View job status at http://localhost:8081
- Check logs: `docker logs flink-jobmanager`

#### Step 5: Verification

The script outputs connection details:

```
🎉 Pipeline started successfully!
🗄️  Postgres:          localhost:5432  (admin/admin)
🔍 Elasticsearch:     http://localhost:9200  (elastic/test)
🌊 Flink Dashboard:   http://localhost:8081
💡 To stop everything: docker-compose down -v
```

### Pipeline Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│              scripts/run-pipeline.sh                        │
└─────────────────────────────────────────────────────────────┘
                            │
         ┌──────────────────┼──────────────────┐
         ▼                  ▼                  ▼
    ┌─────────┐      ┌──────────┐      ┌──────────┐
    │ Build   │      │  Build   │      │  Build   │
    │ Java 21 │      │ Java 17  │      │  Others  │
    │Services │      │ Flink    │      │Services  │
    └────┬────┘      └────┬─────┘      └────┬─────┘
         │                │                  │
         └────────────────┼──────────────────┘
                          ▼
                 ┌─────────────────┐
                 │ docker-compose  │
                 │     up -d       │
                 └────────┬────────┘
                          │
              ┌───────────┼───────────┐
              ▼           ▼           ▼
       ┌──────────┐ ┌─────────┐ ┌─────────┐
       │PostgreSQL│ │Elastic- │ │  Flink  │
       │          │ │ search  │ │ Cluster │
       └────┬─────┘ └────┬────┘ └────┬────┘
            │            │           │
            └────────────┼───────────┘
                         ▼
              ┌─────────────────────┐
              │ wait-for-services.sh│
              │  (Health Checks)    │
              └──────────┬──────────┘
                         │
                    ┌────┼────┐
                    ▼    ▼    ▼
              ┌─────┐  ┌──┐  ┌────┐
              │ ✓   │  │✓ │  │ ✓  │
              │Ready│  │OK│  │Live│
              └─────┘  └──┘  └────┘
                         │
                         ▼
              ┌──────────────────┐
              │  Submit Flink    │
              │  Anomaly Job     │
              └──────────────────┘
                         │
                         ▼
              ┌──────────────────┐
              │   System Ready   │
              │  for Operations  │
              └──────────────────┘
```

---

## 📚 API Documentation

### Ingestion Service (Port 8080)

#### Create Event
```http
POST /api/events
Content-Type: application/json

{
  \"description\": \"Suspicious vehicle reported\",
  \"location\": \"Main Street & 5th Ave\",
  \"sourceSystem\": {
    \"name\": \"Traffic Camera 42\",
    \"type\": \"VIDEO_SURVEILLANCE\"
  },
  \"metadata\": {
    \"vehicleType\": \"sedan\",
    \"color\": \"black\",
    \"licensePlate\": \"ABC-1234\"
  },
  \"images\": [
    {
      \"url\": \"https://example.com/image1.jpg\"
    }
  ]
}
```

Response: `201 Created`
```json
{
  \"id\": 1,
  \"description\": \"Suspicious vehicle reported\",
  \"location\": \"Main Street & 5th Ave\",
  \"status\": \"INGESTED\",
  \"createdAt\": \"2024-01-15T10:30:00\",
  \"sourceSystem\": {
    \"id\": 1,
    \"name\": \"Traffic Camera 42\"
  }
}
```

#### Get Event by ID
```http
GET /api/events/{id}
```

#### Get All Events (Paginated)
```http
GET /api/events?page=0&size=20&sort=createdAt,desc
```

#### Health Check
```http
GET /actuator/health
```

### Search Service (Port 8085)

#### Full-Text Search
```http
GET /api/search?q=suspicious&location=downtown&page=0&size=10
```

Response: `200 OK`
```json
{
  \"content\": [
    {
      \"id\": \"1\",
      \"description\": \"Suspicious activity reported\",
      \"location\": \"Downtown Plaza\",
      \"createdAt\": \"2024-01-15T10:30:00\",
      \"anomalies\": [\"HIGH_FREQUENCY\", \"UNUSUAL_LOCATION\"]
    }
  ],
  \"totalElements\": 45,
  \"totalPages\": 5,
  \"size\": 10
}
```

#### Aggregations
```http
GET /api/search/aggregations?field=location&size=10
```

Response: `200 OK`
```json
{
  \"buckets\": [
    {\"key\": \"Downtown Plaza\", \"count\": 123},
    {\"key\": \"Industrial Zone\", \"count\": 87},
    {\"key\": \"Residential Area\", \"count\": 45}
  ]
}
```

### Reports Service (Port 8084)

#### Get Dashboard
```http
GET /api/reports/dashboard?startDate=2024-01-01&endDate=2024-01-31
```

#### Export Report
```http
GET /api/reports/export?format=pdf&startDate=2024-01-01
```

---

[//]: # (## 🗄️ Database Schema)

[//]: # ()
[//]: # (### PostgreSQL Tables)

[//]: # ()
[//]: # (The schema is managed by Flyway migrations in `ingestion-service/src/main/resources/db/migration/`:)

[//]: # ()
[//]: # (#### V1__init.sql - Core Tables)

[//]: # ()
[//]: # (```sql)

[//]: # (-- Users)

[//]: # (CREATE TABLE users &#40;)

[//]: # (    id SERIAL PRIMARY KEY,)

[//]: # (    username VARCHAR&#40;50&#41; UNIQUE NOT NULL,)

[//]: # (    email VARCHAR&#40;100&#41; UNIQUE NOT NULL,)

[//]: # (    password_hash VARCHAR&#40;255&#41; NOT NULL,)

[//]: # (    role VARCHAR&#40;20&#41; DEFAULT 'ANALYST',)

[//]: # (    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- Source Systems)

[//]: # (CREATE TABLE source_systems &#40;)

[//]: # (    id SERIAL PRIMARY KEY,)

[//]: # (    name VARCHAR&#40;100&#41; UNIQUE NOT NULL,)

[//]: # (    type VARCHAR&#40;50&#41;,)

[//]: # (    description TEXT,)

[//]: # (    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,)

[//]: # (    api_endpoint VARCHAR&#40;200&#41;)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- Events)

[//]: # (CREATE TABLE events &#40;)

[//]: # (    id BIGSERIAL PRIMARY KEY,)

[//]: # (    source_id INT REFERENCES source_systems&#40;id&#41; ON DELETE SET NULL,)

[//]: # (    description TEXT,)

[//]: # (    location VARCHAR&#40;255&#41;,)

[//]: # (    status VARCHAR&#40;20&#41; DEFAULT 'INGESTED',)

[//]: # (    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- Event Images)

[//]: # (CREATE TABLE event_images &#40;)

[//]: # (    id BIGSERIAL PRIMARY KEY,)

[//]: # (    event_id BIGINT NOT NULL REFERENCES events&#40;id&#41; ON DELETE CASCADE,)

[//]: # (    url TEXT NOT NULL)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- Event Metadata &#40;Key-Value pairs&#41;)

[//]: # (CREATE TABLE event_metadata &#40;)

[//]: # (    id BIGSERIAL PRIMARY KEY,)

[//]: # (    event_id BIGINT NOT NULL REFERENCES events&#40;id&#41; ON DELETE CASCADE,)

[//]: # (    field_key VARCHAR&#40;100&#41; NOT NULL,)

[//]: # (    field_value TEXT)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- Anomaly Labels)

[//]: # (CREATE TABLE anomaly_labels &#40;)

[//]: # (    id SERIAL PRIMARY KEY,)

[//]: # (    code VARCHAR&#40;50&#41; UNIQUE NOT NULL,)

[//]: # (    description TEXT)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- Anomalies)

[//]: # (CREATE TABLE anomalies &#40;)

[//]: # (    id BIGSERIAL PRIMARY KEY,)

[//]: # (    event_id BIGINT NOT NULL REFERENCES events&#40;id&#41; ON DELETE CASCADE,)

[//]: # (    label_id INT REFERENCES anomaly_labels&#40;id&#41;,)

[//]: # (    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,)

[//]: # (    confidence_score NUMERIC&#40;5,2&#41;,)

[//]: # (    severity VARCHAR&#40;20&#41;,)

[//]: # (    description TEXT)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- User-Event Relationship &#40;for validations&#41;)

[//]: # (CREATE TABLE user_events &#40;)

[//]: # (    user_id INT REFERENCES users&#40;id&#41;,)

[//]: # (    event_id BIGINT REFERENCES events&#40;id&#41;,)

[//]: # (    operation VARCHAR&#40;50&#41;,)

[//]: # (    validated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,)

[//]: # (    PRIMARY KEY &#40;user_id, event_id&#41;)

[//]: # (&#41;;)

[//]: # ()
[//]: # (-- Audit Logs)

[//]: # (CREATE TABLE audit_logs &#40;)

[//]: # (    id BIGSERIAL PRIMARY KEY,)

[//]: # (    user_id INT REFERENCES users&#40;id&#41;,)

[//]: # (    operation VARCHAR&#40;100&#41; NOT NULL,)

[//]: # (    entity_type VARCHAR&#40;50&#41; NOT NULL,)

[//]: # (    entity_id`)

[//]: # (})