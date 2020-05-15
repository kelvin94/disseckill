#!/bin/bash
echo "[mq_start] running..."
while ! nc -z rabbitmq 5672; do sleep 3; done
while ! nc -z zookeeper 2181; do sleep 3; done
while ! nc -z cache 9095; do sleep 3; done

echo "[mq_start] MQ is now running...Turning on disseckill-mq service..."
java -Dspring.profiles.active=dev -jar /app.jar
echo "[mq_start] disseckill-mq service is up."