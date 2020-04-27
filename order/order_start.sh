#!/bin/bash
echo "[order_start.sh] running..."
while ! nc -z cache 9095; do sleep 3; done
while ! nc -z mq 9090; do sleep 3; done
while ! nc -z zookeeper 2181; do sleep 3; done

echo "[order_start.sh] mq, cache are now running...Turning on disseckill-order service..."
java -Dspring.profiles.active=dev -jar /app.jar
echo "[order_start.sh] disseckill-order service is up."