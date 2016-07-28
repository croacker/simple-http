package ru.croacker.test.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 *
 */
@Service
public class ResourceService{

    public InputStream get(String name){
        return getClass().getResourceAsStream(name);
    }

}
