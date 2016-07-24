package ru.croc.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import ru.croc.test.http.AppHttpServer;
import ru.croc.test.service.AsposeService;
import ru.croc.test.service.CommandLineService;
import ru.croc.test.util.StringUtil;

import java.io.IOException;

/**
 *
 */
@Slf4j
public class AsposeTestEntrypoint {

    private static ApplicationContext getContext() {
        return ContextLoader.getInstance().getContext();
    }

    public static void main(String[] args) throws IOException {
        loadContext();
        AsposeTestEntrypoint application = getContext().getBean(AsposeTestEntrypoint.class);
        if (args.length != 0){
            application.processCommandline(args);
        }else {
            application.start();
        }
    }

    private void processCommandline(String...args) {
        String fileName = getContext().getBean(CommandLineService.class).getFileName(args);
        if(!StringUtil.isEmpty(fileName)){
            log.info("Convert file:" + fileName + " to pdf...");
            getContext().getBean(AsposeService.class).processFile(fileName);
        }

    }

    private void start() throws IOException {
        log.info("Start Http-server.");
        getContext().getBean(AppHttpServer.class).start();
    }

    private static void loadContext() {
        log.info("Load application context...");
        ContextLoader.getInstance().load();
    }

}
