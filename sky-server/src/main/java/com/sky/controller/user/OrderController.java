package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private WebSocketServer webSocketServer;


    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        //webSocket推送消息
        return Result.success(orderPaymentVO);
    }
    @GetMapping("orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id){
        OrderVO orderVO = orderService.listByOrderId(id);
        return Result.success(orderVO);
    }
    @PutMapping("cancel/{id}")
    public Result<String> cancel(@PathVariable Long id){
        orderService.cancel(id);
        return Result.success("取消成功");
    }

    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult = orderService.pageSelect(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("repetition/{id}")
    public Result repetition(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();
    }

    @GetMapping("reminder/{id}")
    public Result reminder(@PathVariable Long id){
        orderService.reminder(id);
        return Result.success();
    }
}
