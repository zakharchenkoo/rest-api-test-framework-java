package com.alona.qa.dataproviders;

import com.alona.qa.utils.CsvReader;
import org.testng.annotations.DataProvider;

import java.util.List;

public class RepositoryDataProvider {
    @DataProvider(name = "publicRepositoriesFromCsv")
    public Object[][] publicRepositoriesFromCsv() {
        List<String[]> rows = CsvReader.readCsvFromResources("testdata/repositories.csv");
        Object[][] data = new Object[rows.size()][3];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0];
            data[i][1] = rows.get(i)[1];
            data[i][2] = rows.get(i)[2];
        }
        return data;
    }
}
