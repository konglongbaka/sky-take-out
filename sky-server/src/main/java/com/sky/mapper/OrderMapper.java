package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
   void insert(Orders order);

   /**
    * 根据订单号查询订单
    * @param orderNumber
    */
   @Select("select * from orders where number = #{orderNumber}")
   Orders getByNumber(String orderNumber);

   /**
    * 修改订单信息
    * @param orders
    */
   void update(Orders orders);

   @Select("select * from orders where id = #{orderId}")
   Orders selectById(Long orderId);
   @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} " +
           "where number = #{orderNumber}")
   void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);


   Page<OrderVO> pageSelect(OrdersPageQueryDTO ordersPageQueryDTO);
   @Update("update orders set status = #{cancelled} where id = #{id}")
   void cancel(Long id, Integer cancelled);
   @Select("select * from orders where status = #{status} and order_time < #{dateTime}")
   List<Orders> selectTimeout(LocalDateTime dateTime, Integer status);
   @Select("select count(*) from orders where status = #{status}")
   Integer countByMap(Map map);
   @Select("select sum(amount) from orders where status = #{status}")
   Double sumByMap(Map map);
}
