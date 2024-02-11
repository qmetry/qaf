package com.qmetry.qaf.automation.ui.playwright;

import static com.microsoft.playwright.impl.PlaywrightUtils.createPlaywright;
import static com.microsoft.playwright.impl.PlaywrightUtils.getBrowserTypeFromEnv;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qmetry.qaf.automation.core.QAFTestBase.STBArgs;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.AbstractTestBase;
public class PlaywrightTestBase  extends AbstractTestBase<PlaywrightDriver> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase#tearDown()
	 */
	

	public PlaywrightTestBase() {
		super(new PlaywrightDriverFactory());
	}

	@Override
	public PlaywrightDriver getDriver() {
		
		if (!getBase().hasDriver()) {
			PlaywrightListenerHandler handler = new PlaywrightListenerHandler();
			Playwright playwright = createPlaywright(null, false, handler);//createPlaywright(null, false);//Playwright.create();
		    BrowserType browserType = getBrowserTypeFromEnv(playwright);
			Browser browser = browserType.launch();
	        Page page = browser.newPage();
			PlaywrightDriver uiDriver = new PlaywrightDriver(playwright,browser,page);
			
			String driverName = ApplicationProperties.DRIVER_NAME.getStringVal("");
			if (ApplicationProperties.DRIVER_NAME.getStringVal("").equalsIgnoreCase("")) {
				System.err.println("Driver not configured!... \nUsing " + STBArgs.browser_str.getDefaultVal()
						+ " as default value. Please configure driver to be used using '"
						+ ApplicationProperties.DRIVER_NAME.key + "' property");
				driverName = STBArgs.browser_str.getDefaultVal();
			}
			getBase().setDriver(driverName, uiDriver);
		}
		

		return ((PlaywrightDriver) getBase().getUiDriver());
	}

	@Override
	protected void launch(String baseurl) {
		getDriver().getPage().navigate(baseurl);

	}
	

}
