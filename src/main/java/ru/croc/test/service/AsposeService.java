package ru.croc.test.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 *
 */
@Service
public class AsposeService {

    private ConfigurationService getConfigurationService(){
        return ConfigurationService.getInstance();
    }

    public File processFile(File file){
        return file;
    }

    public File processFile(String fileName, InputStream inputStream) throws IOException {
        String targetFileName = getConfigurationService().get(ConfigurationService.Keys.UPLOAD_FOLDER) + "/" + fileName;
        return convertToPdf(targetFileName, inputStream);
    }

    private File convertToPdf(String targetFileName, InputStream inputStream){
        File targetFile = new File(targetFileName);
        try {
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile;
    }

}
