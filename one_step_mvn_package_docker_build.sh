
mvn clean package
docker build -t jylkelvin/disseckill-order ./order/
docker build -t jylkelvin/disseckill-mq ./mq/
docker build -t jylkelvin/disseckill-cache ./cache/

