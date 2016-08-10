package ru.croacker.test.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.croacker.test.service.upload.UploadResut;
import ru.croacker.test.service.UploadFileService;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
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
                    result = getUploadFileService().processFile(fileItem).toJson();
                }

                outputStream.write(result.getBytes());
                log.info("File-Item: " + fileItem.getFieldName() + " = " + fileItem.getName());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private String checkNewFile(FileItem fileItem) {
        return null;
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