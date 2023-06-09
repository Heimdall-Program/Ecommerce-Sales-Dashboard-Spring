package com.example.MonitoringInternetShop.Secure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleCheckInterceptor("ADMIN")).addPathPatterns("/dashboard", "/edit_order", "/edit-user", "/orders", "/products", "/promotions", "/settings", "/user-management");
        registry.addInterceptor(new RoleCheckInterceptor("USER")).addPathPatterns("/create-order", "/user-orders");
    }

}
