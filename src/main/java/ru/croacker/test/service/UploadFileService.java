package ru.croacker.test.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 *
 */
@Service
@Slf4j
public class UploadFileService {

    @Value("${server.upload.folder}")
    @Getter
    private String uploadFolder;

    public File processFile(FileItem fileItem) {
        String fileName = "../".concat(FilenameUtils.concat("upload", FilenameUtils.getName(fileItem.getName())));
        return writeToFile(fileName, fileItem);
    }

    private File getFile(String fileName){
        File file = new File(fileName);
        deleteOld(file);
        return file;
    }

    private void deleteOld(File file){
        if(file.exists()){
            file.delete();
        }
    }

    private File writeToFile(String fileName, FileItem fileItem){
        File file = getFile(fileName);
        try {
            FileUtils.copyInputStreamToFile(fileItem.getInputStream(), file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }
}
