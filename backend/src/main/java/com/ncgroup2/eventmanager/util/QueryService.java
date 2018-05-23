package com.ncgroup2.eventmanager.util;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@PropertySource("classpath:event.properties")
@PropertySource("classpath:folder.properties")
@PropertySource("classpath:message.properties")
<<<<<<< HEAD
@PropertySource("classpath:queries/note.properties")
=======
>>>>>>> b2ae88754f22771a82878a0eec8b53edde5299f0
public class QueryService {

    @Resource
    private Environment env;

    public String getQuery(String name) {
        return env.getRequiredProperty(name);
    }
}
