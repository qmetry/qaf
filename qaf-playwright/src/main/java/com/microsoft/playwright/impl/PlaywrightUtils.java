/**
 * 
 */
package com.microsoft.playwright.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Playwright.CreateOptions;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.impl.driver.Driver;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.playwright.PlaywrightListenerHandler;
import com.qmetry.qaf.automation.util.ClassUtil;

/**
 * @author chirag
 *
 */
public final class PlaywrightUtils {

	/**
	 * 
	 */
	private PlaywrightUtils() {
	}

	public static Playwright createPlaywright(CreateOptions options, boolean forceNewDriverInstanceForTests, PlaywrightListenerHandler playwrightListenerHandler) {
		Map<String, String> env = Collections.emptyMap();
		if (options != null && options.env != null) {
			env = options.env;
		}
		Driver driver = forceNewDriverInstanceForTests ? Driver.createAndInstall(env, true)
				: Driver.ensureDriverInstalled(env, true);
		try {
			ProcessBuilder pb = driver.createProcessBuilder();
			pb.command().add("run-driver");
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			Process p = pb.start();
			Connection connection = new PWConnection(p.getInputStream(), p.getOutputStream(), env).withListener(playwrightListenerHandler);
			PlaywrightImpl result = connection.initializePlaywright();
			// result.driverProcess = p;
			ClassUtil.setField("driverProcess", result, p);
			result.initSharedSelectors(null);

			return result;
		} catch (IOException e) {
			throw new PlaywrightException("Failed to launch driver", e);
		}
	}
	
	public static BrowserType getBrowserTypeFromEnv(Playwright playwright) {
	    String browserName = ApplicationProperties.DRIVER_NAME.getStringVal(System.getenv("BROWSER"));

	    if (browserName == null) {
	      browserName = "chromium";
	    }

	    switch (browserName) {
	      case "webkit":
	        return playwright.webkit();
	      case "firefox":
	        return playwright.firefox();
	      case "chromium":
	        return playwright.chromium();
	      default:
	        throw new IllegalArgumentException("Unknown browser: " + browserName);
	    }
	  }

}
