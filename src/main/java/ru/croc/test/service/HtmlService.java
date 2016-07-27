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

    @Autowired @Getter
    private ResourceService resourceService;

    public InputStream getIndex(){
        return getResourceService().get("/view/file-upload-page.html");
    }

}
