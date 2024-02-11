package com.qmetry.qaf.automation.ui.playwright;

import java.util.Base64;
import java.util.Map;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qmetry.qaf.automation.ui.UiDriver;

public class PlaywrightDriver implements UiDriver {

	private Page page;
	private Playwright playwright;
	private Browser browser;

	public PlaywrightDriver(Playwright playwright, Browser browser, Page page) {
		this.page=page;
		this.playwright = playwright;
		this.browser = browser;
	}
	

	@Override
	public String takeScreenShot() {
		return Base64.getEncoder().encodeToString(page.screenshot());
	}

	@Override
	public void stop() {
		page.close();
		playwright.close();
	}
	
	public Page getPage() {
		return page;
	}
	
	public Browser getBrowser() {
		return browser;
	}


	@Override
	public Map<String, Object> getActualCapabilites() {
		return null;
	}

}
