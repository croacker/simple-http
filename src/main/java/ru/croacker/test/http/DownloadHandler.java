package ru.croacker.test.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 */
@Slf4j
@Component("downloadHandler")
public class DownloadHandler extends FileHandler {

    @Override
    protected InputStream getStream(String fileId){
        InputStream inputStream = null;
        String fileName = "../".concat(FilenameUtils.concat(getUploadFolder(), FilenameUtils.getName(fileId)));
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
