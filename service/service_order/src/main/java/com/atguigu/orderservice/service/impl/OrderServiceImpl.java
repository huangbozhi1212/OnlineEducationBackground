package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.orderVo.CourseWebVoOrder;
import com.atguigu.commonutils.orderVo.UcenterMemberOrder;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.OrderQuery;
import com.atguigu.orderservice.mapper.OrderMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    //根据课程id和用户id创建订单，返回订单id
    @Override
    public String createOrder(String courseId, String memberIdByJwtToken) {
        //通过远程调用根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberIdByJwtToken);
        //通过远程调用根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //创建Order对象，向order对象里面设置需要数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
        order.setCourseId(courseId); //课程id
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberIdByJwtToken);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);  //订单状态（0：未支付 1：已支付）
        order.setPayType(1);  //支付类型 ，微信1
        baseMapper.insert(order);
        //返回订单号
        return order.getOrderNo();
    }

    @Override
    public Map pageOrderCondition(long page, long limit, OrderQuery order) {
        //创建page对象
        Page<Order> pageOrder = new Page<>(page,limit);

        //构建条件
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        Integer status = order.getStatus(); //支付状态
        String teacherName = order.getTeacherName();//课程老师
        String courseTitle = order.getCourseTitle();//课程标题
        Integer payType = order.getPayType(); //支付类型
        String begin = order.getBegin();//开始时间
        String end = order.getEnd(); //结束时间
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(teacherName) ) {
            wrapper.like("teacher_name", teacherName);
        }

        if (!StringUtils.isEmpty(courseTitle)) {
            wrapper.like("course_title", courseTitle);
        }
        if (!StringUtils.isEmpty(payType)) {
            wrapper.eq("pay_type", payType);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);//大于
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);//小于
        }
        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        baseMapper.selectPage(pageOrder,wrapper);

        HashMap<String, Object> map = new HashMap<>();

        long total = pageOrder.getTotal();     //总记录数
        List<Order> records = pageOrder.getRecords();//集合

        map.put("total",total);
        map.put("records",records);
        return map;
    }
}
