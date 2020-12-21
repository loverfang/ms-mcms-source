package net.mingsoft.config;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.aop.Advisor;
import net.mingsoft.basic.filter.XSSEscapeFilter;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.mingsoft.basic.interceptor.ActionInterceptor;
import net.mingsoft.basic.util.BasicUtil;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * 上传路径
	 */
	@Value("${ms.upload.path}")
	private String uploadFloderPath;
	/**
	 * 上传路径映射
	 */
	@Value("${ms.upload.mapping}")
	private String uploadMapping;
	@Bean
	public ActionInterceptor actionInterceptor() {
		return new ActionInterceptor();
	}


	/**
	 * 增加对rest api鉴权的spring mvc拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 排除配置
		registry.addInterceptor(actionInterceptor()).excludePathPatterns("/static/**", "/app/**", "/webjars/**",
				"/*.html", "/*.htm");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**").addResourceLocations("/upload/","file:upload/");
		registry.addResourceHandler("/templets/**").addResourceLocations("/templets/","file:templets/");
		registry.addResourceHandler("/html/**").addResourceLocations("/html/","file:html/");
		//三种映射方式 webapp下、当前目录下、jar内
		registry.addResourceHandler("/app/**").addResourceLocations("/app/","file:app/", "classpath:/app/");
		registry.addResourceHandler("/static/**").addResourceLocations("/static/","file:static/","classpath:/static/","classpath:/META-INF/resources/");
		registry.addResourceHandler("/api/**").addResourceLocations("/api/","file:api/", "classpath:/api/");
		if(new File(uploadFloderPath).isAbsolute()){
			//如果指定了绝对路径，上传的文件都映射到uploadMapping下
			registry.addResourceHandler(uploadMapping).addResourceLocations("file:"+uploadFloderPath+ File.separator
					//映射其他路径文件
					//,file:F://images
			);
		}
	}


	/**
	 * druid数据库连接池监控
	 */
	@Bean
	public BeanTypeAutoProxyCreator beanTypeAutoProxyCreator() {
		BeanTypeAutoProxyCreator beanTypeAutoProxyCreator = new BeanTypeAutoProxyCreator();
		beanTypeAutoProxyCreator.setTargetBeanType(DruidDataSource.class);
		beanTypeAutoProxyCreator.setInterceptorNames("druidStatInterceptor");
		return beanTypeAutoProxyCreator;
	}
	//XSS过滤器
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        XSSEscapeFilter xssFilter = new XSSEscapeFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean(xssFilter);
        xssFilter.includes.add(".*/search.do");
        registration.setName("XSSFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

	/**
	 * RequestContextListener注册
	 */
	@Bean
	public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerRegistration() {
		return new ServletListenerRegistrationBean<>(new RequestContextListener());
	}
	/**
	 * 设置默认首页
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		WebMvcConfigurer.super.addViewControllers(registry);
	}


	/**
	 * 解决com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException 问题，提交实体不存在的字段异常
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// TODO Auto-generated method stub
		converters.add(mappingJackson2HttpMessageConverter());
		WebMvcConfigurer.super.configureMessageConverters(converters);

	}

	@Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();

        //添加此配置
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converter.setObjectMapper(objectMapper);
        return converter;
    }
	@Bean
	public ExecutorService crawlExecutorPool() {
		// 创建线程池
		ExecutorService pool =
				new ThreadPoolExecutor(20, 20,
						0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>());
		return pool;
	}
}
