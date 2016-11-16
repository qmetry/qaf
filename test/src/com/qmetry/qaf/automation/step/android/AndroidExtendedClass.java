package com.qmetry.qaf.automation.step.android;

import com.qmetry.qaf.automation.step.common.BaseClass;
import com.qmetry.qaf.automation.step.QAFTestStep;

public class AndroidExtendedClass extends BaseClass {

	@QAFTestStep(description = "only in extended android step")
	public String onlyInExtendedAndroid() {
		System.out.println("Android");
		return "Only In android";
	}
}
