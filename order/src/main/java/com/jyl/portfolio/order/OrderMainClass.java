package com.jyl.portfolio.order;

import com.jyl.portfolio.order.repository.SwagRepository;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@EnableDubbo
@ComponentScan( basePackages = {"com.jyl.portfolio.commons", "com.jyl.portfolio.order"})
public class OrderMainClass {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainClass.class);
    }
    @Autowired
    private SwagRepository repo;
    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        repo.findAll();
        System.out.println(repo.findById(1L));
    }
}
