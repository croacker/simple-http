package ru.croc.test.service;

import com.google.common.collect.Maps;
import ru.croc.test.util.StringUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class ConfigurationService{

    public static final String APP_PROPERTIES = "/application.properties";

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

    public void init(){
        properties = Maps.fromProperties(getProperties());
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        try {
            properties.load(getResourceService().get(APP_PROPERTIES));
        } catch (IOException e) {
            Log.getInstance().log(e);
        }
        return properties;
    }

    public String get(String key){
        return StringUtil.asNotNull(properties.get(key));
    }

}
