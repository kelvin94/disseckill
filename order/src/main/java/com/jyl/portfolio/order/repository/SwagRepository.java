package com.jyl.portfolio.order.repository;



import com.jyl.portfolio.order.entity.SeckillSwag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SwagRepository extends JpaRepository<SeckillSwag, Long> {

    // JpaRepository provides basic CRUD implementation
    public List<SeckillSwag> findAll();

    public Optional<SeckillSwag> findBySeckillSwagId(Long seckillSwagId);

    @Query(value = "select stock_count from seckill_swag where seckill_swag_id = ?1", nativeQuery = true)
    public Long findRemainingStock(Long seckillSwagId);

    @Transactional
    @Modifying
    @Query(value = "update seckill_swag set stock_count = ?1 where seckill_swag_id = ?2", nativeQuery = true)
    public int updateStockCount(int newSockCount, Long seckillSwagId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('1', 'iphoneX', '7788.00', '788.00','100', '2019-09-22 17:22:52', '2029-06-22 17:22:52', '2019-03-22 17:22:52')", nativeQuery = true)
    public void insertDummyData_iphone();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('2', '华为 Mate 10', '4199.00', '419.00','50', '2019-09-22 17:22:52', '2029-06-22 17:22:52', '2019-03-22 17:22:52')", nativeQuery = true)
    public void insertDummyData_HuaweiMate10();



    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('3', '华为 Mate 20', '3199.00', '319.00','50', '2019-09-22 17:22:52', '2029-06-22 17:22:52', '2019-03-22 17:22:52')", nativeQuery = true)
    public void insertDummyData_HuaweiMate20();


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('4', '华为 Mate 30', '6199.00', '619.00','50', '2019-05-22 17:22:52', '2019-06-22 17:22:52', '2018-03-22 17:22:52')", nativeQuery = true)
    public void insertDummyData_HuaweiMate30();


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('5', 'Iphone IIX', '6199.00', '619.00','1', '2019-05-22 17:22:52', '2019-06-22 17:22:52', '2018-03-22 17:22:52')", nativeQuery = true)
    public void insertDummyData_Iphone30();
}
