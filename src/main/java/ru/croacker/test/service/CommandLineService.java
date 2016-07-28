package ru.croacker.test.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Service;
import ru.croacker.test.util.StringUtil;

import java.util.Arrays;

/**
 *
 */
@Service
@Slf4j
public class CommandLineService {

    public String getFileName(String... args) {
        String fileName = StringUtil.EMPTY;
        Options options = getCommandLineOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("f")) {
                fileName = commandLine.getOptionValue("f");
            } else {
                printHelp();
            }
        } catch (ParseException e) {
            log.error("Error parse command line args " + Arrays.toString(args));
            printHelp();
        }
        return fileName;
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("posix", getCommandLineOptions());
    }

    private static Options getCommandLineOptions() {
        Options options = new Options();
        return options.addOption("f", "file", true, "Please enter a command line option and the name of the file to convert to pdf");
    }

}
