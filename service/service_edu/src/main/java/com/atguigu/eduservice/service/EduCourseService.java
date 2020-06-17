package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-03
 */
public interface EduCourseService extends IService<EduCourse> {
    //添加课程基本信息的方法
    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoForm courseInfoForm);

    CoursePublishVo getPublishCourseInfo(String id);

    Map pageCourseList(long page, long limit, CourseQuery courseQuery);

    void removeCourse(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);
    /**
     * 获取课程信息
     * @param courseId
     * @return
     */
    CourseWebVo getBaseCourseInfo(String courseId);

    /**
     * 更新课程浏览数
     * @param courseId
     */
    void updatePageViewCount(String courseId);
}
