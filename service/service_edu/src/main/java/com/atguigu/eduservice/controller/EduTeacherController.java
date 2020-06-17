package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-04-17
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin  //解决跨域
public class EduTeacherController {
    //把service注入
    @Autowired
    private EduTeacherService teacherService;


    //查询讲师表所有数据
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }


    //逻辑删除讲师
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id){
        boolean remove = teacherService.removeById(id);
        if (remove){
            return R.ok();
        }else {
            return R.error();
        }
    }
    //分页讲师列表
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit){
        Page<EduTeacher> pageParm = new Page<>(page, limit);

        teacherService.page(pageParm,null);
        List<EduTeacher> records = pageParm.getRecords(); //当前页数据
        long total = pageParm.getTotal(); //总记录数
        return R.ok().data("total",total).data("rows",records);
    }

    //条件查询带分页的方法
    @ApiOperation(value = "带模糊查询的分页讲师列表")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
                                  @ApiParam(name = "current", value = "当前页码", required = true)
                                  @PathVariable long current,
                                  @ApiParam(name = "limit", value = "每页记录数", required = true)
                                  @PathVariable long limit,
                                  @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
                                  @RequestBody TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> eduTeacherPage = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        //多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);//like
        }
        if (!StringUtils.isEmpty(level) ) {
            wrapper.eq("level", level);//等于
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
        teacherService.page(eduTeacherPage,wrapper);

        long total = eduTeacherPage.getTotal();     //总记录数
        List<EduTeacher> records = eduTeacherPage.getRecords();//集合
        return R.ok().data("total",total).data("rows",records);
    }

    //新增教师
    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R save(@ApiParam(name = "teacher",value = "讲师对象",required = true)
                  @RequestBody EduTeacher teacher){
        boolean save = teacherService.save(teacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //根据讲师ID进行查询
    @ApiOperation(value = "根据讲师ID进行查询")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher teacherServiceById = teacherService.getById(id);
        return R.ok().data("teacher",teacherServiceById);
    }

    //讲师修改功能
    @ApiOperation(value = "根据讲师ID进行修改")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean update = teacherService.updateById(eduTeacher);
        if (update){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

