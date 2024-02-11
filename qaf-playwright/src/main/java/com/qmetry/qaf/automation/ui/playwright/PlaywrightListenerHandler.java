package com.qmetry.qaf.automation.ui.playwright;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.QAFListener;
import com.qmetry.qaf.automation.keys.ApplicationProperties;

public class PlaywrightListenerHandler implements PlaywrightCommandListener {
	private static final Log logger = LogFactoryImpl.getLog(PlaywrightListenerHandler.class);
	private final Collection<PlaywrightCommandListener> listeners;
	private PlaywrightDriver driver;

	public PlaywrightListenerHandler() {
		listeners = getDriverListeners();
	}
	
	
	public void beforeCommand(PlaywrightCommandTracker commandTracker) {
		for(PlaywrightCommandListener listener : listeners) {
			listener.beforeCommand(driver,commandTracker);
		}
	}
	
	public void afterCommand(PlaywrightCommandTracker commandTracker) {
		for(PlaywrightCommandListener listener : listeners) {
			listener.afterCommand(driver,commandTracker);
		}
	}
	
	public void onFailure(PlaywrightCommandTracker commandTracker) {
		for(PlaywrightCommandListener listener : listeners) {
			listener.onFailure(driver,commandTracker);
			if (!commandTracker.hasException()) {
				//handled by listener
				break;
			}
		}
	}
	
	public void onInitialize(PlaywrightDriver uiDriver) {
		this.driver=uiDriver;
		for(PlaywrightCommandListener listener : listeners) {
			listener.onInitialize(uiDriver);
		}
	}

	public void onInitializationFailure(LaunchOptions launchOptions, Throwable e) {
		for(PlaywrightCommandListener listener : listeners) {
			listener.onInitializationFailure(launchOptions, e);
		}
	}

	public void beforeInitialize(LaunchOptions launchOptions) {
		for(PlaywrightCommandListener listener : listeners) {
			listener.beforeInitialize(launchOptions);
		}
	}

	private static Collection<PlaywrightCommandListener> getDriverListeners() {
		LinkedHashSet<PlaywrightCommandListener> listners = new LinkedHashSet<PlaywrightCommandListener>();
		String[] clistners = ConfigurationManager.getBundle()
				.getStringArray(ApplicationProperties.WEBDRIVER_COMMAND_LISTENERS.key);
		for (String listenr : clistners) {
			try {
				PlaywrightCommandListener cls = (PlaywrightCommandListener) Class.forName(listenr).newInstance();
				listners.add(cls);
			} catch (Exception e) {
				logger.error("Unable to register listener class " + listenr, e);
			}
		}
		clistners = ConfigurationManager.getBundle().getStringArray(ApplicationProperties.QAF_LISTENERS.key);
		for (String listener : clistners) {
			try {
				QAFListener cls = (QAFListener) Class.forName(listener).newInstance();
				if (PlaywrightCommandListener.class.isAssignableFrom(cls.getClass()))
					listners.add((PlaywrightCommandListener) cls);
			} catch (Exception e) {
				logger.error("Unable to register class as driver listener:  " + listener, e);
			}
		}
		return listners;
	}
}
