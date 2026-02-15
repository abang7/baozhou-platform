package com.baozhou.baozhouplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baozhou.baozhouplatform.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

/**
 * Teacher数据访问层
 */
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
}
