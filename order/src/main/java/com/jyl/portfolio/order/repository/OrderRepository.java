package com.jyl.portfolio.order.repository;

import com.jyl.portfolio.order.entity.SeckillOrder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Repository
public interface OrderRepository extends JpaRepository<SeckillOrder, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into seckill_order(seckill_swag_id, total, user_phone, state) values(:seckillSwagId,:dealPrice, :phoneNumber, :state)", nativeQuery = true)
    public void insertOder(@Param("seckillSwagId") Long seckillSwagId, @Param("dealPrice") BigDecimal dealPrice,
                           @Param("phoneNumber") Long phoneNumber, @Param("state") int state) throws DataIntegrityViolationException;

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_order(seckill_swag_id, total,user_phone,create_time,state)  VALUES ('1', '1207.00', '18520272650', '2018-06-02 17:22:52', 1)", nativeQuery = true)
    public void insertDummyData_order1();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_order(seckill_swag_id, total,user_phone,create_time,state)  VALUES ('2', '207.00', '13999999999', '2018-06-01 17:22:52', 0)", nativeQuery = true)
    public void insertDummyData_order2();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_order(seckill_swag_id, total,user_phone,create_time,state)  VALUES ('3', '7.00', '12999999999', '2018-05-31 17:22:52', -1)", nativeQuery = true)
    public void insertDummyData_order3();
}
