
-- -- ----------------------------
-- -- Records of seckill_swag
-- -- ----------------------------
INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('1', 'iphoneX', '7788.00', '788.00','100', '2019-09-22 17:22:52', '2029-06-22 17:22:52', '2019-03-22 17:22:52');
INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('2', '华为 Mate 10', '4199.00', '419.00','50', '2019-09-22 17:22:52', '2029-06-22 17:22:52', '2019-03-22 17:22:52');
INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('3', '华为 Mate 20', '3199.00', '319.00','50', '2019-09-22 17:22:52', '2029-06-22 17:22:52', '2019-03-22 17:22:52');
INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('4', '华为 Mate 30', '6199.00', '619.00','50', '2019-05-22 17:22:52', '2019-06-22 17:22:52', '2018-03-22 17:22:52');
INSERT INTO  seckill_swag(seckill_swag_id, title, price, seckill_price, stock_count, start_time, end_time, create_time)  VALUES ('5', 'Iphone IIX', '6199.00', '619.00','1', '2019-05-22 17:22:52', '2019-06-22 17:22:52', '2018-03-22 17:22:52');

-- -- ----------------------------
-- -- Records of sk_order
-- -- ----------------------------
INSERT INTO  seckill_order(seckill_swag_id, total,user_phone,create_time,state)  VALUES ('1', '1207.00', '18520272650', '2018-06-02 17:22:52', 1);
INSERT INTO  seckill_order(seckill_swag_id, total,user_phone,create_time,state)  VALUES ('2', '207.00', '13999999999', '2018-06-01 17:22:52', 0);
INSERT INTO  seckill_order(seckill_swag_id, total,user_phone,create_time,state)  VALUES ('3', '7.00', '12999999999', '2018-05-31 17:22:52', -1);
