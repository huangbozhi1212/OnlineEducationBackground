package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.config.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-03
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;//小节service

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1、根据课程id查询课程里面的所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2、根据课程id查询课程里面的所有小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //创建集合，用于存储最终封装的数据
        List<ChapterVo> finalList = new ArrayList<>();

        //3、遍历查询章节list集合进行封装
        //查询出来所有的章节list集合遍历
        for (int i = 0; i < eduChapterList.size(); i++) {
            //每一个章节
            EduChapter eduChapter = eduChapterList.get(i);
            //eduChapter对象复制到chapterVo里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //把chapter放到最终的list集合
            finalList.add(chapterVo);

            //创建集合。用于封装章节的小节
            List<VideoVo> videoVoList = new ArrayList<>();

            //4、遍历查询小节list集合进行封装
            for (int j = 0; j < eduVideoList.size(); j++) {
                //得到每一个小节
                EduVideo eduVideo = eduVideoList.get(j);
                //判断小节里的chapterid和章节里的id是否一样
                if (eduVideo.getChapterId().equals(eduChapter.getId())){
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            //把封装之后的小节list集合，放到章节对象里面
            chapterVo.setChildren(videoVoList);
        }
        return finalList;
    }

    //删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterId章节id,查询小节表，如果查询有数据，不能删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        if (count >0){
            //查询出小节，不能删除
            throw new GuliException(20001,"不能删除");
        }else {
            //没有小节数据，可以删除
            int deleteById = baseMapper.deleteById(chapterId);
            return deleteById> 0;
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
