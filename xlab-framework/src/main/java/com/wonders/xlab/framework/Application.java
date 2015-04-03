package com.wonders.xlab.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.wonders.xlab.framework.repository.MyRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by wangqiang on 15/3/28.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "${app.basePackages}",
        repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
@EntityScan("${app.basePackages}")
@ComponentScan("${app.basePackages}")
public class Application extends SpringBootServletInitializer {

    @Value("${jackson.indent.output}")
    public boolean jacksonIndentOutput = false;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        if (jacksonIndentOutput) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        return objectMapper
                .registerModule(new JodaModule())
                .registerModule(new Hibernate4Module());

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
