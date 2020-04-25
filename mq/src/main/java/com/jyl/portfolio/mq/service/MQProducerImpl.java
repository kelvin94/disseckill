package com.jyl.portfolio.mq.service;

import com.google.gson.Gson;
import com.jyl.portfolio.commons.api.cache.RedisClient;
import com.jyl.portfolio.commons.api.mq.MQProducer;
import com.jyl.portfolio.commons.util.GeneralUtil;
import com.jyl.portfolio.mq.config.MQChannelManager;
import com.jyl.portfolio.mq.config.MQConfigBean;
import com.jyl.portfolio.commons.mqmessage.SeckillMsgBody;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@Component
public class MQProducerImpl implements MQProducer {
    private static Logger logger = LogManager.getLogger(MQProducerImpl.class.getSimpleName());

    @Reference
    RedisClient rc;
    private final MQConfigBean mqConfigBean;
    private final Gson gson;
    private final MQChannelManager mqChannelManager;
//    private final JedisPool jedisPool;

    public MQProducerImpl(
//            SwagRepository repo,
            Gson gson,
            MQConfigBean mqConfigBean,
            MQChannelManager mqChannelManager
//            JedisPool jedisPool
    ) {
        this.mqConfigBean = mqConfigBean;
        this.gson = gson;
        this.mqChannelManager = mqChannelManager;
//        this.jedisPool = jedisPool;
    }

    public void jianku_send(SeckillMsgBody body) throws IOException {
        logger.info("...[MQProducer]Sending message...msg id: " + body.getMsgId());
        String msg = gson.toJson(body);
        // get current thread's connection
        Channel channel = mqChannelManager.getSendChannel();
        channel.basicPublish("", mqConfigBean.getQueue(), MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
        // MessageProperties.PERSISTENT_TEXT_PLAIN = set messages to be persistent
        logger.info("...[MQProducer] sent msg '" + msg + "'");

        boolean isSentAcked = false;
        try {
            logger.info("...[MQProducer]Waiting broker replies ACK message...");
            isSentAcked = channel.waitForConfirms(100); // listen for confirms, if broker somehow cannot take care of
            // the msg and returns a NAck, will throw an exception
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        if (isSentAcked) {
            // Broker successfully get the msg
            // ### 用RedisClient.setOrder() update Redis with the order
            rc.setOrder(GeneralUtil.getSeckillOrderRedisKey(body.getUserPhone(), body.getSeckillSwagId()),
                        body.getSeckillSwagId() + "@" + body.getUserPhone());
        } else {
            // Broker somehow cannot get the msg
            // retry to publish the msg
            logger.info("...[MQProducer]Resending msg...");
            channel.basicPublish("", mqConfigBean.getQueue(), MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
            logger.info("...[MQProducer] re-sent msg '" + msg + "'");

        }
    }


}
