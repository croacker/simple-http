package ru.croc.test.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.croc.test.util.StringUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
@Service @Slf4j
public class ConfigurationService{

    public static final String APP_PROPERTIES = "/application.properties";

    public static class Keys{
        public static final String PORT = "server.port";
        public static final String UPLOAD_FOLDER = "server.upload.folder";
    }

    private static ConfigurationService instance;

    private Map<String, String> properties;

    private ResourceService getResourceService(){
        return ResourceService.getInstance();
    }

    public static ConfigurationService getInstance() {
        if(instance == null){
            instance = new ConfigurationService();
            instance.init();
        }
        return instance;
    }

    @PostConstruct
    public void init(){
        properties = Maps.fromProperties(getProperties());
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        try {
            properties.load(getResourceService().get(APP_PROPERTIES));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return properties;
    }

    public String get(String key){
        return StringUtil.asNotNull(properties.get(key));
    }

}
