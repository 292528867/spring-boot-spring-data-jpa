package com.wonders.xlab.framework;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.thetransactioncompany.cors.CORSFilter;
import com.wonders.xlab.framework.repository.MyRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public FilterRegistrationBean corsFilter() {

        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("cors.allowOrigin", "*");
        initParameters.put("cors.supportedMethods", "GET, POST, HEAD, PUT, DELETE");
        initParameters.put("cors.supportedHeaders", "Accept, Origin, X-Requested-With, Content-Type, Last-Modified");


        Filter filter = new CORSFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setInitParameters(initParameters);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        if (jacksonIndentOutput) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString("");
            }
        });

        Hibernate4Module hibernateMoudle = new Hibernate4Module();
        hibernateMoudle.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);

        return objectMapper
                .registerModule(new JodaModule())
                .registerModule(hibernateMoudle);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.profiles("production")
                .sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
