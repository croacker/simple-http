package ru.croc.test;

import java.io.IOException;

import ru.croc.test.http.AppHttpServer;

/**
 *
 */
public class AsposeTest {



    public static void main(String[] args) throws IOException {
        AppHttpServer httpServer = new AppHttpServer();
        httpServer.start();
    }

}
