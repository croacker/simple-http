package ru.croc.test.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
    public void handle(HttpExchange exchange) throws IOException {
        String fileId = exchange.getRequestURI().getPath();
        log.info("Process request file:" + fileId);
        InputStream inputStream = getResourceService().get(fileId);
        if (inputStream == null) {
            String response = "Error 404 File not found.";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();
            output.close();
        } else {
            exchange.sendResponseHeaders(200, 0);
            OutputStream output = exchange.getResponseBody();
            final byte[] buffer = new byte[0x10000];
            int count;
            while ((count = inputStream.read(buffer)) >= 0) {
                output.write(buffer, 0, count);
            }
            output.flush();
            output.close();
            inputStream.close();
        }
    }
}
