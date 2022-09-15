package com.teraenergy.illegalparking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.teraenergy"}, excludeFilters = {@ComponentScan.Filter(SpringBootApplication.class)})
@EntityScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"com.teraenergy.illegalparking.model.entity"}
)
@MapperScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"com.teraenergy.illegalparking.model.mapper"}
)
public class ApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTests.class, args);
    }

}
