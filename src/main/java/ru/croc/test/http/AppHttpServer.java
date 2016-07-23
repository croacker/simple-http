package ru.croc.test.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.croc.test.ContextLoader;
import ru.croc.test.service.ConfigurationService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Component
@Slf4j
public class AppHttpServer {

    private Map<String, HttpHandler> routes = new HashMap<String, HttpHandler>();

    @Value("${server.port}")
    private Integer port;

    @Autowired @Getter
    private ConfigurationService configurationService;

    private static ApplicationContext getContext(){
        return ContextLoader.getInstance().getContext();
    }

    public void start() throws IOException {
        HttpServer server = createServer();
        server.start();
        log.info("Server startup at:" + port + " port");
    }

    private void init(){
        routes.put("/",  getContext().getBean(IndexHttpHandler.class));
        routes.put("/upload", getContext().getBean(UploadHttpHandler.class));
        routes.put("/css", getContext().getBean(StaticHttpHandler.class));
        routes.put("/js", getContext().getBean(StaticHttpHandler.class));
        routes.put("/fonts", getContext().getBean(StaticHttpHandler.class));
        routes.put("/img", getContext().getBean(StaticHttpHandler.class));
    }

    private HttpServer createServer() throws IOException {
        init();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        for (Map.Entry<String, HttpHandler> route: routes.entrySet()){
            server.createContext(route.getKey(), route.getValue());
        }
        server.setExecutor(null);
        return server;
    }
}
