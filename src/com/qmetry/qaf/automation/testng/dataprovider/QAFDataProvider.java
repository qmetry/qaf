/*******************************************************************************
 * Copyright (c) 2019 Infostretch Corporation
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
package com.qmetry.qaf.automation.testng.dataprovider;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Values of this annotation parameters can be overridden by providing property
 * <code>&lt;tc_name&gt;.testdata</code>=&lt;value&gt;
 * <p>
 * The value contains comma separated parameter and value combination:
 * <code>&lt;tc_name&gt;.testdata</code>=&lt;param&gt;=value,&lt;param&gt;=value
 * Supported parameters are
 * <ul>
 * <li>datafile : data file url, csv or excel file url
 * <li>sheetname: sheet name for excel data file, default is the first sheet
 * <li>labelname: name of label, if data start and end cell marked with label in
 * excel sheet
 * <li>hasheaderrow: true/false, indicates excel sheet has header row
 * <li>sqlquery: database query, will not be used if data file provided. Must
 * required other db properties
 * </ul>
 * com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.java
 * 
 * @author chirag
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ METHOD, TYPE })
public @interface QAFDataProvider {
	/**
	 * Represents meta-data for data provider
	 * 
	 * @author chirag
	 *
	 */
	public enum params {
		DATAFILE, SHEETNAME, KEY, HASHEADERROW, SQLQUERY, BEANCLASS, JSON_DATA_TABLE, DATAPROVIDER, DATAPROVIDERCLASS, FILTER, FROM, TO, INDICES;
	}

	public static final String NAME= "qaf-data-provider";
	public static final String NAME_PARALLEL = "qaf-data-provider-parallel";

	/**
	 * Used to provide csv or excel file. Can be overridden by property
	 * <code>&lt;tc_name&gt;.testdata</code>
	 * 
	 * @return
	 */
	String dataFile() default "";

	/**
	 * Optional sheet name (value or property) for excel file. If not provided
	 * first sheet will be considered. Can be overridden by property
	 * <code>&lt;tc_name&gt;.testdata</code>
	 * 
	 * @return
	 */
	String sheetName() default "";

	/**
	 * Optional flag to indicate excel data contains header row that need to be
	 * skipped. Default value is false. Can be overridden by property
	 * <code>&lt;tc_name&gt;.testdata</code>
	 * 
	 * @return
	 */
	boolean hasHeaderRow() default false;

	/***
	 * Optional data label name in excel sheet. Required if want to provide data
	 * start/end cell marked with label. Can be overridden by property
	 * <code>&lt;tc_name&gt;.testdata</code>
	 * 
	 * @return
	 */
	String key() default "";

	/**
	 * Used to provide database query. Will not be considered if data file is
	 * provided.
	 * 
	 * @return
	 */
	String sqlQuery() default "";

	/**
	 * Filter to apply on data set returned by the data-provider that returns
	 * List of Maps. It must represent logical expression that returns true or
	 * false. You can use map key as context variable and test meta-data as
	 * parameter. For example you test data has "first-name", "last-name", "age"
	 * and "country", you can have expression that filters records for age above
	 * 18 as below:<br/>
	 * <code>
	 * <ul>
	 * <li>filter="age>18"</li>
	 * <li>filter="country.equalsIgnoreCase('India') && age>18"</li>
	 * </ul>
	 * </code> <br/>
	 * As you can use test case meta-data, method name as "method" and class
	 * name as "class" as parameters, Another example of using test case
	 * meta-data in filter assuming test case meta-data has meta-data
	 * "testCaseID" and test data has "ID". <code>
	 * <ul>
	 * <li>filter="ID=='${testCaseID}'"</li>
	 * <li>filter="ID.equalsIgnoreCase(\"${method}\")"</li>
	 * </ul>
	 * </code>
	 * 
	 * @return
	 */
	String filter() default "";
}
