package com.qmetry.qaf.automation.stepfinder;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.JavaStepFinder;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestCase;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author amit.bhoraniya
 *         This class tests for the step loading mechanism from provided
 *         {@link ApplicationProperties.STEP_PROVIDER_PKG }
 */
public class StepTests extends WebDriverTestCase {

	@Test(description = "verify definition of step inside interface and invoking android(package-1) implementation")
	public void stepDefineInInterfaceImplementedInAndroid() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.android");
		StringTestStep.execute("open application", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Android"));
	}

	@Test(description = "verify definition of step inside interface and invoking iOS(package-2) implementation")
	public void stepDefineInInterfaceImplementedIniOS() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.ios");
		StringTestStep.execute("open application", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("IOS"));
	}

	@Test(description = "step define in interface for package common and android")
	public void stepDefineInInterfaceImplementedInAndroid1() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.common;com.qmetry.qaf.automation.stepfinder.android");
		StringTestStep.execute("open application", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Android"));
	}

	@Test(description = "step define in interface for package common and ios")
	public void stepDefineInInterfaceImplementedIniOS2() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.common;com.qmetry.qaf.automation.stepfinder.ios");
		StringTestStep.execute("open application", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("IOS"));
	}

	@Test(description = "step define in interface and step provider package ios,android should call android step")
	public void stepDefineInInterfaceAndroid2() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.ios;com.qmetry.qaf.automation.stepfinder.android");
		StringTestStep.execute("open application", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Android"));
	}

	@Test(description = "step define in interface and step provider package android,ios should call ios step")
	public void stepDefineInInterfaceIOS2() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.android;com.qmetry.qaf.automation.stepfinder.ios");
		StringTestStep.execute("open application", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("IOS"));
	}

	@Test(description = "step only in android and step provide pkg android")
	public void testValidStepIsCalledAndroid3() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.android");
		StringTestStep.execute("only in android", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In android"));
	}

	@Test(description = "step only in ios and step provide pkg ios")
	public void testValidStepIsCalledForIOS3() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.ios");
		StringTestStep.execute("only in ios", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In ios"));
	}

	@Test(description = "test valid step is called for Android")
	public void testValidStepIsCalledAndroid4() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.android");
		StringTestStep.execute("in both", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Android"));
	}

	@Test(description = "in both")
	public void testValidStepIsCalledForIOS4() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.ios");
		StringTestStep.execute("in both", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("IOS"));
	}

	@Test(description = "loadBaseClassToAndroid")
	public void loadBaseClassToAndroid() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.android");
		StringTestStep.execute("common base step", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("BaseStep"));
	}

	@Test(description = "loadBaseClassToIOS")
	public void loadBaseClassToIOS() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.ios");
		StringTestStep.execute("common base step", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("BaseStep"));
	}

	@Test(description = "test valid step is called for Android Extended")
	public void testValidStepIsCalledAndroidExtended() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.android");
		StringTestStep.execute("only in extended android step", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In android"));
	}

	@Test(description = "test valid step is called for IOS Extended")
	public void testValidStepIsCalledForIOSExtended() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg",
				"com.qmetry.qaf.automation.stepfinder.ios");
		StringTestStep.execute("only in extended ios step", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In ios"));
	}

	@Test(description = "root package should find all steps")
	public void rootFindAllPackages() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf.automation.stepfinder");

		StringTestStep.execute("only in extended android step", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In android"));

		StringTestStep.execute("only in extended ios step", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In ios"));

		StringTestStep.execute("in both", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.anyOf(Matchers.equalTo("Android"), Matchers.equalTo("IOS")));

		StringTestStep.execute("only in ios", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In ios"));

		System.out.println(JavaStepFinder.getAllJavaSteps());
		StringTestStep.execute("only in android", new Object[]{});
		Validator.verifyThat(getBundle().getString("last.step.result"),
				Matchers.equalTo("Only In android"));
	}

	@Test(description = "verify step finder should find common steps")
	public void shouldFindCommonSteps() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf");
		Validator.verifyThat(JavaStepFinder.getAllJavaSteps(),
				Matchers.hasKey("COMMENT"));
	}

}
