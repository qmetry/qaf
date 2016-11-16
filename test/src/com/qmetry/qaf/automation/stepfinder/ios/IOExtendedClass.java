package com.qmetry.qaf.automation.stepfinder.ios;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.stepfinder.common.BaseClass;

public class IOExtendedClass extends BaseClass {

	@QAFTestStep(description = "only in extended ios step")
	public String onlyInExtendedIOS() {
		System.out.println("IOS");
		return "Only In ios";
	}
}
