package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-01
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try {
            //文件输入流
            InputStream inputStream = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //课程分类列表（树形）
    @Override
    public List<OneSubject> getAllSubject() {
        //1、查询所有一级分类 parent_id = 0
        QueryWrapper<EduSubject> wrapperOne= new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //2、查询所有二级分类 parent_id != 0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建list集合，用于存储最终的封装数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //3、封装一级分类
        //查询出来的所有的一级分类list集合遍历，得到每一个一级分类的对象，获取每一个一级分类对象的值
        for (int i = 0; i < oneSubjectList.size(); i++) {
            //得到oneSubjectList每一个eduSubject对象
            EduSubject subject = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
            //oneSubject.setId(subject.getId());
            //oneSubject.setTitle(subject.getTitle());
            //把subject的值复制到对应的oneSubject对象里面
            BeanUtils.copyProperties(subject,oneSubject);
            //把oneSubject里面的值获取出来，放到OneSubject对象里面
            finalSubjectList.add(oneSubject);
            //4、封装二级分类
            //在一级分类循环遍历查询所有的二级分类
            //创建list集合封装到每一个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for (int j = 0; j < twoSubjectList.size(); j++) {
                //获取每一个二级分类
                EduSubject twoFinalSubject = twoSubjectList.get(j);
                //判断二级分类的parent_id和一级分类的id是否一样
                if (twoFinalSubject.getParentId().equals(oneSubject.getId())){
                    //把twoFinalSubject复制到twoSubject里面，放到twoFinalSubjectList
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoFinalSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            //把一级分类的所有二级分类放到一级里面
            oneSubject.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
