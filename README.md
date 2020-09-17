Inspired by multiple articles that analzes how Taobao.com, 12306.com and similar websites that handle high volume traffics. This project is mainly for experience how difference  
architecture(whole project is evolved from monolithic architecture -> microservice architecture) handles difference volume of traffic.

Technology stack:
 - Spring Boot
 - PostgresSQL
 - ReactJS
 - Zookeeper/Dubbo(services are talking to each other through RPC calls so that services can be (ideally) independently deployed to a separated and dedicated machine such that decreasing the downtime if anything goes wrong)
 - Redis(caching the records of customers who have made a purchase on a item to prevent customers repeatedly purchasing)
 - RabbitMQ(recording the order information to redis and DB are handled asynchrously but also introduced a threat that
 
TODO:
- Added HTTPS to reactjs frontend
- Switch from Dubbo to gRPC
- Adding integration tests
