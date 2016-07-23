package ru.croc.test.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.croc.test.service.HtmlService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class IndexHttpHandler implements HttpHandler {

    private HtmlService getHtmlService(){
        return HtmlService.getInstance();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
