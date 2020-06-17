package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.orderVo.UcenterMemberOrder;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/ucenterservice/ucentermember")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    @PostMapping("/login")
    public R login(@RequestBody UcenterMember member){
        //调用service方法实现登陆，返回token值，使用jwt生成
        String token = memberService.login(member);
        return R.ok().data("token",token);
   }

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    // 根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        System.out.println(request);
        //调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember byId = memberService.getById(memberIdByJwtToken);
        return R.ok().data("userInfo",byId);
    }

    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = memberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }
    //统计某一天的注册人数
    @GetMapping("countregister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count= memberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }
}

