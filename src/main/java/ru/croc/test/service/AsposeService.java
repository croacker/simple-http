package ru.croc.test.service;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aspose.words.BorderCollection;
import com.aspose.words.Document;
import com.aspose.words.FieldUpdateCultureSource;
import com.aspose.words.FontSettings;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.Shape;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(fileName);
                result = processFile(fileName, inputStream);
                log.info("Created result pdf-file: " + result.getAbsolutePath());
                IOUtils.closeQuietly();
            } catch (FileNotFoundException e) {
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }finally {
                IOUtils.closeQuietly(inputStream);
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
        OutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(targetFileName);
            copyedFromKsed(inputStream, outputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }finally {
            IOUtils.closeQuietly(outputStream);
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

    private void copyedFromKsed(InputStream source, OutputStream destination) {
        try {
            final Document attachment = new Document(source, null);
            FontSettings fs = new FontSettings();
            fs.setFontsFolder(fontFolder, true);
            fs.setDefaultFontName("Franklin Gothic Book");
            attachment.setFontSettings(fs);
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
