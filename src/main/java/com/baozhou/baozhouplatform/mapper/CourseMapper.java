package com.baozhou.baozhouplatform.mapper;

import com.baozhou.baozhouplatform.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * CourseMapper - 数据访问层接口
 * 负责与数据库的交互操作
 */
@Mapper  // 告诉Spring这是MyBatis的Mapper接口，需要被扫描和管理
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * BaseMapper<Course> 已经提供了常用的CRUD方法：
     *
     * - insert(Course entity)        插入一条记录
     * - deleteById(Serializable id) 根据ID删除
     * - updateById(Course entity)   根据ID更新
     * - selectById(Serializable id) 根据ID查询
     * - selectList(Wrapper)         查询列表
     * - selectPage(...)             分页查询
     *
     * 因为继承了BaseMapper，我们不需要写任何SQL代码！
     * MyBatis-Plus会自动帮我们生成SQL语句
     *
     * 如果需要自定义复杂的查询，可以在这里添加方法，
     * 比如：List<Course> selectByTeacherName(String name);
     */
}