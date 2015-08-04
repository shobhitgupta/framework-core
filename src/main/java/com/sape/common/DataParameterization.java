package com.sape.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import com.sape.exceptions.AutomationFrameworkException;

public class DataParameterization {
    private static final Logger LOG = Logger.getLogger(DataParameterization.class);

    private DataParameterization() {

    }

    static public List<List<String>> getEqualDivisionsOfList(List<String> mainStrings, int parts) {
        int quotient = mainStrings.size() / parts;
        if (quotient == 0) {
            throw new AutomationFrameworkException("Number of elements in main String : " + mainStrings.size()
                    + " is less than number of parts expected : " + parts);
        }
        int remainder = mainStrings.size() % parts;
        List<List<String>> finalList = new ArrayList<List<String>>();
        int counter = 0;
        for (int i = 0; i < parts; i++) {
            int size = i < remainder ? quotient + 1 : quotient;
            List<String> listToAdd = mainStrings.subList(counter, counter + size);
            finalList.add(listToAdd);
            counter = counter + size;
        }
        return finalList;
    }

    @DataProvider
    public static Object[][] dataParameter(ITestNGMethod ctx) {
        try {
            String fileName = Constants.BASE_DIR + Config.Paths.SUITE_DIR + Constants.FS
                    + Config.Paths.TEST_DATA_DIR + Constants.FS + ctx.getXmlTest().getName() + "_" + Constants.DATA_EXCEL_SUFFIX
                    + ".xlsx";
            ExcelUtils exl = new ExcelUtils(fileName);
            exl.setSheet(ctx.getMethodName());

            int startRowNumber = 1;

            List<Integer> row = new ArrayList<Integer>();

            int col = exl.getHeaderCount(startRowNumber) - 1;
            while (exl.getCellData(startRowNumber, 0) != null) {
                if (Constants.TEST_FLOW_EXCEL_INCLUDE_INDICATOR.equals(exl.getCellData(startRowNumber, 0))) {
                    row.add(startRowNumber);
                }
                startRowNumber++;
            }

            String[][] data = new String[row.size()][col];
            for (int i = 0; i < row.size(); i++) {
                for (int j = 0; j < col; j++) {
                    data[i][j] = exl.getCellData(row.get(i), j + 1);
                }

            }
            return data;
        } catch (Exception e) {
            LOG.error("exception: ", e);
        }

        return new String[0][0];
    }
}
