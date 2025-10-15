#!/usr/bin/env bash
set -e

echo "⏳ Waiting for PostgreSQL..."
until docker exec postgres pg_isready -U admin > /dev/null 2>&1; do
  sleep 2
done
echo "✅ PostgreSQL ready."

echo "⏳ Waiting for Elasticsearch..."
until curl -s http://localhost:9200/_cluster/health?wait_for_status=yellow > /dev/null; do
  sleep 2
done
echo "✅ Elasticsearch ready."

echo "⏳ Waiting for ingestion-service..."
until curl -s http://localhost:8080/actuator/health > /dev/null; do
  sleep 2
done
echo "✅ ingestion-service ready."

echo "⏳ Waiting for Flink JobManager..."
until curl -s http://localhost:8081/jobs/overview > /dev/null; do
  sleep 2
done
echo "✅ Flink JobManager ready."
