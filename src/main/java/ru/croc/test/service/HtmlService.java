package ru.croc.test.service;

import java.io.InputStream;

/**
 *
 */
public class HtmlService {

    private static HtmlService instance;

    private ResourceService getResourceService(){
        return ResourceService.getInstance();
    }

    public static HtmlService getInstance() {
        if(instance == null){
            instance = new HtmlService();
        }
        return instance;
    }

    public InputStream getIndex(){
        return getResourceService().get("/view/index.html");
    }

}
