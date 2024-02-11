/*******************************************************************************
 * Copyright (c) 2019 Infostretch Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.qmetry.qaf.automation.keys;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.qmetry.qaf.automation.util.StringUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.QAFListener;
import com.qmetry.qaf.automation.data.BaseDataBean;
import com.qmetry.qaf.automation.data.PasswordDecryptor;
import com.qmetry.qaf.automation.http.UriProxySelector;
//import com.qmetry.qaf.automation.step.QAFTestStepListener;
//import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener;
//import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriverCommandListener;
//import com.qmetry.qaf.automation.ui.webdriver.QAFWebElementCommandListener;
//import com.qmetry.qaf.automation.ws.rest.RestClientFactory;

/**
 * TO get properties key/value While reading value First preference will be
 * System property if set before run
 * 
 * @author chirag
 */
public enum ApplicationProperties {
	/**
	 * <b>key</b>: <code>report.log.level</code><br/>
	 * <b>value</b>: one of Info, Pass, Fail
	 * <DL>
	 * <DT>Info <DD>will report Info, pass and fail messages
	 * <DT>Pass <DD>will report pass and fail messages but not Info
	 * <DT>Fail <DD>will report fail messages but not Info or Pass
	 * </DL>
	 */
	REPORT_LOG_LEVEL("report.log.level"),
	/**
	 * <b>key</b>: <code>report.log.skip.success</code><br/>
	 * <b>value</b>: boolean, when true it will not show verification success message in report.
	 */
	REPORT_SKIP_SUCCESS("report.log.skip.success"),
	/**
	 * <b>key</b>: <code>test.results.dir </code><br/>
	 * <b>value</b>: dir to place generated result files
	 */
	REPORT_DIR("test.results.dir"), JSON_REPORT_ROOT_DIR("json.report.root.dir"), JSON_REPORT_DIR("json.report.dir"),
	/**
	 * <b>key</b>: <code> selenium.screenshots.dir </code><br/>
	 * <b>value</b>: dir to place screen-shots
	 */
	SCREENSHOT_DIR("selenium.screenshots.dir"),
	/**
	 * <b>key</b>: <code> selenium.screenshots.relative.path </code><br/>
	 * <b>value</b>: screen-shots relative path for reporting
	 */
	SCREENSHOT_RELATIVE_PATH("selenium.screenshots.relative.path"),
	/**
	 * <b>key</b>: <code>selenium.success.screenshots </code><br/>
	 * <b>value</b>: set this flag to 1 if you want to capture screen-shots for
	 * assertion/verification success.
	 */
	SUCEESS_SCREENSHOT("selenium.success.screenshots"),
	/**
	 * <b>key</b>: <code>selenium.failure.screenshots </code><br/>
	 * <b>value</b>: set this flag to 1 if you want to capture screen-shots for
	 * assertion/verification failures. Default value is 1
	 */
	FAILURE_SCREENSHOT("selenium.failure.screenshots"),
	/**
	 * <b>key</b>: <code>selenium.wait.timeout </code><br/>
	 * <b>value</b>: default wait time to be used by framework by wait/assert/verify methods
	 */
	SELENIUM_WAIT_TIMEOUT("selenium.wait.timeout"),
	/**
	 * <b>key</b>: <code>selenium.auto.shutdown </code><br/>
	 * <b>value</b>: flag to auto shutdown selenium server
	 */
	SELENIUM_AUTO_SHUTDOWN("selenium.auto.shutdown"),

	/**
	 * <b>key</b>: <code> tng.context </code><br/>
	 * <b>value</b>: {@link ITestContext} object for current running
	 * thread/test-case.
	 * 
	 */
	CURRENT_TEST_CONTEXT("tng.context"),
	/**
	 * <b>key</b>: <code> current.testcase.name </code><br/>
	 * <b>value</b>: name of the current running test case.
	 * 
	 */
	CURRENT_TEST_NAME("current.testcase.name"),
	/**
	 * <b>key</b>: <code> current.testcase.desc </code><br/>
	 * <b>value</b>: description of the current running test case.
	 * 
	 */
	CURRENT_TEST_DESCRIPTION("current.testcase.desc"),
	/**
	 * <b>key</b>: <code> current.testcase.result </code><br/>
	 * <b>value</b>: {@link ITestResult} object for the current running test
	 * case.
	 * 
	 * @since 2.1.9
	 */
	CURRENT_TEST_RESULT("current.testcase.result"),
	/**
	 * <b>key</b>: <code> driver.name </code><br/>
	 * <b>value</b>: driver to be used, for instance firefoxDriver or
	 * firefoxRemoteDriver etc...
	 * 
	 * @since 2.1.6
	 */
	DRIVER_NAME("driver.name"),
	/**
	 * <b>key</b>: <code> driver.resources.format </code><br/>
	 * <b>value</b>: file or directory to load driver specific resources, for
	 * instance driver specific locators. If you specified driver.name
	 * <code>androidDriver</code> or <code>androidRemoteDriver</code> you can
	 * specify resources for android driver by using
	 * <code>android.resources</code> key.
	 * 
	 * @since 2.1.12
	 */
	DRIVER_RESOURCES_FORMAT("%s.resources"),
	/**
	 * <b>key</b>: <code> driver.init.retry.timeout </code><br/>
	 * <b>value</b>: duration in multiplication of 10 seconds for example 50.
	 * 
	 * @since 2.1.9
	 */
	DRIVER_INIT_TIMEOUT("driver.init.retry.timeout"),

	/**
	 * <b>key</b>: <code> driver.additional.capabilities </code><br/>
	 * <b>value</b>: specify multiple additional capabilities as map that can
	 * applicable for any driver.
	 * 
	 * @see {@link #DRIVER_CAPABILITY_PREFIX} to provide individual capability
	 *      <br/>
	 *      {@link #DRIVER_ADDITIONAL_CAPABILITIES_FORMAT} to provide driver
	 *      specific capability
	 */
	DRIVER_ADDITIONAL_CAPABILITIES("driver.additional.capabilities"),
	/**
	 * <b>key</b>: <code> driver.capabilities </code><br/>
	 * <b>value</b>: specify additional capability by name with this prefix that
	 * can applicable for any driver. For example,
	 * driver.capabilities.&lt;capabilityName&gt;=&lt;value&gt;
	 * 
	 * @see {@link #DRIVER_ADDITIONAL_CAPABILITIES} to provide multiple
	 *      additional capabilities
	 */
	DRIVER_CAPABILITY_PREFIX("driver.capabilities"),

	/**
	 * <b>key</b>: <code> &lt;drivername&gt;.additional.capabilities </code>
	 * <br/>
	 * <b>value</b>: specify multiple additional capabilities as map that can
	 * applicable for specific driver.
	 * 
	 * @see {@link #DRIVER_CAPABILITY_PREFIX} to provide single capability <br/>
	 *      {@link #DRIVER_ADDITIONAL_CAPABILITIES} to provide capability for
	 *      all drivers
	 */
	DRIVER_ADDITIONAL_CAPABILITIES_FORMAT("%s.additional.capabilities"),

	/**
	 * <b>key</b>: <code> &lt;drivername&gt;.capabilities </code> <br/>
	 * <b>value</b>: specify additional capability by name with this prefix that
	 * can applicable for specific driver. For example,
	 * &lt;drivername&gt;.capabilities.&lt;capabilityName&gt;=&lt;value&gt;
	 * 
	 * @see {@link #DRIVER_CAPABILITY_PREFIX} to provide individual capability
	 *      <br/>
	 *      {@link #DRIVER_ADDITIONAL_CAPABILITIES_FORMAT} to provide driver
	 *      specific capability
	 */
	DRIVER_CAPABILITY_PREFIX_FORMAT("%s.capabilities"),

	/**
	 * <b>key</b>: <code> driverClass </code> <br/>
	 * <b>value</b>: capability name to specify driver class name.
	 */
	CAPABILITY_NAME_DRIVER_CLASS("driverClass"),

	/**
	 * <b>key</b>: <code> remote.server</code><br/>
	 * <b>value</b>: remote server url, which will be considered if configured
	 * remote driver.
	 * 
	 * @since 2.1.6
	 */
	REMOTE_SERVER("remote.server"),

	/**
	 * <b>key</b>: <code> remote.port</code><br/>
	 * <b>value</b>: remote server port, which will be considered if configured
	 * remote driver.
	 * 
	 * @since 2.1.6
	 */
	REMOTE_PORT("remote.port"),

	/**
	 * <b>key</b>: <code> env.baseurl </code><br/>
	 * <b>value</b>: base URL of AUT to be used.
	 */
	SELENIUM_BASE_URL("env.baseurl"), // selenium.browser.url
	/**
	 * <b>key</b>: <code>selenium.command.listeners </code><br/>
	 * <b>value</b>: provide comma separated custom command listeners. Custom
	 * command listener must implement {@link SeleniumCommandListener}
	 */
	SELENIUM_CMD_LISTENERS("selenium.command.listeners"),
	/**
	 * <b>key</b>: <code> selenium.capture.network.traffic </code><br/>
	 * <b>value</b>: flag to set capture network traffic true/false, default
	 * value is false
	 */
	CAPTURE_NETWORK_TRAFFIC("selenium.capture.network.traffic"),
	/**
	 * <b>key</b>: <code>selenium.skip.autowait </code><br/>
	 * <b>value</b>:flag to skip auto wait for locator used in command, default
	 * value is false
	 */

	SKIP_AUTO_WAIT("selenium.skip.autowait"),
	/**
	 * <b>key</b>: <code> selenium.skip.autowait </code><br/>
	 * <b>value</b>: comma separated command list to be excluded from auto-wait.
	 */
	AUTO_WAIT_EXCLUDE_CMD("auto.wait.exclude.commands"),
	/**
	 * <b>key</b>: <code>auto.wait.include.commands </code><br/>
	 * <b>value</b>: additional comma separated command list to be included for
	 * auto-wait.
	 */
	AUTO_WAIT_INCLUDE_CMD("auto.wait.include.commands"),

	/**
	 * <b>key</b>: <code>reporter.log.exclude.commands </code><br/>
	 * <b>value</b>: additional comma separated command list to be excluded form
	 * selenium log in HTML report.
	 */
	REPORTER_LOG_EXCLUDE_CMD("reporter.log.exclude.commands"),

	/**
	 * <b>key</b>: <code>commands.execution.interval </code><br/>
	 * <b>value</b>: Set execution interval between two selenium commands.
	 */
	CMD_EXECUTION_INTERVAL("commands.execution.interval"),

	/**
	 * <b>key</b>: <code>integration.tool.qmetry </code><br/>
	 * <b>value</b>: flag that indicates integration with QMetry
	 */
	INTEGRATION_TOOL_QMETRY("integration.tool.qmetry"),
	/**
	 * <b>key</b>: <code>integration.tool.qmetry.uploadattachments </code><br/>
	 * <b>value</b>: flag that indicates integration with QMetry
	 */
	INTEGRATION_TOOL_QMETRY_UPLOADATTACHMENTS("integration.tool.qmetry.uploadattachments"),

	/**
	 * <b>key</b>: <code>qmetry.schedule.file </code><br/>
	 * <b>value</b>: Set QMetry Schedule XML file path.
	 */
	INTEGRATION_PARAM_QMETRY_SCHEDULE_FILE("qmetry.schedule.file"),
	/**
	 * <b>key</b>: <code>integration.param.qmetry.service.url </code><br/>
	 * <b>value</b>: Set QMetry web service URL.
	 */
	INTEGRATION_PARAM_QMETRY_SERVICE_URL("integration.param.qmetry.service.url"),
	/**
	 * <b>key</b>: <code>integration.param.qmetry.user </code><br/>
	 * <b>value</b>: Set QMetry web service login user name.
	 */
	INTEGRATION_PARAM_QMETRY_USER("integration.param.qmetry.user"),
	/**
	 * <b>key</b>: <code>integration.param.qmetry.pwd </code><br/>
	 * <b>value</b>: Set QMetry web service password.
	 */
	INTEGRATION_PARAM_QMETRY_PWD("integration.param.qmetry.pwd"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.project </code><br/>
	 * <b>value</b>: Set QMetry project.
	 */
	INTEGRATION_PARAM_QMETRY_PRJ("integration.param.qmetry.project"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.build </code><br/>
	 * <b>value</b>: Set QMetry build.
	 */
	INTEGRATION_PARAM_QMETRY_BLD("integration.param.qmetry.build"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.release </code><br/>
	 * <b>value</b>: Set QMetry release.
	 */
	INTEGRATION_PARAM_QMETRY_REL("integration.param.qmetry.release"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.cycle </code><br/>
	 * <b>value</b>: Set QMetry release.
	 */
	INTEGRATION_PARAM_QMETRY_CYCLE("integration.param.qmetry.cycle"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.suitid </code><br/>
	 * <b>value</b>: Set QMetry suit id.
	 */
	INTEGRATION_PARAM_QMETRY_SUIT("integration.param.qmetry.suitid"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.suitid </code><br/>
	 * <b>value</b>: Set QMetry suit id.
	 */
	INTEGRATION_PARAM_QMETRY_SUITERUNID("integration.param.qmetry.suitrunid"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.platform </code><br/>
	 * <b>value</b>: Set QMetry platform id.
	 */
	INTEGRATION_PARAM_QMETRY_PLATFORM("integration.param.qmetry.platform"),
	/**
	 * <b>key</b>: <code> integration.param.qmetry.drop </code><br/>
	 * <b>value</b>: Set QMetry drop id.
	 */
	INTEGRATION_PARAM_QMETRY_DROP("integration.param.qmetry.drop"),
	/**
	 * <b>key</b>: <code> webdriver.remote.session </code><br/>
	 * <b>value</b>: Set existing session of web-driver.
	 * <p>
	 * This feature can be used with remote web-driver
	 * </p>
	 */
	WEBDRIVER_REMOTE_SESSION("webdriver.remote.session"),
	/**
	 * <b>key</b>: <code> webdriver.chrome.driver </code><br/>
	 * <b>value</b>: Set Chrome driver path.
	 */
	CHROME_DRIVER_PATH("webdriver.chrome.driver"),
	/**
	 * <b>key</b>: <code> qc.testset.name </code><br/>
	 * <b>value</b>: Set QC test set name.
	 */
	QC_TS_NAME("qc.testset.name"),
	/**
	 * <b>key</b>: <code> qc.testset.folder.path</code><br/>
	 * <b>value</b>: Set QC test set folder path.
	 */
	QC_TS_FOLDER_PATH("qc.testset.folder.path"),
	/**
	 * <b>key</b>: <code> qc.testcase.folder.path</code><br/>
	 * <b>value</b>: Set QC test case folder path.
	 */
	QC_TC_FOLDER_PATH("qc.testcase.folder.path"),
	/**
	 * <b>key</b>: <code> qc.run.name</code><br/>
	 * <b>value</b>: Set QC run name.
	 */
	QC_RUN_NAME("qc.run.name"),
	/**
	 * <b>key</b>: <code> qc.timezone</code><br/>
	 * <b>value</b>: (Optional)Set QC server time - that will be used while
	 * setting date/time in run result.
	 */
	QC_TIMEZONE("qc.timezone"),
	/**
	 * <b>key</b>: <code> qc.runname.generator.impl</code><br/>
	 * <b>value</b>: qualified class name that implements
	 * com.qmetry.qaf.automation.integration.qc.QCRunNameGenerator interface. It
	 * can be used to generate name in custom format for auto-generated set and
	 * run name
	 */
	QC_RUN_NAME_GENERATOR("qc.runname.generator.impl"),
	/**
	 * <b>key</b>: <code> qc.user</code><br/>
	 * <b>value</b>: Set QC user name that will be used to access QC
	 */
	QC_USER("qc.user"),
	/**
	 * <b>key</b>: <code> qc.domain</code><br/>
	 * <b>value</b>: Set QC domain name that will be used to lookup QC project
	 */
	QC_DOMAIN("qc.domain"),
	/**
	 * <b>key</b>: <code> qc.project</code><br/>
	 * <b>value</b>: Set QC project name that where test case defined
	 */
	QC_PROJECT("qc.project"),
	/**
	 * <b>key</b>: <code> qc.pwd</code><br/>
	 * <b>value</b>: Set QC user password
	 */
	QC_PWD("qc.pwd"),
	/**
	 * <b>key</b>: <code> qc.service.url</code><br/>
	 * <b>value</b>: QC service URL, for example:
	 * http://10.20.30.252:8080/qcbin/
	 */
	QC_SERVICE_URL("qc.service.url"),
	/**
	 * <b>key</b>: <code> env.load.locales</code><br/>
	 * <b>value</b>: list of local names to be loaded
	 */
	LOAD_LOCALES("env.load.locales"),

	/**
	 * <b>key</b>: <code> env.default.locale</code><br/>
	 * <b>value</b>: local name from loaded locals that need to treated as
	 * default local
	 */
	DEFAULT_LOCALE("env.default.locale"), LOCALE_CHAR_ENCODING("locale.char.encoding"),
	/**
	 * <b>key</b>: <code> isfw.version</code><br/>
	 * <b>value</b>: ISFW version local
	 */
	ISFW_VERSION("isfw.version"),
	/**
	 * <b>key</b>: <code> isfw.revision</code><br/>
	 * <b>value</b>: ISFW revision local
	 */
	ISFW_REVISION("isfw.revision"),
	/**
	 * <b>key</b>: <code> isfw.build.date</code><br/>
	 * <b>value</b>: ISFW build date local
	 */
	ISFW_BUILD_DATE("isfw.build.date"),
	/**
	 * <b>key</b>: <code> isfw.build.info</code><br/>
	 * <b>value</b>: ISFW build information - version, revision and build date.
	 * local
	 */
	ISFW_BUILD_INFO("isfw.build.info"),
	/**
	 * <b>key</b>: <code>qaf.listeners</code><br/>
	 * <b>value</b>: list of qaf listeners (fully qualified class name that
	 * implements any of {@link QAFTestStepListener},
	 * {@link QAFWebDriverCommandListener},
	 * {@link QAFWebElementCommandListener}) to be registered.
	 * 
	 * @see QAFListener
	 */
	QAF_LISTENERS("qaf.listeners"),
	/**
	 * <b>key</b>: <code> teststep.listeners</code><br/>
	 * <b>value</b>: list of test step listeners (fully qualified class name
	 * that implements QAFTestStepListener) to be registered.
	 * 
	 * @see QAFTestStepListener
	 */
	TESTSTEP_LISTENERS("teststep.listeners"),
	/**
	 * <b>key</b>: <code> wd.command.listeners</code><br/>
	 * <b>value</b>: list of webdriver command listeners (fully qualified class
	 * name that implements QAFWebDriverCommandListener) to be registered.
	 * 
	 * @see QAFWebDriverCommandListener
	 */
	WEBDRIVER_COMMAND_LISTENERS("wd.command.listeners"),
	/**
	 * <b>key</b>: <code> we.command.listeners</code><br/>
	 * <b>value</b>: list of webelement command listeners (fully qualified class
	 * name that implements QAFWebElementCommandListener) to be registered.
	 * 
	 * @see QAFWebElementCommandListener
	 */
	WEBELEMENT_COMMAND_LISTENERS("we.command.listeners"),
	/**
	 * <b>key</b>: <code>integration.param.jira.baseurl </code><br/>
	 * <b>value</b>: Set JIRA Base URL.
	 */
	INTEGRATION_PARAM_JIRA_SERVICE_URL("integration.param.jira.baseurl"),
	/**
	 * <b>key</b>: <code>integration.param.jira.password </code><br/>
	 * <b>value</b>: Set JIRA Password.
	 */
	INTEGRATION_PARAM_JIRA_PWD("integration.param.jira.pwd"),
	/**
	 * <b>key</b>: <code>integration.param.jira.project </code><br/>
	 * <b>value</b>: Set JIRA Projects.
	 */
	INTEGRATION_PARAM_JIRA_PROJECT("integration.param.jira.project"),
	/**
	 * <b>key</b>: <code>integration.param.jira.username </code><br/>
	 * <b>value</b>: Set JIRA Username.
	 */
	INTEGRATION_PARAM_JIRA_USER("integration.param.jira.user"),
	/**
	 * <b>key</b>: <code>step.provider.pkg </code><br/>
	 * <b>value</b>: one or more package name from where test-step should be
	 * loaded. When more than one package provided and same step defined in
	 * multiple package than it will be loaded based on the package order in
	 * "step.provider.pkg", the last package has highest priority.
	 */
	STEP_PROVIDER_PKG("step.provider.pkg"),
	/**
	 * <b>key</b>: <code>retry.count </code><br/>
	 * <b>value</b>: integer to specify how many times test should be retried on
	 * failure by default retry analyzer. This will not take effect if custom
	 * retry analyzer is provided using
	 * {@link ApplicationProperties#RETRY_ANALYZER retry.analyzer }
	 */
	RETRY_CNT("retry.count"),
	/**
	 * <b>key</b>: <code>retry.analyzer</code><br/>
	 * <b>value</b>: fully qualified class name that implements
	 * {@link IRetryAnalyzer}. Provide this property to use your custom retry
	 * analyzer.
	 */
	RETRY_ANALYZER("retry.analyzer"),
	/**
	 * <b>key</b>: <code>bean.populate.random</code><br/>
	 * <b>value</b>: boolean value to specify whether to populate bean data
	 * randomly or in sequence. This property used by
	 * {@link BaseDataBean#fillFromConfig(String) fillFromConfig} method while
	 * populating bean from configuration, when more than one record exist in
	 * configuration .
	 */
	BEAN_POPULATE_RANDOM("bean.populate.random"),

	DRY_RUN_MODE("dryrun.mode"),

	/**
	 * @since 2.1.11 <b>key</b>: <code>rest.client.impl</code><br/>
	 *        <b>value</b>: full qualified name of the class that extends
	 *        {@link RestClientFactory}.
	 */
	REST_CLIENT_FACTORY_IMPL("rest.client.impl"),

	/**
	 * @since 2.1.13 <b>key</b>: <code>password.decryptor.impl</code><br/>
	 *        <b>value</b>: full qualified name of the class that implements
	 *        {@link PasswordDecryptor}. This implementation will be used to
	 *        decrypt password. When configuration manager found any key starts
	 *        with {@link #ENCRYPTED_PASSWORD_KEY_PREFIX}
	 */
	PASSWORD_DECRYPTOR_IMPL("password.decryptor.impl"),

	/**
	 * <b>key</b>: <code>encrypted</code><br/>
	 * <b>value</b>: property with prefix 'encrypted'. When configuration
	 * manager found any key starts with 'encrypted' prefix, for example
	 * 'encripted.db.pwd', then it will store decrypted value without prefix,
	 * 'db.pwd' in this example. So you can reference decrypted value anywhere
	 * in the code with key without this prefix ('db.pwd' in this example).
	 * 
	 * @since 2.1.13
	 */
	ENCRYPTED_PASSWORD_KEY_PREFIX("encrypted."),
	/**
	 * <p>
	 * To set default meta-data for all element. Meta-data provided with locator
	 * has higher preference than default values.
	 * </p>
	 * <b>key</b>: <code>element.default.metadata</code><br/>
	 * <b>value</b>: JSON map of meta data to be set as default for element.
	 * 
	 * @since 2.1.13
	 * 
	 */
	ELEMENT_GLOBAL_METADATA("element.default.metadata"),
	/**
	 * <p>
	 * Specify weather to attach default element listener or not.
	 * </p>
	 * <b>key</b>: <code>element.default.listener</code><br/>
	 * <b>value</b>: boolean true/false.
	 * 
	 * @since 2.1.13
	 * 
	 */
	ELEMENT_ATTACH_DEFAULT_LISTENER("element.default.listener"),
	/**
	 * <p>
	 * Set true to trust all certificates and ignore host name verification for
	 * web-services.
	 * </p>
	 * <b>key</b>: <code>https.accept.all.cert</code><br/>
	 * <b>value</b>: boolean true/false.
	 * 
	 * @since 2.1.13
	 * 
	 */
	HTTPS_ACCEPT_ALL_CERT("https.accept.all.cert"),

	/**
	 * <p>
	 * Set test case identifier meta-key which will be used to as file name of
	 * test case result json file.
	 * </p>
	 * <b>key</b>: <code>tc.identifier.key</code><br/>
	 * <b>value</b>: String test-case meta-key.
	 * 
	 * @since 2.1.13
	 * 
	 */
	TESTCASE_IDENTIFIER_KEY("tc.identifier.key"),
	/**
	 * <p>
	 * Set proxy server that needs to used by {@link UriProxySelector}
	 * </p>
	 * <b>key</b>: <code>proxy.server</code><br/>
	 * <b>value</b>: proxy server.
	 * 
	 * @since 2.1.14
	 * 
	 */
	PROXY_SERVER_KEY("proxy.server"),
	/**
	 * <p>
	 * Set proxy server port that needs to used by {@link UriProxySelector}.
	 * Default value is 80.
	 * </p>
	 * <b>key</b>: <code>proxy.port</code><br/>
	 * <b>value</b>: integer port of running proxy server.
	 * 
	 * @since 2.1.14
	 * 
	 */
	PROXY_PORT_KEY("proxy.port"),
	/**
	 * <p>
	 * Set one or more host url that needs to be proxied through given proxy server.
	 * </p>
	 * <b>key</b>: <code>host.to.proxy</code><br/>
	 * <b>value</b>: one or more host URL separated by ';'
	 * 
	 * @since 2.1.14
	 * 
	 */
	PROXY_HOSTS_KEY("host.to.proxy"),
	/**
	 * <p>
	 * Set list of meta-data rule to be applied on meta-data during dryrun.
	 * </p>
	 * <b>key</b>: <code>metadata.rules</code><br/>
	 * <b>value</b>: Json format List of {@link MetaDataRule}
	 * 
	 * @since 2.1.15
	 * 
	 */
	METADATA_RULES("metadata.rules"),
	/**
	 * <p>
	 * Set format as supported by {@link MessageFormat} that accepts one argument.
	 * </p>
	 * <b>key</b>: <code>metadata.formatter</code><br/>
	 * <b>value</b>: format to apply on meta-value
	 * 
	 * @since 2.1.15
	 * 
	 */
	METADATA_FORMTTOR_PREFIX("metadata.formatter");

	public String key;

	private ApplicationProperties(String key) {
		this.key = key;
	}

	/**
	 * @param defaultVal
	 *            optional
	 * @return
	 */
	public String getStringVal(String... defaultVal) {
		return System.getProperty(key, getObjectVal(defaultVal).toString());
	}

	private Object getObjectVal(String... defaultVal) {
		List<?> list = ConfigurationManager.getBundle().getList(key,
				(null == defaultVal || defaultVal.length < 1 || null == defaultVal[0]) ? Collections.emptyList()
						: Arrays.asList(defaultVal));
		return list != null && !list.isEmpty() ? list.get(list.size() - 1) : "";
	}
	/**
	 * @param defaultVal
	 *            optional
	 * @return
	 */
	public int getIntVal(int... defaultVal) {
		try {
			int val = Integer.parseInt(getStringVal());
			return val;
		} catch (Exception e) {
			// just ignore
		}
		return (null != defaultVal) && (defaultVal.length > 0) ? defaultVal[0] : 0;
	}

	/**
	 * @param defaultVal
	 *            optional
	 * @return
	 */
	public boolean getBoolenVal(boolean... defaultVal) {
		try {
			String sVal = getStringVal().trim();
			boolean val = StringUtil.isNumeric(sVal) ? (Integer.parseInt(sVal) != 0) : Boolean.parseBoolean(sVal);
			return val;
		} catch (Exception e) {
			// just ignore
		}
		return (null != defaultVal) && (defaultVal.length > 0) && defaultVal[0];
	}

	public Object getObject(Object... defaultVal) {
		Object objToReturn = ConfigurationManager.getBundle().getObject(key);
		return null != objToReturn ? objToReturn
				: (null != defaultVal) && (defaultVal.length > 0) ? defaultVal[0] : null;
	}
}
