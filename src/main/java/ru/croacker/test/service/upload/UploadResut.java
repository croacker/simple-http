package ru.croacker.test.service.upload;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.croacker.test.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Accessors(fluent = true)
public class UploadResut implements OperationResult {

    @Getter
    @Setter
    private String deleteType = "DELETE";
    @Getter
    @Setter
    private String deleteUrl = StringUtil.EMPTY;
    @Getter
    @Setter
    private String name = StringUtil.EMPTY;
    @Getter
    @Setter
    private long size = 0;
    @Getter
    @Setter
    private String thumbnailUrl = StringUtil.EMPTY;
    @Getter
    @Setter
    private String type = StringUtil.EMPTY;
    @Getter
    @Setter
    private String url = StringUtil.EMPTY;
    @Getter
    @Setter
    private String error = StringUtil.EMPTY;

    @Override
    public String toJson() {
        return new Gson().toJson(getWrap());
    }

    private Map getWrap(){
        List<UploadResut> uploadResuts = new ArrayList<UploadResut>();
        uploadResuts.add(this);
        Map<String, List> results = new HashMap<String, List>();
        results.put("files", uploadResuts);
        return results;
    }
}
