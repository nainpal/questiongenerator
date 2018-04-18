package com.questiongenerator.config;

import com.questiongenerator.questionprovider.QuestionProviderFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ServiceLocatorFactoryBean questionProviderFactoryServiceLocatorFactoryBean()
    {
        ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(QuestionProviderFactory.class);
        return bean;
    }

    @Bean
    public QuestionProviderFactory questionProviderFactory()
    {
        return (QuestionProviderFactory) questionProviderFactoryServiceLocatorFactoryBean().getObject();
    }
}
