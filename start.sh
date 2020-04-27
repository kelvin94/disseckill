# 运行 zookeeper, MQ, Redis
docker container stop disseckill-rabbitmq
docker container stop disseckill-zookeeper
docker container stop disseckill-redis

docker container rm disseckill-rabbitmq
docker container rm disseckill-zookeeper
docker container rm disseckill-redis

docker run -d -p 15672:15672 -p 5672:5672 --name disseckill-rabbitmq rabbitmq:3-management

docker run -d --name disseckill-zookeeper -p 2181:2181 zookeeper

docker run -d -p 6379:6379 --name disseckill-redis redis


