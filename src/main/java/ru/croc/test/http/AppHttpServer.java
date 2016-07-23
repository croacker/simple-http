package ru.croc.test.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.croc.test.service.ConfigurationService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AppHttpServer {

    private Map<String, HttpHandler> routes = new HashMap<String, HttpHandler>();

    private int port;

    private ConfigurationService getConfigurationService(){
        return ConfigurationService.getInstance();
    }

    public AppHttpServer(){
        init();
    }

    public void start() throws IOException {
        HttpServer server = createServer();
        server.start();
    }

    private void init(){
        routes.put("/", new IndexHttpHandler());
        routes.put("/upload", new UploadHttpHandler());
        routes.put("/css", new StaticHttpHandler());
        routes.put("/js", new StaticHttpHandler());
        routes.put("/fonts", new StaticHttpHandler());
        routes.put("/img", new StaticHttpHandler());
        port = Integer.parseInt(getConfigurationService().get("server.port"));
    }

    private HttpServer createServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        for (Map.Entry<String, HttpHandler> route: routes.entrySet()){
            server.createContext(route.getKey(), route.getValue());
        }
        server.setExecutor(null);
        return server;
    }
}
