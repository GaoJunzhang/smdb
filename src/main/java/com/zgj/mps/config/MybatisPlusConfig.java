package com.zgj.mps.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* class_name: MybatisPlusConfig
* package: com.zgj.zboot.config.mybatisplus
* describe: mybatis配置
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 10:48
**/
@Configuration
@MapperScan("com.zgj.mps.dao.mapper*")
public class MybatisPlusConfig {

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
