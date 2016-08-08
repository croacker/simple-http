package ru.croacker.test.service.upload;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.croacker.test.service.upload.OperationResult;
import ru.croacker.test.util.StringUtil;

/**
 *
 */
@Accessors(fluent = true)
public class UploarResut implements OperationResult {

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

}
