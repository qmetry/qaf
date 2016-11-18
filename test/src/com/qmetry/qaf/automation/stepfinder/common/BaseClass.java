package com.qmetry.qaf.automation.stepfinder.common;

import com.qmetry.qaf.automation.step.QAFTestStep;

public class BaseClass {

	@QAFTestStep(description = "common base step")
	public String commonBaseStep() {
		System.out.println("Commonnn base steps");
		return "BaseStep";
	}

}
