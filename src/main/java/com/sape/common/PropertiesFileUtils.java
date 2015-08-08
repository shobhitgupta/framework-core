package com.sape.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.sape.exceptions.AutomationFrameworkException;

public class PropertiesFileUtils {
    private static final Logger LOG = Logger.getLogger(PropertiesFileUtils.class);
    private static final String SOURCE_FILE = Constants.BASE_DIR + Constants.REPORTER_PROPS_TEMPLATE_FILE;
    private static final String DEST_FILE = Constants.BASE_DIR + Constants.REPORTING_PROP_FILE_NAME;

    private PropertiesFileUtils() {

    }

    public static void replaceRelativePathsTextFile() {
        LOG.info("properties template file for reporter: " + SOURCE_FILE);
        LOG.info("properties file for reporter: " + DEST_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(SOURCE_FILE));
                BufferedWriter bw = new BufferedWriter(new FileWriter(DEST_FILE))) {
            String curLine;
            while ((curLine = br.readLine()) != null) {
                curLine = curLine.contains("<BASE_DIR>") ? curLine.replace("<BASE_DIR>", Constants.BASE_DIR) : curLine;
                curLine = curLine.contains("<separator>") ? curLine.replace("<separator>", Constants.FS) : curLine;
                curLine = curLine.contains("\\") ? curLine.replace("\\", "\\\\") : curLine;

                bw.write(curLine);
                bw.newLine();
            }
            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            throw new AutomationFrameworkException("com.reporting1 properties file not found", e);
        } catch (IOException e) {
            throw new AutomationFrameworkException("problem reading com.reporting1 properties file", e);
        }
        return;

    }

}
