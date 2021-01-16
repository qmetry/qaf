/*******************************************************************************
 * Copyright (c) 2021 Infostretch Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.qmetry.qaf.automation.data;

import com.qmetry.qaf.automation.util.DateUtil;
import com.qmetry.qaf.automation.util.PoiExcelUtil;
import com.qmetry.qaf.automation.util.Validator;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.Map;

public class ExcelDataProviderTest {

    @Test(description = "Excel data provider without sheet")
    public void excelDataWithoutSheetName() {
        Object[][] actualResult = PoiExcelUtil.getExcelDataAsMap("resources/testdata.xlsx", "");
        Validator.assertThat("Rows:", actualResult.length, Matchers.equalTo(4));
        for (int i = 0; i < actualResult.length; i++) {
            @SuppressWarnings("rawtypes")
            Map map = (Map) actualResult[i][0];
            Validator.verifyThat("Columns:", map.keySet().size(), Matchers.equalTo(7));
            Validator.verifyThat("Id:", map.get("Id"), Matchers.equalTo((double) (i + 1)));
            Validator.verifyThat("FirstName:", map.get("firstname"), Matchers.equalTo("Test" + (i + 1)));
            Validator.verifyThat("LastName:", map.get("lastname"), Matchers.equalTo("L" + (i + 1)));
            Validator.verifyThat("Activated:", map.get("activated"), Matchers.equalTo((i % 2 == 0)));
            try {
                Validator.verifyThat("Created:", map.get("created"), Matchers.equalTo(DateUtil.parseDate((i + 1) + "/03/2021", "dd/MM/yy")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Validator.verifyThat("Version:", map.get("version"), Matchers.equalTo((1.0 + ((i + 1.0) / 10))));
            Validator.verifyThat("Email:", map.get("email"), Matchers.equalTo(String.format("test%d.l%d@test.com", (i + 1), (i + 1))));
        }
    }

    @Test(description = "Excel data provider with sheet name")
    public void excelDataWithSheetName() {
        Object[][] actualResult = PoiExcelUtil.getExcelDataAsMap("resources/testdata.xlsx", "AnotherSheet");
        Validator.assertThat("Rows:", actualResult.length, Matchers.equalTo(4));
        for (int i = 0; i < actualResult.length; i++) {
            @SuppressWarnings("rawtypes")
            Map map = (Map) actualResult[i][0];
            Validator.verifyThat("Columns:", map.keySet().size(), Matchers.equalTo(7));
            Validator.verifyThat("Id:", map.get("Id"), Matchers.equalTo((double) (i + 1)));
            Validator.verifyThat("FirstName:", map.get("firstname"), Matchers.equalTo("Test" + (i + 1)));
            Validator.verifyThat("LastName:", map.get("lastname"), Matchers.equalTo("L" + (i + 1)));
            Validator.verifyThat("Activated:", map.get("activated"), Matchers.equalTo((i % 2 == 0)));
            try {
                Validator.verifyThat("Created:", map.get("created"), Matchers.equalTo(DateUtil.parseDate((i + 1) + "/03/2021", "dd/MM/yy")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Validator.verifyThat("Version:", map.get("version"), Matchers.equalTo((1.0 + ((i + 1.0) / 10))));
            Validator.verifyThat("Email:", map.get("email"), Matchers.equalTo(String.format("test%d.l%d@test.com", (i + 1), (i + 1))));
        }
    }

    @Test(description = "Excel data provider with datatable")
    public void excelDataWithDataTable() {
        for (int j = 1; j <= 2; j++) {
            Object[][] actualResult = PoiExcelUtil.getTableDataAsMap("resources/testdata.xlsx", "Data" + j, "DataTable");
            Validator.assertThat("Rows:", actualResult.length, Matchers.equalTo(4));
            for (int k = 0; k < actualResult.length; k++) {
                @SuppressWarnings("rawtypes")
                Map map = (Map) actualResult[k][0];
                int i = (4 * (j - 1)) + k;
                Validator.verifyThat("Columns:", map.keySet().size(), Matchers.equalTo(7));
                Validator.verifyThat("Id:", map.get("Id"), Matchers.equalTo((double) (i + 1)));
                Validator.verifyThat("FirstName:", map.get("firstname"), Matchers.equalTo("Test" + (i + 1)));
                Validator.verifyThat("LastName:", map.get("lastname"), Matchers.equalTo("L" + (i + 1)));
                Validator.verifyThat("Activated:", map.get("activated"), Matchers.equalTo((i % 2 == 0)));
                try {
                    Validator.verifyThat("Created:", map.get("created"), Matchers.equalTo(DateUtil.parseDate((i + 1) + "/03/2021", "dd/MM/yy")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Validator.verifyThat("Version:", map.get("version"), Matchers.equalTo((1.0 + ((i + 1.0) / 10))));
                Validator.verifyThat("Email:", map.get("email"), Matchers.equalTo(String.format("test%d.l%d@test.com", (i + 1), (i + 1))));
            }
        }
    }
}
