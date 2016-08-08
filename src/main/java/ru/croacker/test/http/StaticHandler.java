package ru.croacker.test.http;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.croacker.test.service.ResourceService;

import java.io.InputStream;

/**
 *
 */
@Component("staticHttpHandler")
@Slf4j
public class StaticHandler extends FileHandler {

    @Autowired @Getter
    private ResourceService resourceService;

    @Override
    protected InputStream getStream(String fileId){
        return getResourceService().get(fileId);
    }

}
