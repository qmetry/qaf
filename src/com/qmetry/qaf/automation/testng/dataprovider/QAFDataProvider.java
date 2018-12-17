/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/

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
