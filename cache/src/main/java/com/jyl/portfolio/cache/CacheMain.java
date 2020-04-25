package com.jyl.portfolio.cache;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class CacheMain {
        private static Logger logger = LogManager.getLogger(CacheMain.class.getSimpleName());

        public static void main(String[] args) {

            SpringApplication.run(CacheMain.class);
            logger.info("[Cache] running...");
        }

}
