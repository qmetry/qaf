package com.qmetry.qaf.automation.ui.playwright;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
//import com.qmetry.qaf.automation.ui.webdriver.CommandTracker;

public class PlaywrightCommandLogger implements PlaywrightCommandListener {
	private ArrayList<LoggingBean> commandLog;
	private final Log logger = LogFactory.getLog(getClass());
	private Set<String> excludeCommandsFromLogging;

	public PlaywrightCommandLogger(ArrayList<LoggingBean> commandLog) {
		this.commandLog = commandLog;
		excludeCommandsFromLogging = new HashSet<String>(Arrays.asList(new String[] { "getHtmlSource",
				"captureEntirePageScreenshotToString", "executeScript", "screenshot" }));
		excludeCommandsFromLogging
				.addAll(Arrays.asList(ApplicationProperties.REPORTER_LOG_EXCLUDE_CMD.getStringVal("").split(",")));
	}
	
	
	
}
