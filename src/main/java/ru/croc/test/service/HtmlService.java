package ru.croc.test.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 *
 */
@Service
public class HtmlService {

    private static HtmlService instance;

    @Autowired @Getter
    private ResourceService resourceService;

    public static HtmlService getInstance() {
        if(instance == null){
            instance = new HtmlService();
        }
        return instance;
    }

    public InputStream getIndex(){
        return getResourceService().get("/view/file-upload-page.html");
    }

}
