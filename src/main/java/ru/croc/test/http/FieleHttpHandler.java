package ru.croc.test.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.croc.test.service.ResourceService;

import java.io.*;

/**
 *
 */
@Component
@Slf4j
public class FieleHttpHandler implements HttpHandler {

    @Autowired @Getter
    private ResourceService resourceService;

    @Value("${server.pdf.folder}")
    @Getter
    private String pdfFolder;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String fileId = httpExchange.getRequestURI().getPath();
        log.info("Process request file:" + fileId);
        InputStream inputStream = getFileStream(fileId);
        if (inputStream != null) {
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream output = httpExchange.getResponseBody();
            final byte[] buffer = new byte[0x10000];
            int count;
            while ((count = inputStream.read(buffer)) >= 0) {
                output.write(buffer, 0, count);
            }
            output.flush();
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(inputStream);
        }else {
            writeError(httpExchange);
        }
    }

    private InputStream getFileStream(String fileId){
        InputStream inputStream = null;
        String fileName = getPdfFolder() + fileId.replace("file/", "");
        File file = new File(fileName);
        if(file.exists()){
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }
        return inputStream;
    }

    protected void writeError(HttpExchange httpExchange) throws IOException {
        String response = "Error 404 File not found.";
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream output = httpExchange.getResponseBody();
        output.write(response.getBytes());
        output.flush();
        IOUtils.closeQuietly(output);
    }
}
