package com.baozhou.baozhouplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 保州平台主启动类
 *
 * @MapperScan 注解说明：
 * - 指定Mapper接口所在的包
 * - MyBatis-Plus会自动扫描这个包下的所有Mapper接口
 * - 不加这个注解，Service层注入Mapper时会报错
 */
@SpringBootApplication
@MapperScan("com.baozhou.baozhouplatform.mapper")
public class BaozhouPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaozhouPlatformApplication.class, args);
    }

}
