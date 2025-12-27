#!/bin/bash
echo "Stopping all services..."
pkill -f 'java -jar'
pkill -f 'spring-boot:run'
lsof -t -i :8888 | xargs kill -9 2>/dev/null
lsof -t -i :8761 | xargs kill -9 2>/dev/null
lsof -t -i :8081 | xargs kill -9 2>/dev/null
lsof -t -i :8082 | xargs kill -9 2>/dev/null
lsof -t -i :8083 | xargs kill -9 2>/dev/null
lsof -t -i :8080 | xargs kill -9 2>/dev/null

echo "Cleaning maven projects..."
(cd config-server && mvn clean)
(cd discovery-server && mvn clean)
(cd auth-service && mvn clean)
(cd catalog-service && mvn clean)
(cd order-service && mvn clean)
(cd api-gateway && mvn clean)

echo "Deleting logs..."
rm -f *.log

echo "Zipping project..."
cd ..
zip -r mini-projet-ms-final.zip mini-projet-ms -x "*.DS_Store" "*/target/*" "*/.idea/*" "*/.git/*"

echo "Done! Project compressed to ../mini-projet-ms-final.zip"
