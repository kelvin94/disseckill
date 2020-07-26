package com.jyl.portfolio.commons.stateenum.stateenum;

public enum SeckillStateEnum {
    ENQUEUE_PRE_SECKILL(6, "Your order is lining up in the message queue..."),

    /**
     * 释放分布式锁失败，秒杀被淘汰
     */
    DISTLOCK_RELEASE_FAILED(5, "The deal is sold out. No more in stocks."),

    /**
     * 获取分布式锁失败，秒杀被淘汰
     */
    DISTLOCK_ACQUIRE_FAILED(4, "The deal is sold out. No more in stocks."),

    /**
     * Redis秒杀没抢到
     */
    REDIS_ERROR(3, "You are unable to get the deal, the deal is sold off before your purchase can be recorded to DB."),
    SOLD_OUT(2, "The deal is sold out. No more in stocks."),
    SUCCESS(1, "Purchase success."),
    END(0, "Deal is over"),
    REPEAT_KILL(-1, "Repeated purchase of same deal is not allowed"),
    /**
     * 运行时才能检测到的所有异常-系统异常
     */
    INNER_ERROR(-2, "没抢到"),
    /**
     * md5错误的数据篡改
     */
    DATA_REWRITE(-3, "数据篡改"),

    DB_CONCURRENCY_ERROR(-4, "没抢到"),
    /**
     * 被AccessLimitService限流了
     */
    ACCESS_LIMIT(-5, "没抢到");

    private int state;
    private String stateInfo;
    private String otherInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateEnum stateOf(int index) {
        for (SeckillStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}

