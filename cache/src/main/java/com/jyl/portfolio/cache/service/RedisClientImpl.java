package com.jyl.portfolio.cache.service;

import com.google.gson.Gson;
import com.jyl.portfolio.commons.api.cache.RedisClient;
import com.jyl.portfolio.commons.dto.UrlExposer;
import com.jyl.portfolio.commons.exceptions.SeckillCloseException;
import com.jyl.portfolio.commons.util.GeneralUtil;
import org.apache.dubbo.config.annotation.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class RedisClientImpl implements RedisClient {
    private static Logger logger = LogManager.getLogger(RedisClientImpl.class.getSimpleName());
    private final Gson gson;
    private final JedisPool jedisPool;

    public RedisClientImpl(JedisPool jedisPool, Gson gson) {
        this.gson = gson;
        this.jedisPool = jedisPool;
    }

    @Override
    public UrlExposer exportSeckillUrl(Long seckillSwagId) {
        Jedis jedis = null;

         try {
             System.out.println("Export swag url from Redis--->");
            jedis = jedisPool.getResource();
//             check if the key(swag id) is in redis, if exists, decompose the json convert it back to POJO
            if (jedis.exists(GeneralUtil.getUrlRedisKey(seckillSwagId))) {
                logger.info("Seckill product exists in Redis. Returning obj in redis-->");
                String redis_value = jedis.get(GeneralUtil.getUrlRedisKey(seckillSwagId));
                return gson.fromJson(redis_value, UrlExposer.class);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            // return the jedis resource back to the resource pool
            logger.info("closeing redis connection and return resource back to the resource pool");
            if (jedis != null)
                jedis.close();
        }
        return null;
    }

    // ## 不需要这个method
    @Override
    public String getSwagUrl(Long seckillSwagId) {
        return null;
    }

    // ## 不需要这个method
    @Override
    public void setSwagUrl(Long seckillSwagId, String url) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(GeneralUtil.getUrlRedisKey(seckillSwagId), url);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        // return the jedis resource back to the resource pool
    }

    @Override
    public String getOrder(Long userPhone, Long seckillSwagId) {
        String str_order = null;
        Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                logger.debug("key: " + GeneralUtil.getSeckillOrderRedisKey(userPhone, seckillSwagId));
                str_order = jedis.get(GeneralUtil.getSeckillOrderRedisKey(userPhone, seckillSwagId));
            } catch (Exception e) {
                logger.error("Exception thrown..." + e.getMessage());
                e.printStackTrace();
                throw e;
            } finally {
                if (jedis != null) {
                    logger.info("Redis conn close");
                    jedis.close();
                }
            }
        return str_order;
    }

    public void setOrder(String seckillOrderRedisKey, String seckillOrderRedisValue) {
            // then call redis 做减库
            try (Jedis jedis = jedisPool.getResource()) {
                logger.info("...[MQProducer]Consumer receive msg，put order into Redis...");
                jedis.set(seckillOrderRedisKey, seckillOrderRedisValue);
            }
            logger.info("...[MQProducer]Redis - 减库结束...");
    }


    @Override
    public void updateStockCount(Long seckillSwagId, Long userPhone) {
        long dealStartTs = 0;
        long dealEndTs = 0;
        int remainingStockCount = 0;
        BigDecimal seckill_price = null;
        try (Jedis jedis = jedisPool.getResource()) {
            // Non-repeated purchase - Update Redis Url with new stockCount
            String seckill_url = jedis.get(GeneralUtil.getUrlRedisKey(seckillSwagId));
            UrlExposer url = gson.fromJson(seckill_url, UrlExposer.class);
            if (url != null && url.getStockCount() > 0) {
                remainingStockCount = url.getStockCount();
                remainingStockCount--;
                url.setStockCount(remainingStockCount);
                if (url.getStockCount() <= 0) {
                    throw new SeckillCloseException("Sold out. 卖完啦洗洗睡吧.");
                }
                dealStartTs = url.getDealStart();
                dealEndTs = url.getDealEnd();
                seckill_price = url.getSeckill_price();
                Date currentSysTime = new Date();
                if (currentSysTime.getTime() > dealStartTs && currentSysTime.getTime() < dealEndTs) {
                    // Redis: update stockCount...
                    logger.info("updating redis stockCount...");
                    jedis.set(GeneralUtil.getUrlRedisKey(seckillSwagId), gson.toJson(url));
                    jedis.set(GeneralUtil.getSeckillOrderRedisKey(userPhone, seckillSwagId), "sfasfdsa");// "1" as
                    // value to indicate this person has placed an order of this product
                    // If sales is still on-going(here is only double check incase FE exposes the api endpoint for
                    // seckill_execute method)
                    //  create a new msg and send to a com.jyl.portfolio.cache.service to update Postgres stockCount and persist the
                    //  seckillOrder record.
                    logger.info("Redis part done... now call postgres to insert order");
                }
            }
        }
    }
}
