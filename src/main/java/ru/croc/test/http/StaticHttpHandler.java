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
@Component("staticHttpHandler")
@Slf4j
public class StaticHttpHandler extends FileHttpHandler {

    @Autowired @Getter
    private ResourceService resourceService;

    @Override
    protected InputStream getStream(String fileId){
        return getResourceService().get(fileId);
    }

}
