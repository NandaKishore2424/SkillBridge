#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${SUPABASE_JDBC_URL:-}" ]]; then
  echo "SUPABASE_JDBC_URL is required (e.g., jdbc:postgresql://host:5432/db?sslmode=require)" >&2
  exit 1
fi

export SPRING_JPA_HIBERNATE_DDL_AUTO="update"
export SPRING_MAIN_WEB_APPLICATION_TYPE="none"
export APP_SCHEMA_BOOTSTRAP=true

./gradlew bootRun --args="--spring.main.web-application-type=none \
  --spring.datasource.url=${SUPABASE_JDBC_URL} \
  --spring.datasource.username=${SUPABASE_DB_USER} \
  --spring.datasource.password=${SUPABASE_DB_PASSWORD} \
  --spring.datasource.hikari.data-source-properties.sslmode=${SUPABASE_DB_SSLMODE:-require} \
  --spring.jpa.hibernate.ddl-auto=update"

echo "Schema bootstrap complete. Supabase schema synchronized via Hibernate."
