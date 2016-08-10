package ru.croacker.test.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.croacker.test.service.upload.UploadResut;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
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

    @Autowired
    @Getter
    private FileTypeMap fileTypeMap;

    public UploadResut processFile(FileItem fileItem) {
        String fileName =
                "../".concat(FilenameUtils.concat(getUploadFolder(), FilenameUtils.getName(fileItem.getName())));
        File file = writeToFile(fileName, fileItem);
        return toUploadResut(file);
    }

    private File getFile(String fileName) {
        File file = new File(fileName);
        deleteOld(file);
        return file;
    }

    private void deleteOld(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    private File writeToFile(String fileName, FileItem fileItem) {
        File file = getFile(fileName);
        try {
            FileUtils.copyInputStreamToFile(fileItem.getInputStream(), file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }

    private UploadResut toUploadResut(File file) {
        UploadResut resut = new UploadResut();
        if (file != null && file.exists()) {
            String contentType = getFileTypeMap().getContentType(file);
            resut.name(file.getName()).size(file.length()).type(getFileTypeMap().getContentType(file))
                    .url("file/" + file.getName());
            if (contentType.contains("image")) {
                resut.thumbnailUrl("file/" + file.getName());
            }
        }
        return resut;
    }

}
