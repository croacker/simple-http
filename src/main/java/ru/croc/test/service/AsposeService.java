package ru.croc.test.service;

import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 *
 */
public class AsposeService {

    private static AsposeService instance;

    public static AsposeService getInstance() {
        if(instance == null){
            instance = new AsposeService();
        }
        return instance;
    }

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
