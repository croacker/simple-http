package ru.croc.test.service;

import com.google.common.collect.Maps;
import ru.croc.test.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class ConfigurationService{

    public static final String APP_PROPERTIES = "application.properties";

    public static final String DEFAULT_VALUE = StringUtil.EMPTY;

    private static ConfigurationService instance;

    private Map<String, String> properties;

    public static ConfigurationService getInstance() {
        if(instance == null){
            instance = new ConfigurationService();
            instance.init();
        }
        return instance;
    }

    public void init(){
        Maps.fromProperties(getProperties());
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        try {
            properties.load(getResource());
        } catch (IOException e) {
            Log.getInstance().log(e);
        }
        return properties;
    }

    private InputStream getResource(){
        return getClass().getResourceAsStream(APP_PROPERTIES);
    }

    public String get(String key){
        return StringUtil.asNotNull(properties.get(key));
    }

}
