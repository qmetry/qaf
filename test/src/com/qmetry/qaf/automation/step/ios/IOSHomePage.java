package com.qmetry.qaf.automation.step.ios;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.common.HomePage;

public class IOSHomePage implements HomePage {

	@Override
	public String openApp() {
		System.out.println("IOS App Open");
		return "IOS";
	}

	@QAFTestStep(description = "only in ios")
	public String onlyInIOS() {
		System.out.println("IOS");
		return "Only In ios";
	}

	@QAFTestStep(description = "in both")
	public String inBoth() {
		System.out.println("IOS");
		return "IOS";
	}
}
