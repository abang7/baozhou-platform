package com.baozhou.baozhouplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baozhou.baozhouplatform.entity.Student;

/**
 * Student服务层接口
 *
 * 学习要点：
 * 1. IService是MyBatis-Plus提供的服务层接口
 * 2. 继承IService后会获得更多批量操作的方法
 * 3. Service层用于处理业务逻辑
 *
 * 与Mapper的区别：
 * - Mapper：单表CRUD操作
 * - Service：可以包含复杂业务逻辑、多表操作、事务控制
 */
public interface IStudentService extends IService<Student> {

    // IService已经提供了丰富的CRUD方法：
    // - save(Student entity): 保存
    // - saveBatch(Collection<Student> list): 批量保存
    // - removeById(Serializable id): 根据ID删除
    // - updateById(Student entity): 根据ID更新
    // - getById(Serializable id): 根据ID查询
    // - list(): 查询所有
    // - page(Page<Student> page): 分页查询

    // 如果需要自定义业务方法，可以在这里添加
    // 例如：
    // List<Student> queryStudentsByStatus(String status);
}
