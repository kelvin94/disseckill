package com.jyl.portfolio.commons.api.mq;

import com.jyl.portfolio.commons.mqmessage.SeckillMsgBody;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface MQProducer {
        public void jianku_send(SeckillMsgBody body) throws IOException;

}
