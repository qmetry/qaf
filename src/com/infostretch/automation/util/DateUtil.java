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


package com.infostretch.automation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * com.infostretch.automation.util.DateUtil.java
 * 
 * @author chirag
 */
public class DateUtil {
	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

	/**
	 * Convert date string from one format to another format.
	 * <p>
	 * <b>Example:</b>
	 * <ul>
	 * <li><code>
	 * formatDate("2012-01-11",
				"yyy-MM-dd", "MMM d, yyyy"))
	 * </code> will retrun "Jan 11, 2012"</li>
	 * </ul>
	 * </p>
	 * 
	 * @param dateStr
	 *            : date string to be formated
	 * @param formatFrom
	 *            : format of the given date string
	 * @param formatTo
	 *            : String expected format
	 * @return date string in expected format
	 */
	public static String getFormatedDate(String dateString, String formatFrom, String formatTo) {
		SimpleDateFormat aformat = new SimpleDateFormat(formatFrom);
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo);
		Date d;
		try {
			d = aformat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return eformat.format(d);
	}

	public static String getFormatedDate(Date date, String formatTo, String toLocale) {
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo, new Locale(toLocale));
		return eformat.format(date);
	}

	public static String getFormatedDate(String dateString, String formatFrom, String formatTo, String fromLocale,
			String toLocale) {
		SimpleDateFormat aformat = new SimpleDateFormat(formatFrom, new Locale(fromLocale));
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo, new Locale(toLocale));
		Date d;
		try {
			d = aformat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return eformat.format(d);
	}

	/**
	 * Get date for today, before/after today as string
	 * 
	 * @param days
	 *            : number of days to be added in current day.
	 * @param format
	 *            : (option) format in which require date string, default is
	 *            "MM/dd/yyyy"
	 * @return date string for expected day. Example:
	 *         <ol>
	 *         <li>getDate(0) will return today's date
	 *         <li>getDate(1) will return tomorrow's date
	 *         <li>getDate(-1) will return yesterday's date
	 *         </ol>
	 */
	public static String getDate(int days, String... format) {
		String outformat = (format == null) || (format.length < 1) ? DEFAULT_DATE_FORMAT : format[0];

		return new SimpleDateFormat(outformat).format(getDate(days));

	}

	/**
	 * Get date for today, before/after today. Example:
	 * <ol>
	 * <li>getDate(0) will return today's date
	 * <li>getDate(1) will return tomorrow's date
	 * <li>getDate(-1) will return yesterday's date
	 * </ol>
	 * 
	 * @param days
	 * @return date
	 */
	public static Date getDate(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	/**
	 * Get date after/before base date. Example:
	 * <ol>
	 * <li>getDate(baseDate, 5) will return 5 day's date after baseDate
	 * <li>getDate(baseDate, -5) will return 5 day's date before baseDate
	 * </ol>
	 * 
	 * @param date
	 * @param days
	 * @return date
	 */
	public static Date getDateAfter(Date date, int days) {
		Calendar cal = getCalendarDate(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	public static int getMonthsDifference(Date date1, Date date2) {

		Calendar cal1 = getCalendarDate(date1);
		Calendar cal2 = getCalendarDate(date2);

		int m1 = (cal1.get(Calendar.YEAR) * 12) + cal1.get(Calendar.MONTH);
		int m2 = (cal2.get(Calendar.YEAR) * 12) + cal2.get(Calendar.MONTH);
		return m2 - m1;

	}

	/**
	 * Returns number of days between date1 and date2, such that date1 + days =
	 * date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDateDifference(Date date1, Date date2) {
		int oneDay = 1000 * 60 * 60 * 24;
		Calendar cal1 = getCalendarDate(date1);
		Calendar cal2 = getCalendarDate(date2);

		return (int) ((cal1.getTimeInMillis() - cal2.getTimeInMillis()) / oneDay);
	}

	public static Calendar getCalendarDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date parseDate(String dateStr, String format) throws ParseException {
		if (StringUtil.isBlank(format)) {
			format = DEFAULT_DATE_FORMAT;
		}
		return new SimpleDateFormat(format).parse(dateStr);
	}

	public static String getFormatedDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

}
