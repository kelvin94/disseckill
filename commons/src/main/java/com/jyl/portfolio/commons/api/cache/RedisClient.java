package com.jyl.portfolio.commons.api.cache;

import com.jyl.portfolio.commons.dto.UrlExposer;

public interface RedisClient {
        public UrlExposer exportSeckillUrl(Long seckillSwagId);

        public String getSwagUrl(Long seckillSwagId);

        public void setSwagUrl(Long seckillSwagId, String url);

        public String getOrder( Long userPhone, Long seckillSwagId);

        public void setOrder(String seckillOrderRedisKey, String seckillOrderRedisValue);

        public int updateStockCount(Long seckillSwagId, Long userPhone);

        public void clear();
}
