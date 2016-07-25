package ru.croc.test.http;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.croc.test.service.AsposeService;
import ru.croc.test.util.StringUtil;

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
@Component
@Slf4j
public class UploadHttpHandler implements HttpHandler {

    @Autowired
    @Getter
    private Gson gson;

    @Autowired
    @Getter
    private AsposeService asposeService;


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
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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
            OutputStream outputStream = httpExchange.getResponseBody();

            for (FileItem fileItem : fileItems) {
                String result;

                if(supportedFileExtension(fileItem.getName())){
                    File file = getAsposeService().processFile(fileItem.getName(), fileItem.getInputStream());
                    result = getUploadResult(file);
                }else {
                    result = getErrorResultFileExtension(Files.getFileExtension(fileItem.getName()).toLowerCase());
                }

                outputStream.write(result.getBytes());
                log.info("File-Item: " + fileItem.getFieldName() + " = " + fileItem.getName());
            }
            outputStream.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean supportedFileExtension(String fileName) {
        String fileExtension = Files.getFileExtension(fileName).toLowerCase();
        return fileExtension.equals("doc") || fileExtension.equals("docx");
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

    @Accessors(fluent = true)
    private static class UploarResut {
        @Getter
        @Setter
        private String deleteType = "DELETE";
        @Getter
        @Setter
        private String deleteUrl = StringUtil.EMPTY;
        @Getter
        @Setter
        private String name = StringUtil.EMPTY;
        @Getter
        @Setter
        private long size = 0;
        @Getter
        @Setter
        private String thumbnailUrl = StringUtil.EMPTY;
        @Getter
        @Setter
        private String type = StringUtil.EMPTY;
        @Getter
        @Setter
        private String url = StringUtil.EMPTY;
        @Getter
        @Setter
        private String error = StringUtil.EMPTY;
    }
}