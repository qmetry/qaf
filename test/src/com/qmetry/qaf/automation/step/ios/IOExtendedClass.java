package com.qmetry.qaf.automation.step.ios;

import com.qmetry.qaf.automation.step.common.BaseClass;
import com.qmetry.qaf.automation.step.QAFTestStep;

public class IOExtendedClass extends BaseClass {

	@QAFTestStep(description = "only in extended ios step")
	public String onlyInExtendedIOS() {
		System.out.println("IOS");
		return "Only In ios";
	}
}
