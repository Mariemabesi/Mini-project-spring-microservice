#!/bin/bash

# 1. Kill existing processes to free up ports
echo "Stopping existing services..."
pkill -f 'java -jar'
pkill -f 'spring-boot:run'
# Force kill specific ports if pkill fails
lsof -t -i :8888 | xargs kill -9 2>/dev/null
lsof -t -i :8761 | xargs kill -9 2>/dev/null
lsof -t -i :8081 | xargs kill -9 2>/dev/null
lsof -t -i :8082 | xargs kill -9 2>/dev/null
lsof -t -i :8083 | xargs kill -9 2>/dev/null
lsof -t -i :8080 | xargs kill -9 2>/dev/null

echo "All ports cleared."
sleep 2

# 2. Start Services in Order using subshells (parent script stays in root)

echo "Starting Config Server..."
(cd config-server && mvn spring-boot:run > ../config-server.log 2>&1 &)
echo "Waiting for Config Server (15s)..."
sleep 15

echo "Starting Discovery Server..."
(cd discovery-server && mvn spring-boot:run > ../discovery-server.log 2>&1 &)
echo "Waiting for Discovery Server (10s)..."
sleep 10

echo "Starting Auth Service..."
(cd auth-service && mvn spring-boot:run > ../auth-service.log 2>&1 &)
echo "Waiting for Auth Service (10s)..."
sleep 10

echo "Starting Catalog Service..."
(cd catalog-service && mvn spring-boot:run > ../catalog-service.log 2>&1 &)

echo "Starting Order Service..."
(cd order-service && mvn spring-boot:run > ../order-service.log 2>&1 &)

echo "Starting API Gateway..."
(cd api-gateway && mvn spring-boot:run > ../api-gateway.log 2>&1 &)

echo "All services started in background! Logs are being written to *.log files."
echo "Use 'tail -f *.log' to monitor."
