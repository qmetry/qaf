package com.qmetry.qaf.automation.stepfinder.common;

import com.qmetry.qaf.automation.step.QAFTestStep;

public interface HomePage {

	@QAFTestStep(description = "open application")
	String openApp();
}
