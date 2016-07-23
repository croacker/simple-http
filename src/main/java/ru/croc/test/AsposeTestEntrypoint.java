package ru.croc.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import ru.croc.test.http.AppHttpServer;

import java.io.IOException;

/**
 *
 */
@Slf4j
public class AsposeTestEntrypoint {

    private static ApplicationContext getContext(){
        return ContextLoader.getInstance().getContext();
    }

    public static void main(String[] args) throws IOException {
        loadContext();
        getContext().getBean(AsposeTestEntrypoint.class).start();
    }

    private void start() throws IOException {
        log.info("Start Http-server.");
        getContext().getBean(AppHttpServer.class).start();
    }

    private static void loadContext(){
        log.info("Load application context...");
        ContextLoader.getInstance().load();
    }
}
