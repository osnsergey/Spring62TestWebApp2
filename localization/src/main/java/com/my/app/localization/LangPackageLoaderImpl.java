package com.my.app.localization;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LangPackageLoaderImpl implements LangPackageLoader, ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {}

    @Override
    public List<Object> provideLanguagesForComponent(String component) {
      return Collections.emptyList();
    }
}