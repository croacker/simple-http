package ru.croacker.test.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 */
@Slf4j
@Component("downloadHttpHandler")
public class DownloadHttpHandler extends FileHttpHandler {

    @Override
    protected InputStream getStream(String fileId){
        InputStream inputStream = null;
        String fileName = getPdfFolder() + fileId.replace("file/", "");
        File file = new File(fileName);
        if(file.exists()){
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }
        return inputStream;
    }

}
