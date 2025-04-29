package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;


    @Scheduled(cron = "0 0/1 * * * ? ")
    public void setOrderTimeout() {
        log.info("定时任务执行");
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(-30);
        List<Orders> ordersUnPaidList =  orderMapper.selectTimeout(dateTime,Orders.UN_PAID);
        List<Orders> ordersTO_BE_CONFIRMEDList =  orderMapper.selectTimeout(dateTime,Orders.TO_BE_CONFIRMED);
        ordersUnPaidList.forEach(order -> {
            order.setStatus(Orders.CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason("订单未支付，自动取消");
            orderMapper.update(order);
        });
        ordersTO_BE_CONFIRMEDList.forEach(order -> {
            order.setStatus(Orders.CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason("商家未确认，自动取消");
            orderMapper.update(order);
        });
    }
}
