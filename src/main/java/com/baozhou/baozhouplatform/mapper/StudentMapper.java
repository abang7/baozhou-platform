package com.baozhou.baozhouplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baozhou.baozhouplatform.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * Student数据访问层
 *
 * 学习要点：
 * 1. MyBatis-Plus的BaseMapper提供了CRUD方法
 * 2. @Mapper注解指定这是MyBatis的Mapper接口
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    // BaseMapper已经提供了常用的增删改查方法：
    // - insert(Student entity): 插入
    // - deleteById(Serializable id): 根据ID删除
    // - updateById(Student entity): 根据ID更新
    // - selectById(Serializable id): 根据ID查询
    // - selectList(Wrapper<Student> wrapper): 条件查询
    // - selectPage(Page<Student> page, Wrapper<Student> wrapper): 分页查询

    // 如果需要自定义SQL，可以在这里添加方法并使用@Select、@Insert等注解
    // 或者在resources/mapper目录下创建XML文件
}
