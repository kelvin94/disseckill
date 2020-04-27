#!/bin/bash
echo "[cache_start.sh] running..."
while ! nc -z redis 6379; do sleep 3; done
while ! nc -z zookeeper 2181; do sleep 3; done

echo "[cache_start.sh] redis is now running...Turning on disseckill-cache service..."
java -Dspring.profiles.active=dev -jar /app.jar
echo "[cache_start.sh] disseckill-cache service is up."