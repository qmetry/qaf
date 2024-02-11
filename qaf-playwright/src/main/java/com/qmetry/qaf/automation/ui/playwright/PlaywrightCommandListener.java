/**
 * 
 */
package com.qmetry.qaf.automation.ui.playwright;

import com.microsoft.playwright.BrowserType.LaunchOptions;

/**
 * @author chirag
 *
 */
public interface PlaywrightCommandListener {

	default void onInitialize(PlaywrightDriver uiDriver) {

	}

	default void onInitializationFailure(LaunchOptions launchOptions, Throwable e) {

	}

	default void beforeInitialize(LaunchOptions launchOptions) {

	}

	default void beforeCommand(PlaywrightDriver driver, PlaywrightCommandTracker commandTracker) {
	}

	default void afterCommand(PlaywrightDriver driver, PlaywrightCommandTracker commandTracker) {
	}

	default void onFailure(PlaywrightDriver driver, PlaywrightCommandTracker commandTracker) {
	}

}
