package ru.croc.test.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 *
 */
@Service
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
