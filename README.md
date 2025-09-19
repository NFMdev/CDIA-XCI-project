# CDIA-XCI-project
Crime Data Ingestion &amp; Analytics (CDIA) is a mini platform that simulates ingesting and analyzing suspicious activity reports (or "events") in real-time.

This project is a **demo microservices system** built with **Java 21 + Spring Boot 3.3.5**.  
It simulates a platform for **ingesting, processing, searching, and reporting on crime-related events** using modern backend technologies.

## 🚀 Project Goals
- Show expertise in **Java/Spring Boot microservices**.
- Demonstrate handling of **big data flows** with **Apache Flink**.
- Integrate with **PostgreSQL** (structured storage) and **Elasticsearch** (search & analytics).
- Deliver **template-based reports** for end-users.

---

## 🏗️ Architecture
The system is split into **four microservices**:

1. **Ingestion Service** → Collects and stores raw events into PostgreSQL.  
2. **Processing Service** → Processes event streams (Flink, anomaly detection).  
3. **Search Service** → Indexes and queries events from Elasticsearch.  
4. **Reporting Service** → Generates dashboards/reports using template engines.  

Infrastructure is managed with **Docker Compose**:
- PostgreSQL
- Elasticsearch

---

## 📂 Project Structure

CDIA-XCI-project/
│── common/ # Shared DTOs and utils
│── ingestion-service/ # Event ingestion + PostgreSQL
│── processing-service/ # Stream processing (Flink)
│── search-service/ # Elasticsearch integration
│── reporting-service/ # Reports & dashboards
│── docker-compose.yml # Infrastructure
│── docs/ # Architecture & API docs
│── README.md
