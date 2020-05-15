package com.jyl.portfolio.order.service;


import com.jyl.portfolio.commons.apiParameter.SeckillParameter;
import com.jyl.portfolio.commons.mqmessage.SeckillMsgBody;
import com.jyl.portfolio.order.dto.SeckillExecution;
import com.jyl.portfolio.commons.dto.UrlExposer;
import com.jyl.portfolio.order.entity.SeckillOrder;
import com.jyl.portfolio.order.entity.SeckillSwag;

import java.math.BigDecimal;
import java.util.List;

public interface SeckillService {

    /**
     * 获取所有的秒杀商品列表
     *
     * @return
     */
    List<SeckillSwag> findAll_swags();

    /**
     * 获取所有的已完成订单
     *
     * @return
     */
    List<SeckillOrder> findAll_orders();

    /**
     * 获取某一条商品秒杀信息
     *
     * @param seckillSwag_Id
     * @return
     */
    SeckillSwag findBySeckillSwagId(Long seckillSwag_Id);

    /**
     * 秒杀开始时输出暴露秒杀的地址
     * 否者输出系统时间和秒杀时间
     * <p>
     * // exposer = 暴露接口用到的方法，目的就是获取秒杀商品抢购的地址
     *
     * @param seckillId
     */
    UrlExposer exportSeckillUrl(Long seckillId);

    /**
     * 执行秒杀的操作
     *
     * @param requestParam
     */
    SeckillExecution executeSeckill(SeckillParameter requestParam)
            throws Exception;

    /**
     * @param msg: RabbitMQ message, constructed by SeckillMsgBody class
     * @param dealPrice
     */
    void decrementStockCountInRedis(SeckillMsgBody body, BigDecimal dealPrice);

    /**
     *
     */
    void clear() throws Exception ;
}
