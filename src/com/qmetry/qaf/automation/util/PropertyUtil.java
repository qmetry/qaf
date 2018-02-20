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

package com.qmetry.qaf.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertyConverter;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.data.Base64PasswordDecryptor;
import com.qmetry.qaf.automation.data.PasswordDecryptor;
import com.qmetry.qaf.automation.keys.ApplicationProperties;

/**
 * com.qmetry.qaf.automation.util.PropUtil.java
 * 
 * @author chirag.jayswal
 */
public class PropertyUtil extends XMLConfiguration {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8633909707831110230L;
	Log logger = LogFactoryImpl.getLog(PropertyUtil.class);

	public PropertyUtil() {
		super();
		setLogger(logger);
		setDelimiterParsingDisabled(true);
		Iterator<Entry<Object, Object>> iterator = System.getProperties().entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();

			String skey = String.valueOf(entry.getKey());
			String sval = String.valueOf(entry.getValue());
			if (!StringMatcher.like("^(sun\\.|java\\.).*").match(skey)) {
				Object[] vals = sval != null && sval.indexOf(getListDelimiter()) >= 0
						? sval.split(getListDelimiter() + "") : new Object[] { sval };
				for (Object val : vals) {
					super.addPropertyDirect(skey, val);
				}
			}
		}
	}

	@Override
	protected void addPropertyDirect(String key, Object value) {
		if (!System.getProperties().containsKey(key)) {
			if (key.toLowerCase().startsWith("system.")) {
				super.addPropertyDirect(key, value);
				key = key.substring(key.indexOf(".") + 1);
				System.setProperty(key, (String) value);
			}
			super.addPropertyDirect(key, value);
		} else {
			String sysVal = System.getProperty(key);
			if (!sysVal.equalsIgnoreCase(value.toString())) {
				logger.debug("property [" + key + "] value [" + value
						+ "] ignored! It is overriden with System provided value: [" + sysVal + "]");
			}
		}

		if (key.toLowerCase().startsWith(ApplicationProperties.ENCRYPTED_PASSWORD_KEY_PREFIX.key)) {
			String decryptedValueKey = key.substring(ApplicationProperties.ENCRYPTED_PASSWORD_KEY_PREFIX.key.length());
			PasswordDecryptor passwordDecryptor = getPasswordDecryptor();
			String decryptedValue = passwordDecryptor.getDecryptedPassword((String) value);
			addPropertyDirect(decryptedValueKey, decryptedValue);
			logger.info("Added property [" + decryptedValueKey + "] with decrypted value using "
					+ passwordDecryptor.getClass().getSimpleName());
		} else if (ApplicationProperties.PASSWORD_DECRYPTOR_IMPL.key.equalsIgnoreCase(key)) {
			// update decrypted value for encrypted keys for existing keyes
			String prefix = ApplicationProperties.ENCRYPTED_PASSWORD_KEY_PREFIX.key.replace(".", "");
			Iterator<?> encryptedValueKeys = getKeys(prefix);
			while (encryptedValueKeys.hasNext()) {
				PasswordDecryptor passwordDecryptor = getPasswordDecryptor();

				String encryptedValueKey = (String) encryptedValueKeys.next();
				String decryptedValueKey = encryptedValueKey
						.substring(ApplicationProperties.ENCRYPTED_PASSWORD_KEY_PREFIX.key.length());
				String decryptedValue = passwordDecryptor.getDecryptedPassword(getString(encryptedValueKey));
				addPropertyDirect(decryptedValueKey, decryptedValue);
				logger.info("Updated property [" + decryptedValueKey + "] with decrypted value using "
						+ passwordDecryptor.getClass().getSimpleName());
			}
		}else if(ApplicationProperties.HTTPS_ACCEPT_ALL_CERT.key.equalsIgnoreCase(key) && getBoolean(key)){
			try {
				logger.info("Seeting behavior to accept all certitificate and host name");
				ignoreSSLCetrificatesAndHostVerification();
			} catch (KeyManagementException e) {
				logger.error("Unable to set behavior to ignore certificate and host name verification", e);
			} catch (NoSuchAlgorithmException e) {
				logger.error("Unable to find Algorithm while setting ignore certificate and host name verification", e);
			}
		}
	}

	public PropertyUtil(PropertyUtil prop) {
		this();
		append(prop);
	}

	public PropertyUtil(String... file) {
		this();
		load(file);
	}

	public void addAll(Map<String, ?> props) {
		boolean b = props.keySet().removeAll(System.getProperties().keySet());
		if (b) {
			logger.debug("Found one or more system properties which will not modified");
		}
		copy(new MapConfiguration(props));
	}

	public PropertyUtil(File... file) {
		this();
		load(file);
	}

	public boolean load(String... files) {
		boolean r = true;
		for (String file : files) {

			file = getSubstitutor().replace(file);
			loadFile(new File(file));

		}
		return r;
	}

	public boolean load(File... files) {
		boolean r = true;
		for (File file : files) {
			loadFile(file);
		}
		return r;
	}

	private boolean loadFile(File file) {
		try {
			if (file.getName().endsWith("xml") || file.getName().contains(".xml.")) {
				load(new FileInputStream(file));
			} else {
				PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
				propertiesConfiguration.setEncoding(getString(ApplicationProperties.LOCALE_CHAR_ENCODING.key, "UTF-8"));
				propertiesConfiguration.load(new FileInputStream(file));
				copy(propertiesConfiguration);
				propertiesConfiguration.clear();
			}
			return true;
		} catch (ConfigurationException e) {
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	/**
	 * load property inside java/jar package
	 * 
	 * @param cls
	 * @param propertyFile
	 * @return
	 */
	public boolean load(Class<?> cls, String propertyFile) {
		boolean success = false;
		InputStream in = null;
		try {
			in = cls.getResourceAsStream(propertyFile);
			load(in);
			success = true;
		} catch (Exception e) {
			logger.error("Unable to load properties from file:" + propertyFile, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return success;
	}

	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public String[] getStringArray(String key, String... defaultValue) {
		String[] retVal = super.getStringArray(key);
		return (retVal != null) && (retVal.length > 0) ? retVal : defaultValue == null ? new String[] {} : defaultValue;
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultValue) {
		try {
			String sVal = getString(key, "").trim();
			boolean val = StringUtil.booleanValueOf(sVal, defaultValue);
			return val;
		} catch (Exception e) {
			return super.getBoolean(key, defaultValue);
		}
	}

	public Object getObject(String key) {
		return super.getProperty(key);
	}

	/**
	 * @param sPropertyName
	 * @return property-key value if key presents or key otherwise.
	 */
	public String getPropertyValue(String sPropertyName) {
		return getString(sPropertyName, sPropertyName);
	}

	/**
	 * @param sPropertyName
	 * @return property-key value if key presents or null otherwise
	 */
	public String getPropertyValueOrNull(String sPropertyName) {
		return getString(sPropertyName);
	}

	public void storePropertyFile(File f) {
		try {
			save(f);
		} catch (ConfigurationException e) {
			logger.error(e.getMessage());
		}
	}

	// don't add but overwrite
	/**
	 * this will overwrite existing value if any
	 */
	@Override
	public void addProperty(String key, Object value) {
		clearProperty(key);
		super.addProperty(key, value);
	}

	@Override
	public void setProperty(String key, Object value) {
		// allow List Delimiter for string value
		if (null != value && value instanceof String) {
			if (value.toString().indexOf(getListDelimiter()) > 0) {
				value = PropertyConverter.split(value.toString(), getListDelimiter());
			}
		}
		super.setProperty(key, value);
	}

	/**
	 * Add a property to the configuration. If it already exists then the value
	 * stated here will be added to the configuration entry. For example, if the
	 * property: resource.loader = file is already present in the configuration
	 * and you call addProperty("resource.loader", "classpath") Then you will
	 * end up with a List like the following: ["file", "classpath"] Specified
	 * by: addProperty(...) in Configuration Parameters: key The key to add the
	 * property
	 * 
	 * @param key
	 * @param value
	 */
	public void editProperty(String key, Object value) {
		super.addProperty(key, value);
	}

	// clear property if it is not system property
	@Override
	public void clearProperty(String key) {
		if (!System.getProperties().containsKey(key)) {
			super.clearProperty(key);
		} else {
			logger.debug("clear system property ignored:" + key);
		}
	}

	public PasswordDecryptor getPasswordDecryptor() {
		String implName = getString(ApplicationProperties.PASSWORD_DECRYPTOR_IMPL.key);
		if (StringUtil.isBlank(implName)) {
			return new Base64PasswordDecryptor();
		} else {
			try {
				return (PasswordDecryptor) Class.forName(implName).newInstance();
			} catch (Exception e) {
				throw new AutomationError("Unable to get instance of PasswordDecryptor implementation", e);
			}
		}
	}

	private static void ignoreSSLCetrificatesAndHostVerification() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("SSL");

		// set up a TrustManager that trusts everything
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				System.out.println("======== AcceptedIssuers =============");
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
				System.out.println("========= ClientTrusted =============");
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
				System.out.println("======== ServerTrusted =============");
			}
		} }, new SecureRandom());

		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}
}
