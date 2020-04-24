package com.jyl.portfolio.order.dto;

import com.jyl.portfolio.commons.stateenum.stateenum.SeckillStateEnum;
import com.jyl.portfolio.order.entity.SeckillOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SeckillExecution implements Serializable {
    private Long seckillSwagId;
    private int state;
    private Long userPhone;
    private String stateInfo;

    // 秒杀成功的订单对象
    private SeckillOrder seckillOrder;

    public SeckillExecution(Long seckillSwagId, int state, SeckillStateEnum seckillStatEnum,
                            SeckillOrder seckillOrder) {
        // 用于seckill success
        this.seckillSwagId = seckillSwagId;
        this.state = state;
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.seckillOrder = seckillOrder;
    }

    public SeckillExecution(Long seckillSwagId, int state, String stateInfo) {
        // return this for any exception is thrown
        this.seckillSwagId = seckillSwagId;
        this.state = state;
        this.stateInfo = stateInfo;
    }
}

