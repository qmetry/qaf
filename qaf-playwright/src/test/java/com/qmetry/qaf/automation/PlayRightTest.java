package com.qmetry.qaf.automation;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;
import com.qmetry.qaf.automation.ui.playwright.PlaywrightDriver;
import com.qmetry.qaf.automation.ui.playwright.PlaywrightTestCase;
import com.qmetry.qaf.automation.ui.playwright.Validator;

public class PlayRightTest extends PlaywrightTestCase {

	@Test
	public void testDriver() {
		PlaywrightDriver driver = getDriver();
		//String s = driver.takeScreenShot();
		driver.getPage().navigate("https://www.google.com");
		
		Locator l = driver.getPage().locator("[name=abcdefg]");
		Locator loc = driver.getPage().locator("[name=q]");
		loc.fill("qaf playright");
		l.dispatchEvent("submit");
		
		//driver.getPage().mainFrame().
		
		Validator.verifyThat(loc).isVisible();
		Validator.verifyThat(l).not().isVisible();

		//assertThat(true, Matchers.is(false));

	}

}
