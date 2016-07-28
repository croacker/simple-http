package ru.croacker.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.aspose.words.BorderCollection;
import com.aspose.words.Document;
import com.aspose.words.FieldUpdateCultureSource;
import com.aspose.words.FontSettings;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.Shape;
import com.google.common.io.Files;
import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@Slf4j
public class AsposeService {

    @Value("${server.pdf.folder}")
    @Getter
    private String pdfFolder;

    @Value("${server.font.folder}")
    @Getter
    private String fontFolder;

    public File processFile(String fileName) {
        File result = null;
        if(sourceExists(fileName)) {
            try {
                @Cleanup InputStream inputStream = new FileInputStream(fileName);
                result = processFile(fileName, inputStream);
                log.info("Created result pdf-file: " + result.getAbsolutePath());
                IOUtils.closeQuietly();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else {
            log.error("File: "+ fileName + " NOT found. Exception!");
        }
        return result;
    }

    public File processFile(String fileName, InputStream inputStream) throws IOException {
        String targetFileName = getTargetFileName(fileName);
        log.info("Aspose process file:" + fileName);
        log.info("Aspose font folder:" + fontFolder);
        log.info("Aspose try save file to :" + new File(targetFileName).getAbsolutePath());
        removeOld(targetFileName);
        return convertToPdf(targetFileName, inputStream);
    }

    private File convertToPdf(String targetFileName, InputStream inputStream){
        try{
            @Cleanup OutputStream outputStream = new FileOutputStream(targetFileName);
            copyedFromKsed(inputStream, outputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        File targetFile = new File(targetFileName);
        return targetFile;
    }

    private boolean sourceExists(String fileName) {
        return new File(fileName).exists();
    }

    private String getTargetFileName(String fileName){
        return getPdfFolder() + "/" + Files.getNameWithoutExtension(fileName) + ".pdf";
    }

    private void removeOld(String targetFileName) {
        File file = new File(targetFileName);
        if(file.exists()){
            file.delete();
        }
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
    }

    /**
     * Метод скопированый из приложения ТН
     * @param source
     * @param destination
     */
    private void copyedFromKsed(InputStream source, OutputStream destination) {
        try {
            final Document attachment = new Document(source, null);
            FontSettings fontSettings = new FontSettings();
            fontSettings.setFontsFolder(fontFolder, true);
            fontSettings.setDefaultFontName("Franklin Gothic Book");
            attachment.setFontSettings(fontSettings);
            /**
             * Начало блока кода отладки. Убрать при отсутствии в логах сообщений после периода эксплуатации
             */
            NodeCollection shapes = attachment.getChildNodes(NodeType.SHAPE, true);

            for (Object node : shapes) {
                Shape shape = (Shape) node;
                BorderCollection bc = shape.getImageData().getBorders();
                if (bc.getLeft().getLineWidth() > 0) {
                    bc.clearFormatting();
                    log.error("Shape has border for attachment");
                }
            }

            /**
             * Конец блока кода отладки.
             */

            attachment.getFieldOptions().setFieldUpdateCultureSource(FieldUpdateCultureSource.FIELD_CODE);
            try {
                attachment.acceptAllRevisions();
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            } // Показываем исправленную версию. На одном файле происходит падение. Пока перехвачено падение. следует
              // иметь ввиду, что возможно будет показана не исправленная версия
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            attachment.save(destination, saveOptions);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
