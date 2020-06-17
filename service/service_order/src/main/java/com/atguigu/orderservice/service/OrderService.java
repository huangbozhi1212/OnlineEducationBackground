package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.OrderQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-20
 */
public interface OrderService extends IService<Order> {
    //根据课程id和用户id创建订单，返回订单id
    String createOrder(String courseId, String memberIdByJwtToken);

    Map pageOrderCondition(long page, long limit, OrderQuery order);
}
