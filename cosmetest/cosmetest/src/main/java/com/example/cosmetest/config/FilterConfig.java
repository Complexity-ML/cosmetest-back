package com.example.cosmetest.config;

import com.example.cosmetest.filter.UserActionLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserActionLoggingFilter> loggingFilter() {
        FilterRegistrationBean<UserActionLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserActionLoggingFilter());
        registrationBean.addUrlPatterns("/*"); // Appliquer à toutes les URL
        return registrationBean;
    }
}
