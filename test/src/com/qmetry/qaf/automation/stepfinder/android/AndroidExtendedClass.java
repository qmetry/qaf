package com.qmetry.qaf.automation.stepfinder.android;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.stepfinder.common.BaseClass;

public class AndroidExtendedClass extends BaseClass {

	@QAFTestStep(description = "only in extended android step")
	public String onlyInExtendedAndroid() {
		System.out.println("Android");
		return "Only In android";
	}
}
