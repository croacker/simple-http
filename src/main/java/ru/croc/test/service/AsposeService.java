package ru.croc.test.service;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 *
 */
@Service
public class AsposeService {

    @Value("${server.pdf.folder}")
    @Getter
    private String pdfFolder;

    private ConfigurationService getConfigurationService(){
        return ConfigurationService.getInstance();
    }

    public File processFile(String fileName){
        return processFile(new File(fileName));
    }

    public File processFile(File file){
        return file;
    }

    public File processFile(String fileName, InputStream inputStream) throws IOException {
        String targetFileName = getPdfFolder() + "/" + fileName;
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
