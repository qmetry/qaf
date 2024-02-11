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
package com.qmetry.qaf.automation.util;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.core.ConfigurationManager;

/**
 * Database class for DatabaseUtil Created on Jan 29, 2010
 * 
 * @author Chirag Jayswal
 */

public class DatabaseUtil {
	public static final String DB_CONNECTION_URL = "db.connection.url";
	public static final String DB_DRIVER_CLASS = "db.driver.class";
	public static final String DB_USER = "db.user";
	public static final String DB_PWD = "db.pwd";
    private static final Log log = LogFactoryImpl.getLog(DatabaseUtil.class);

	/**
	 * {@link PreparedStatement#close() close PreparedStatement} if not null ignoring exception if any
	 * @param ps
	 */
	public static void close(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.clearWarnings();
				ps.clearParameters();
				ps.close();
			}
		} catch (SQLException sqle) {
			log.error("An error occurred while attempting to close a PreparedStatement", sqle);
		}
	}

	/**
	 * {@link Statement#close() close Statement} if not null ignoring exception if any
 
	 * @param s
	 */
	public static void close(Statement s) {
		try {
			if (s != null) {
				s.close();
			}
		} catch (SQLException sqle) {
			log.error("An error occurred while attempting to close a SQLStatement", sqle);
		}
	}

	/**
	 * Utility method to close result-set and statement if not not and ignoring exception if any
	 * @param ps
	 * @param rs
	 */
	public static void close(PreparedStatement ps, ResultSet rs) {
		close(rs);
		close(ps);
	}

	/**
	 * Utility method to close result-set and statement if not not and ignoring exception if any
	 * @param st
	 * @param rs
	 */
	public static void close(Statement st, ResultSet rs) {
		close(rs);
		close(st);
	}

	/**
	 * {@link ResultSet#close() close result set} if not null ignoring exception if any
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException sqle) {
			log.error("An error occurred while attempting to close a ResultSet", sqle);
		}
	}

	/**
	 * {@link Connection#close() close connection} if not null ignoring exception if any

	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException se) {
			log.error("An error occurred while attempting to close a database connection", se);
		}
	}

	/**
	 * For default data base configuration, Creates data base connection using following properties:
	 * <ol>
	 * <li>db.driver.class</li>
	 * <li>db.connection.url</li>
	 * <li>db.user</li>
	 * <li>db.pwd</li>
	 * </ol>
	 * 
	 * @return {@link Connection} object
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		return getConnection("");
	}

	/**
	 * If you want to use multiple data bases in the project. provide different configuration with different prefix. For example
	 * <p>defualt</p>
	 * <ol>
	 * <li>db.driver.class</li>
	 * <li>db.connection.url</li>
	 * <li>db.user</li>
	 * <li>db.pwd</li>
	 * </ol>
	 * <p>another database configuration</p>
	 * <ol>
	 * <li>con1.db.driver.class</li>
	 * <li>con1.db.connection.url</li>
	 * <li>con1.db.user</li>
	 * <li>con1.db.pwd</li>
	 * </ol>
	 * to use another configuration: <code>{@link #getConnection(String) getConnection("con1")}
	 * @param prefix - prefix of the database configuration to be used
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(String prefix) throws Exception {
		Configuration props = StringUtil.isBlank(prefix) ? ConfigurationManager.getBundle()
				: ConfigurationManager.getBundle().subset(prefix);
		String url = props.getString(DB_CONNECTION_URL);
		String driverclass = props.getString(DB_DRIVER_CLASS);
		String user = props.getString(DB_USER);
		String pwd = props.getString(DB_PWD);
		return getConnection(driverclass, url, user, pwd);
	}

	/**
	 * @param driverCls
	 * @param url
	 * @param user
	 * @param pwd
	 * @return {@link Connection} object
	 * @throws Exception
	 */
	public static Connection getConnection(String driverCls, String url, String user, String pwd) throws Exception {
		Connection con = null;
		Class.forName(driverCls);// loads the driver
		con = DriverManager.getConnection(url, user, pwd);
		return con;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static Object[][] getData(String query) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				int colsCnt = rs.getMetaData().getColumnCount();
				Object[] cols = new Object[colsCnt];
				for (int indx = 0; indx < colsCnt; indx++) {
					cols[indx] = getValue(rs, indx + 1);
				}
				rows.add(cols);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}

		return rows.toArray(new Object[][] {});
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static Object[][] getRecordDataAsMap(String query) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				HashMap<String, Object> map = new LinkedHashMap<String, Object>();

				int colsCnt = rs.getMetaData().getColumnCount();
				for (int indx = 1; indx <= colsCnt; indx++) {
					map.put(rs.getMetaData().getColumnLabel(indx), getValue(rs, indx));
				}
				rows.add(new Object[] { map });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}
		return rows.toArray(new Object[][] {});
	}
	
	/**
	 * use this method when you want to run query using default database configuration
	 * @param query
	 * @return
	 */
	public static List<Map<String, Object>> getRecordAsMap(String query) {
		return getRecordAsMap("", query);
	}

	/**
	 * Use this method if you have multiple database configuration. Provide prefix of the configuration to be used. 
	 * @see #getConnection(String)
	 * @param connectionPrefix
	 * @param query
	 * @return
	 */
	public static List<Map<String, Object>> getRecordAsMap(String connectionPrefix,String query) {
		ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection(connectionPrefix);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				Map<String, Object> map = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);

				int colsCnt = rs.getMetaData().getColumnCount();
				for (int indx = 1; indx <= colsCnt; indx++) {
					map.put(rs.getMetaData().getColumnLabel(indx), getValue(rs,indx));
				}
				rows.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}
		return rows;
	}
	
	private static Object getValue(ResultSet rs, int colIndex) throws SQLException {
		Object oVal = rs.getObject(colIndex);
		try {
			if(oVal instanceof Blob) {
				oVal = rs.getBytes(colIndex);
			}else if(oVal instanceof Clob) {
				oVal = rs.getString(colIndex);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return oVal;
	}
}
