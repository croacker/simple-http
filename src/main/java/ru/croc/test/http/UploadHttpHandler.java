package ru.croc.test.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;

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

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if(httpExchange.getRequestMethod().equals("POST")){
            uploadFile(httpExchange);
        }else {
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

    private void uploadFile(final HttpExchange httpExchange){
        for(Map.Entry<String, List<String>> header : httpExchange.getRequestHeaders().entrySet()) {
            log.info(header.getKey() + ": " + header.getValue().get(0));
        }
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        try {
            ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
            List<FileItem> result = servletFileUpload.parseRequest(new RequestContext() {

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

            });
            httpExchange.getResponseHeaders().add("Content-type", "text/plain");
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            for(FileItem fileItem : result) {
                List<UploarResut> uploarResuts = new ArrayList<UploarResut>();
                uploarResuts.add(new UploarResut());
                Map<String, List> results = new HashMap<String, List>();
                results.put("files", uploarResuts);

                Gson gson = new Gson();
                String json = gson.toJson(results);
                outputStream.write(json.getBytes());

                log.info("File-Item: " + fileItem.getFieldName() + " = " + fileItem.getName());
            }
            outputStream.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static class UploarResut{
        private String deleteType = "DELETE";
        private String deleteUrl = "http://jquery-file-upload.appspot.com/image%2Fjpeg/2048386607/Go5A302w7os.jpg";
        private String name = "Go5A302w7os.jpg";
        private int size = 230268;
        private String thumbnailUrl = "http://jquery-file-upload.appspot.com/image%2Fjpeg/2048386607/Go5A302w7os.jpg.80x80.jpg";
        private String type = "image/jpeg";
        private String url = "http://jquery-file-upload.appspot.com/image%2Fjpeg/2048386607/Go5A302w7os.jpg";
    }
}