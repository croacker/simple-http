package ru.croc.test.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.croc.test.service.ResourceService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
@Component
@Slf4j
public class StaticHttpHandler implements HttpHandler {

    @Autowired @Getter
    private ResourceService resourceService;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String fileId = httpExchange.getRequestURI().getPath();
        log.info("Process request file:" + fileId);
        InputStream inputStream = getResourceService().get(fileId);
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

    protected void writeError(HttpExchange httpExchange) throws IOException {
        String response = "Error 404 File not found.";
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream output = httpExchange.getResponseBody();
        output.write(response.getBytes());
        output.flush();
        IOUtils.closeQuietly(output);
    }
}
