package ru.croacker.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 *
 */
@Service
@Slf4j
public class ResourceService{

    public InputStream get(String name){
        return getClass().getResourceAsStream(name);
    }

}
