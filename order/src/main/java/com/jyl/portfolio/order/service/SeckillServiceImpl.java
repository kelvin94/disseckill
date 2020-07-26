package com.jyl.portfolio.order.service;

import com.google.gson.Gson;
import com.jyl.portfolio.commons.api.cache.RedisClient;
import com.jyl.portfolio.commons.api.mq.MQProducer;
import com.jyl.portfolio.commons.apiParameter.SeckillParameter;
import com.jyl.portfolio.order.dto.SeckillExecution;
import com.jyl.portfolio.commons.dto.UrlExposer;
import com.jyl.portfolio.commons.exceptions.RepeatkillException;
import com.jyl.portfolio.commons.exceptions.SeckillCloseException;
import com.jyl.portfolio.commons.exceptions.SeckillException;
import com.jyl.portfolio.commons.mqmessage.SeckillMsgBody;
import com.jyl.portfolio.commons.stateenum.stateenum.SeckillStateEnum;
import com.jyl.portfolio.order.entity.SeckillOrder;
import com.jyl.portfolio.order.entity.SeckillSwag;
import com.jyl.portfolio.order.repository.OrderRepository;
import com.jyl.portfolio.order.repository.SwagRepository;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SeckillServiceImpl implements SeckillService {
    private static Logger logger = LogManager.getLogger(SeckillServiceImpl.class.getSimpleName());
    private final Gson gson;
    private final SwagRepository swagRepository;
    private final OrderRepository orderRepository;
    //设置盐值字符串，随便定义，用于混淆MD5值
    private final String salt = "sjajaspu-i-2jrfm;sd";
    @Reference
    MQProducer mqProducer;
    @Reference
    RedisClient rc;

    public SeckillServiceImpl(
            SwagRepository swagRepository,
            OrderRepository orderRepository,
            Gson gson
    ) {
        this.swagRepository = swagRepository;
        this.orderRepository = orderRepository;
        this.gson = gson;
    }

    @Override
    public List<SeckillSwag> findAll_swags() {
        return swagRepository.findAll();
    }

    @Override
    public List<SeckillOrder> findAll_orders() {
        return orderRepository.findAll();
    }

    @Override
    public SeckillSwag findBySeckillSwagId(Long seckillSwag_Id) {
        Optional<SeckillSwag> result = swagRepository.findBySeckillSwagId(seckillSwag_Id);
        return result.orElse(null);
    }

    @Override
    public UrlExposer exportSeckillUrl(Long seckillSwagId) {
        logger.info("Begin exportSeckillUrl...exporting swag_id: "+seckillSwagId);
        /*
            Redis key for swag id: "url:" + seckillSwagId
         */
        // check if url is already in cache; if yes, return it.
        UrlExposer url_in_redis = rc.exportSeckillUrl(seckillSwagId);
        if(url_in_redis != null) {
            logger.info("URL is loaded from redis");
            return url_in_redis;
        }


        // not in the cache then continue with the normal postgres call and store the url in redis as a json
        logger.info("Generating url from DB.. seckillSwagId: " + seckillSwagId);

        Optional<SeckillSwag> swag = swagRepository.findBySeckillSwagId(seckillSwagId);
        if (swag.isPresent()) {
            logger.info("Found swag from postgres.");
            //generate md5Url
            String md5Url = getMd5(swag.get().getSeckillSwagId());
            logger.info("md5 hashed url " + md5Url);

            Date startTs = swag.get().getStartTime();
            Date endTs = swag.get().getEndTime();
            Date now = new Date();
            int currentStockCount = swag.get().getStockCount();

            if (currentStockCount > 0 && now.getTime() > startTs.getTime() && now.getTime() < endTs.getTime()) {
                logger.info("特价中.. current stock count=" + currentStockCount + " swagID=" + seckillSwagId);
                UrlExposer returnValue = new UrlExposer(
                        true, md5Url, swag.get().getSeckillSwagId(),
                        currentStockCount, swag.get().getStartTime().getTime(), swag.get().getEndTime().getTime(),
                        swag.get().getSeckill_price());

                String str_returnValue = gson.toJson(returnValue);

                logger.info("Add the url to Redis ");
                rc.setSwagUrl(seckillSwagId, str_returnValue);

                return returnValue;
            }
            logger.info("Product is currently not on sale - 没有在秒杀的限时里");
            return new UrlExposer(false, swag.get().getSeckillSwagId());
        }
        return null;
    }


    private String getMd5(long seckillSwagId) {
        String base = seckillSwagId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    @Override
    public SeckillExecution executeSeckill(SeckillParameter requestParam) throws Exception {

        Long seckillSwagId = requestParam.getSeckillSwagId();
        Long userPhone = requestParam.getUserPhone();
        logger.info("Executing secKill...seckill_swag_id: "+seckillSwagId+ " userphone: "+userPhone);
        String md5Url = requestParam.getMd5Url();
        BigDecimal dealPrice = requestParam.getDealPrice();
        if (md5Url == null || !md5Url.equalsIgnoreCase(getMd5(seckillSwagId))) {
            throw new SeckillException("seckill data is tampered. hashed url result is different. hased url: "+getMd5(seckillSwagId) );
        }

        // check if the user has placed the order.
        String str_order = rc.getOrder(userPhone, seckillSwagId);
        try{
            if (str_order != null) {
                logger.info("Seckill order already exists in redis. 重复购买。");
                throw new RepeatkillException("Your order already placed.");
            } else {


                /*
                    2019-Oct-26 Update: encapsulate swagID + userPhone as a msg, send the msg to the jianku_exchange
                 */
                SeckillMsgBody msg = new SeckillMsgBody(Calendar.getInstance(), seckillSwagId, userPhone);
                // // 进入待秒杀队列，进行后续串行操作
                logger.info("Sending msg to jianku_exchange...msg: "+msg);
                mqProducer.jianku_send(msg);
                // 立即返回给客户端，说明秒杀成功了
                logger.info("Decrementing stock count in redis...");
                decrementStockCountInRedis(msg,  dealPrice);
                logger.info("Decrementing stock count in redis...Done");
                return new SeckillExecution(seckillSwagId, 1,
                        Objects.requireNonNull(SeckillStateEnum.stateOf(1)).getStateInfo());
            }

        } catch (RepeatkillException ex) {
            logger.error("userPhone " + userPhone + " try to buy twice this product id: " + seckillSwagId);
            throw new RepeatkillException("...userPhone " + userPhone + " try to buy twice this product id: " + seckillSwagId);
        } catch (SeckillCloseException ex) {
            throw new SeckillCloseException("user phone " + userPhone);
        } catch (SeckillException ex) {
            // all other exceptions...
            logger.error("userPhone: " + userPhone + " " + ex.getMessage());
            throw new SeckillException(ex.getMessage());
        }
    }

    // Purpose: decrement stockcount in redis
//    @Transactional
    public void decrementStockCountInRedis(SeckillMsgBody body, BigDecimal dealPrice) {


        Long seckillSwagId = body.getSeckillSwagId();
        Long userPhone = body.getUserPhone();

        logger.info("decrementRedisPGStockCountAndSaveOrder seckillSwagID: " + seckillSwagId + " userPhone: " + userPhone);

        long dealStartTs = 0;
        long dealEndTs = 0;
        // update stock count in cache
        Integer remainingStockCount = rc.updateStockCount(seckillSwagId, userPhone);

        // save the order to Postgres
        decrementStockCountPostgres(seckillSwagId, userPhone, remainingStockCount, dealPrice);
    }

    // updateInventory only interacts to postgres
    private void decrementStockCountPostgres(long seckillid, long userPhone, Integer remainStockCount, BigDecimal seckill_price) {
        if(remainStockCount != null) {
            logger.info("Updating inventory...");
            swagRepository.updateStockCount(remainStockCount, seckillid);
            logger.info("Inserting order...");
            orderRepository.insertOder(seckillid, seckill_price, userPhone, 1); // state: 1 = 秒杀成功
        }
    }

    public void clear() throws Exception {
        rc.clear();
        orderRepository.deleteAll();
        swagRepository.deleteAll();

        swagRepository.insertDummyData_HuaweiMate10();
        swagRepository.insertDummyData_iphone();
        swagRepository.insertDummyData_HuaweiMate20();
        swagRepository.insertDummyData_HuaweiMate30();
        swagRepository.insertDummyData_Iphone30();

        orderRepository.insertDummyData_order1();
        orderRepository.insertDummyData_order2();
        orderRepository.insertDummyData_order3();
    }
}

