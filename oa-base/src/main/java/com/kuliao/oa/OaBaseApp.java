package com.kuliao.oa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.kuliao.oa.db.mapper")
@ServletComponentScan(basePackages = "com.kuliao.oa.myfilter")
public class OaBaseApp{
    public static void main( String[] args ){
        SpringApplication.run(OaBaseApp.class);
    }
}
