package ru.croc.test.http;

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
            List<FileItem> result = servletFileUpload.parseRequest(getRequestContext(httpExchange));

            httpExchange.getResponseHeaders().add("Content-type", "text/plain");
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream outputStream = httpExchange.getResponseBody();

            for (FileItem fileItem : result) {

                File file = asposeService.processFile(fileItem.getName(), fileItem.getInputStream());
                String json = getUploadResult(file);
                outputStream.write(json.getBytes());

                log.info("File-Item: " + fileItem.getFieldName() + " = " + fileItem.getName());
            }
            outputStream.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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
        private String name = "Go5A302w7os.jpg";
        @Getter
        @Setter
        private long size = 230268;
        @Getter
        @Setter
        private String thumbnailUrl = "http://jquery-file-upload.appspot.com/image%2Fjpeg/2048386607/Go5A302w7os.jpg.80x80.jpg";
        @Getter
        @Setter
        private String type = "image/jpeg";
        @Getter
        @Setter
        private String url = "http://jquery-file-upload.appspot.com/image%2Fjpeg/2048386607/Go5A302w7os.jpg";
    }
}