package com.qmetry.qaf.automation.step.common;

import com.qmetry.qaf.automation.step.QAFTestStep;

public interface HomePage {

	@QAFTestStep(description = "open application")
	String openApp();
}
