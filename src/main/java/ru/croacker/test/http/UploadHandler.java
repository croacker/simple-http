package ru.croacker.test.http;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.croacker.test.http.result.UploarResut;
import ru.croacker.test.service.UploadFileService;
import ru.croacker.test.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component("uploadHttpHandler")
@Slf4j
public class UploadHandler implements HttpHandler {

    @Autowired
    @Getter
    private Gson gson;

    @Autowired
    @Getter
    private UploadFileService uploadFileService;


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("Process request:" + getGson().toJson(httpExchange.getRequestHeaders()));
        if (httpExchange.getRequestMethod().equals("POST")) {
            uploadFile(httpExchange);
        } else {
            addError(httpExchange);
        }
    }

    private void addError(HttpExchange httpExchange) throws IOException {
        String response = "Upload file!";
        httpExchange.sendResponseHeaders(200, response.length());
        @Cleanup OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
    }

    private void uploadFile(HttpExchange httpExchange) {
        for (Map.Entry<String, List<String>> header : httpExchange.getRequestHeaders().entrySet()) {
            log.info(header.getKey() + ": " + header.getValue().get(0));
        }
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        try {
            ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
            List<FileItem> fileItems = servletFileUpload.parseRequest(getRequestContext(httpExchange));

            httpExchange.getResponseHeaders().add("Content-type", "text/plain");
            httpExchange.sendResponseHeaders(200, 0);
            @Cleanup OutputStream outputStream = httpExchange.getResponseBody();

            for (FileItem fileItem : fileItems) {
                String result = checkNewFile(fileItem);

                if(result == null){
                    File file = getUploadFileService().processFile(fileItem);
                    result = getUploadResult(file);
                }

                outputStream.write(result.getBytes());
                log.info("File-Item: " + fileItem.getFieldName() + " = " + fileItem.getName());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private String checkNewFile(FileItem fileItem) {
        String fileExtension = Files.getFileExtension(fileItem.getName()).toLowerCase();
        return String.valueOf(fileExtension.equals("doc") || fileExtension.equals("docx"));
    }

    private String getUploadResult(File file) {
        UploarResut resut = new UploarResut();
        if (file != null && file.exists()) {
            resut.name(file.getName())
                    .size(file.length())
                    .thumbnailUrl("file/" + file.getName())
                    .type("application/pdf")
                    .url("file/" + file.getName());
        }
        List<UploarResut> uploarResuts = new ArrayList<UploarResut>();
        uploarResuts.add(resut);
        Map<String, List> results = new HashMap<String, List>();
        results.put("files", uploarResuts);

        return getGson().toJson(results);
    }

    private String getErrorResultFileExtension(String fileExtension){
        return getUploadResuls(new UploarResut().error(" Расширение: " + fileExtension + ", не поддерживается! Только doc и docx"));
    }

    private String getUploadResuls(UploarResut resut){
        List<UploarResut> uploarResuts = new ArrayList<UploarResut>();
        uploarResuts.add(resut);
        Map<String, List> results = new HashMap<String, List>();
        results.put("files", uploarResuts);
        return getGson().toJson(results);
    }

    private RequestContext getRequestContext(final HttpExchange httpExchange) {
        return new RequestContext() {
            @Override
            public String getCharacterEncoding() {
                return "UTF-8";
            }

            @Override
            public int getContentLength() {
                return 0;
            }

            @Override
            public String getContentType() {
                return httpExchange.getRequestHeaders().getFirst("Content-type");
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return httpExchange.getRequestBody();
            }
        };
    }
}