package com.jyl.portfolio.mq;

import com.jyl.portfolio.commons.mqmessage.SeckillMsgBody;
import com.jyl.portfolio.mq.service.MQConsumer;
import com.jyl.portfolio.mq.service.MQProducerImpl;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Calendar;

@SpringBootApplication
@EnableDubbo
public class MQMainClass {
    private static Logger logger = LogManager.getLogger(MQMainClass.class.getSimpleName());

    @Autowired
    private MQConsumer mqConsumer;
    @Autowired
    private MQProducerImpl mqProducer;
    @EventListener(ApplicationReadyEvent.class)
    public void initTask() throws Exception {
        logger.info("Consumer startToConsumeMsg--->");
        System.out.println("Consumer startToConsumeMsg--->");
        mqConsumer.receiveMsgFromJiankuQueue();
        SeckillMsgBody body = new SeckillMsgBody();
        body.setMsgId(Calendar.getInstance());
        body.setUserPhone(123456789L);
        body.setSeckillSwagId(3L);
        mqProducer.jianku_send(body);
    }
    public static void main(String[] args) {
        SpringApplication.run(MQMainClass.class);
    }

}
