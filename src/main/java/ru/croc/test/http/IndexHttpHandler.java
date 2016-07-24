package ru.croc.test.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.croc.test.service.HtmlService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
@Component
@Slf4j
public class IndexHttpHandler implements HttpHandler {

    @Autowired @Getter
    private HtmlService htmlService;

    @Autowired @Getter
    private Gson gson;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        log.info("Process request:" + getGson().toJson(httpExchange.getRequestHeaders()));
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = httpExchange.getResponseBody();
        InputStream inputStream = getHtmlService().getIndex();
        final byte[] buffer = new byte[0x10000];
        int count;
        while ((count = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer,0,count);
        }
        inputStream.close();
        outputStream.close();
    }
}
