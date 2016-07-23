package ru.croc.test.service;

import java.io.InputStream;

/**
 *
 */
public class ResourceService{

    private static ResourceService instance;

    public static ResourceService getInstance() {
        if(instance == null){
            instance = new ResourceService();
        }
        return instance;
    }

    public InputStream get(String name){
        return getClass().getResourceAsStream(name);
    }

}
