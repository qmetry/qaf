package com.qmetry.qaf.automation.step.android;

import com.qmetry.qaf.automation.step.common.HomePage;
import com.qmetry.qaf.automation.step.QAFTestStep;

public class AndroidHomePage implements HomePage {

	@Override
	public String openApp() {
		System.out.println("Android App Open");
		return "Android";
	}

	@QAFTestStep(description = "only in android")
	public String onlyInAndroid() {
		System.out.println("Android");
		return "Only In android";
	}

	@QAFTestStep(description = "in both")
	public String inBoth() {
		System.out.println("Android");
		return "Android";
	}

}
