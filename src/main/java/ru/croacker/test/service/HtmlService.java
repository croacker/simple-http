package ru.croacker.test.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 *
 */
@Service
@Slf4j
public class HtmlService {

    @Autowired @Getter
    private ResourceService resourceService;

    public InputStream getIndex(){
        return getResourceService().get("/view/file-upload-page.html");
    }

}
