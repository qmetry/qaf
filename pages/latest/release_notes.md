---
title: Release Notes
sidebar: qaf_latest-sidebar

permalink: latest/release_notes.html
folder: latest
---

## VER-2.1.9 Release Notes:

**New Features**

 1. [#11](https://github.com/qmetry/qaf/issues/11) TestNG 6.9.10 support
 2. [#28](https://github.com/qmetry/qaf/issues/28) Gherkin for test authoring
 3. [#29](https://github.com/qmetry/qaf/issues/29) Driver initialize failure method in driver listener
 4. [#30](https://github.com/qmetry/qaf/issues/30) Introduce new property `qaf.listeners` to register any of qaf listener
 5. [#19](https://github.com/qmetry/qaf/issues/19) scenario object access in test-step listener
 6. [#31](https://github.com/qmetry/qaf/issues/31) FormDataBean Custom Component support
 7. [#32](https://github.com/qmetry/qaf/issues/32) New property `step.provider.sharedinstance` to allow class variable sharing among steps in same class. Default is false.
 8. [#36](https://github.com/qmetry/qaf/issues/36) New step creation code snippet when step not found
 9. [#37](https://github.com/qmetry/qaf/issues/37) new property `driver.init.retry.timeout` to retry on driver initialization failure
 10 [#10](https://github.com/qmetry/qaf/issues/10) Support for xml for test-authoring 
 12. Support to third party java step annotation (for example cucumber Given/When/Then instead of QAFTestStep) 
 13. Added new driver name _perfecto_ can be used in `driver.name` value as `perfectoDriver` or `perfectoRemoteDriver`
  

**Enahancements**

 1. [#17](https://github.com/qmetry/qaf/pull/17) passing params in deployResult instead of scenario.getMetadata()
 2. [#20](https://github.com/qmetry/qaf/pull/20) support to provide .xml or .loc file location as resources
 3. QAF BDD background support
 4. Private method (with step annotation or Step provider annotation at class level) will be excluded from step mapping
 5. Utility method in UIDriverFectory to get capability that will be used by factory to create driver object.
 6. Updates releated to selenium 3 and Removed seleniumExceptions to make code work without selenium-leg dependency with selenium 3.

**Bug Fixes**

 1. [#18](https://github.com/qmetry/qaf/pull/18) Fixed infinite loop issue with retry.count
 2. Fixed issue of not scanning steps from all step provider package. Improved step finder.
 
**Depricated/Removed features**
 
  * Moved cucumber package in seperate QAF-Cucumber project. 


## VER-2.1.8 Release Notes:

**New Features:**

1. Refactored package from com.infostretch to com.qmetry.qaf

## VER-2.1.7b Release Notes:

**New Features:**

1. Test step threshold support at step level
2. Inbuilt step to start and stop time tracking with threshold
3. Test step duration and threshold recording in json report
4. Command execution duration recording in json report
5. test step result as warning when one or more warning in sub steps
6. non-java custom step definition updates
  custom step definition to have parameter names instead of index, for ex: instead of bdl step def : "login with {0} and {1}" user can define as "login with {username} and {password}"
7. Supported to access map and array argument in step call within custom step.
8. Removed web driver response status from command log for web driver command result
9. Retry analyzer support for data-driven test
10. Introduced new property 'retry.analyzer' : Provide this property to use your custom retry analyzer
11. Added support to provide license file for license
12. property list value passed as system property
13. Specified default driver "firefoxDriver" if driver property not provided, with console message
14. Added support to specify individual capability with "driver.capabilities" prefix for all driver
15. Enhanced step exceptions handling
16. Enhanced verify/assert for text/attribute/value to have proper message if timeout with element not present
17. Added support to have BDD Keyword Synonyms
18. Added support to scan bdd,kwd and excel file with step provider package to load non java steps
29. Support of TestStep meta-data by using MetaData annotation at method level or at class level.
20. Step Provider Package order and priority.Last package in list has highest priority.
21. Underlying configuration of property util from property configuration to xml configuration
22. For Test Step string argument , Single quote (') or Double quote (") is now compulsory
23. Updated cucumber formatter
24. Deprecated "driver.class" use instead "driverClass"
 
**Bug Fixes:**

1. ISFW-148 - Same Scenario name overwrites &lt;test_case_name&gt;.json file in report.
2. ISFW-150 - Run Configuration Include / Exclude not working properly. 


**QAF Reporter:**

1. Report Enhancements/Improvements
2. Test step duration indication in checkpoints and subcheckpoints
3. BarChart for Test step threshold and duration
4. Actual summary time at scenario level
5. Added duration in command log
6. Added Different Exception type as Error Analysis
7. Added rerun count
8. Added retry icon at scenario level
9. Well formatted Command Logs and Error Trace


## VER-2.1.6b Release Notes:

**New Features:**

1. custom meta-data support in json report
2. Test management tool integration support for QMetry 6
3. Randomized data selection for the data driven test cases
4. Introduced new property 'bean.populate.random'
5. Generate failed test cases incase of bdd
6. Added property change listener to handle driver, resources, step provider package changes on the fly.
7. Renamed few of properties related to driver.
   * selenium.defaultBrowser-->driver.name
   * selenium.server-->remote.server
   * selenium.port-->remote.port
8. removed setup method from WebDriverTestBase class
9. for Test Step string argument , Single quote (') or Double quote (") is now compulsory
10. Renamed Attritube to Attribute for following methods
	* verifyAttritube --> verifyAttribute
	* verifyNotAttritube --> verifyNotAttribute
	* waitForNotAttritube -->  waitForNotAttribute
	* waitForAttritube --> waitForAttribute
	* assertAttritube --> assertAttribute
	* assertNotAttritube --> assertNotAttribute
 
**QAF Reporter:**

1. Report Enhancements/Improvements
2. Custom meta-data display in report
3. Report backward support


**Bug Fixes:**

1. ISFWR-46 - QAS report Order by "Name" not working Properly.


## VER-2.1.5 Release Notes:

**New Features:**

1. Custom meta-data filter support using include and exclude parameters
2. Typo correction in 'assertNotText'
3. Enable Perfecto Command logs same as selenium logs
4. Test management tool integration support for QMetry

**Bug Fixes:**

1. ISFW-113 - BDD Empty Scenario must run without any error.
2. ISFW-115 - Screenshot not capture on failure case.
3. ISFW-117 - wrong exception display on failure of assert.
4. ISFW-119 - driver.executeScript throws java.lang.NullPointerException.
5. ISFW-116 - Unexpected behaviour while QAFTestStep defined at interface level and implemented interface in class. 

**QAF Reporter**

1. Report Enhancements/Improvements

**Bug Fixes**

1. ISFWR-43 - Wrong PassRate percentage in report.


## VER-2.1.4 Release Notes:

**New Features:**

1. Fetch all data of DataProvider

2. Performance Enhancements

3. Step exception handling, line number and bdd file name in stacktrace.

4. TestCase marked as skip in-case of TestStep not found or BDD parsing issue.

5. Provision in StepInvocation listener to have step type, step index and also to set next step index from listener.

6. Report to have , not run step in case of TestCase fail or skip.

7. BDD test steps call are now case-insensitive.

**Bug Fixes:**

1. ISFW-148 - Same Scenario name overwrites &lt;test_case_name&gt;.json file in report.


**QAF Reporter:**

1. ISFWR-35 - TrendChart showing incorrect execution count.

## VER-2.1.3 Release Notes:
  Supported selenium-server version 2.41 or above
  
**New Features:**

1. Supported jre 1.6 or above (1.8 will now supported with updated base data bean)
2. Compatibility Perfecto mobile
3. Continue to selenium 1 support
4. Added support for custom locator strategy especially to support custom strategy by appium
5. Added support for getting underlaying driver
6. Test Management Tool Integration Support for BDD and KWD.
7. Appium 1.7 version support
8. Test Step Library BDL and KWL

**Bug Fixes:**

1. ISFW-126 - Wrong testcase count in report.
2. ISFW-105 - BDD/KWD data-driven test data map as Parameter not working.
3. ISFW-99 - With perfacto driver, actual capabilities are not available in Report.
4. ISFW-100 - verification/ assertions with perfecto QASMobileElement is not working.
5. ISFW-101 - Custom locator startegy always taking xpath.
6. ISFW-102 - StepProvider Package for parellel execution not working.
7. ISFW-95 - Typo of method name for assert.
8. ISFW-93 - QAS report showing invalid checkpoints for Java Arguments.


**QAF Reporter:**

Enhancements:

1. Combine checkpoint and overview tab.
2. Auto refresh button redesign.
3. Add legend in trend chart related to pass, skip, and fail test cases.
4. Indication for expandable step in checkpoints tab.
5. Navigate to particular execution from trend-chart.
6. Add tool tip for count of pass, fail, skip test cases in trends-chart.
7. Execution number on sidebar in reports.
8. Add label for Number of Test Cases in trends-chart.(Y-Axis)

**Bug Fixes:**

1. ISFWR-33 - method results are not expanding in report.

## VER-2.1.2 Release Notes:
 
 1. Supported selenium-server version min 2.34

**New Features:**

1. ISFW-51: multiple step provider packages.

2. ISFW-54 Added support for data-bean field value list (list or key) to pick random value

3. ISFW-45 Alternate/multiple locator support

4. ISFW-43: Support for override property from xml configuration in web-service testing

5. ISFW-49: JQuery Locator support

6. Added waitForAjaxToLoad with IsExtendedWebDriver.

7. Test step BDD client - Behavior driven support

8. Test step listener support

9. Property file with .loc extension

10. Cucumber with ISFW (Web application functional Android/IPhone native and Rest web service automation)
    - Validation Support
    - Reporting check points in default cucumber report(s)
    - ISFW reporter support
    - Parameter support in cucumber feature file step call arguments

**Bug Fixes:**

1. seleniumEquals Null Pointer exception.
2. ISFW-57: VerifyPresent is not working in case of nested child component
3. ISFW-47: Wrong Description in report

**Deprecated Feature**

JSONUtil.toMap method

**Modifications**

Following properties modified/added

Original property name|New Property name
load.locale|env.load.locale
default.locales|env.default.locales
teststep.listeners
 
**ISFW Reporter New Features**

Attached deffect tracker with report.

**Bug Fixes**

1. ISFW-55 Not Able to get Test Case Summary in ISFW New Report
2. Not displaying method result if args not provided in method meta-info.json

## VER-2.1.1 Release Notes:

**New Features:**

Supported selenium-server version min 2.22.0 max 2.32.0 and upto 2.34

* Pro: Added  ISFW build information and environment information in report under test overview section.

* Pro: Added CSV step client

* Pro: Added group, soft dependency, ordering support for CSV/Excel scenario

* Pro: Added predefined steps working with web element object

* Pro: Added rest web service testing support

* Added Validator class to provide assertion/verification methods that supports hamcrest matchers

* Added onInitialize method in web driver command listener

**Improvements:**

* Updated QMetry client

* Removed feature of automatic starting selenium server.

* Renamed properties:

	 selenium.browser.url - > env.baseurl
	 
	 test.props.dir -> env.resources

	 app.autolaod.test.prop -> resources.load.subdirs
 
* selenium.server.start.command -> discontinued

Package restructured and renamed some classes, please refer Upgrade notes.


**Screen Shots**

{% include inline_image.html file="TestStep_clients.png" %}

{% include inline_image.html file="Report_overview.png" %}

{% include inline_image.html file="Report_env.png" %}

{% include inline_image.html file="Report_details.png" %}


## VER-2.1.0 Release Notes:

**Improvements:**

**Renamed properties:**

* driver.extra.capabilities to driver.additional.capabilities
 
* driver.extra.capabilities to driver.additional.capabilities

**Package restructured and code clean up:**

* Removed "com.qmetry.qaf.automation.webdriver.custom" package holding sample custom component.

* Removed class/pacakage from selenium community (org.**).

* Removed deprecated methods:**

```java
/**             
* key:test.selenium.report.dir <br/>             
* value: dir to place generated result files             
*             
* @Deprecated: use {@link #REPORT_DIR} instead             
*/
@Deprecated            
SEL_REPORT_DIR("selenium.report.dir"),
```

* ISWebDriverBackedSelenium:

```java
 /**
 * @deprecated Use {@link #getWrappedDriver()} instead.
 */
 @Deprecated
 public IsExtendedWebDriver getUnderlyingWebDriver() {
 return getWrappedDriver();
 }
```

* Base test case:

```java
 /**
 * blank implementation need to override this method to provide before
 * individual test specific implementation Override beforeTest method for
 * before test implementation if you are configuring parallel methods
 * instead of method with @beforeTest annotation
 *
 * @deprecated use @beforeMethod instead
 * @param stb
 * @param m
 */
 @Deprecated
 protected void beforeTest(SeleneseTestBase stb, Method m) {
 }
 /**
 * blank implementation need to override this method to provide before each
 * test specific implementation
 *
 * @deprecated use @beforeMethod instead
 * @param stb
 */
 @Deprecated
 protected void beforeTest(SeleneseTestBase stb) {
 }
```

* PropertyUtil:

```java
 /**
 * @deprecated use {@link #getString(String, String)} instead
 * @param sPropertyName
 * @param def
 * @return
 */
 @Deprecated
 public String getProperty(String sPropertyName, String def) {
 return getString(sPropertyName, def);
 }
 /**
 * @param sPropertyName
 * @return default value is key itself
 */
 public String getPropertyValue(String key) {
 return this.getString(key, key);
 }
 /**
 * @deprecated use {@link #getInt(String)} instead
 * @param sPropertyName
 * @return
 */
 @Deprecated
 public int getPropertyIntValue(String sPropertyName) {
 return getInt(sPropertyName, 0);
 }
 /**
 * @deprecated use {@link #getInt(String, int)} instead
 * @param sPropertyName
 * @param defaultVal
 * @return
 */
 @Deprecated
 public int getPropertyIntValue(String sPropertyName, int defaultVal) {
 return getInt(sPropertyName, defaultVal);
 }
 /**
 * @deprecated use {@link #getString(String,String)} instead
 * @param sPropertyName
 * @param def
 * @return
 */
 @Deprecated
 public String getPropertyValue(String sPropertyName, String def) {
 return getString(sPropertyName, def);
 }
```

* ConfigurationManager:

```java
 /**
 * @deprecated Use {@link ConfigurationManager#getBundle()} instead
 * @return
 */
 @Deprecated
 public PropertyUtil getApplicationProperties() {
 return getBundle();
 }
```  
 
 
## VER-2_0-b09 Release Notes:

**Improvements:**

* Compatibility for chrome driver with server version above or below 2.22 (latest till the date is 2.33)

* Changed the way of providing extra capabilities from csv to json for example

```properties
chrome.extra.capabilities={"platform"="ANY","cssSelectorsEnabled"=true,"chrome.switches":["--ignore-certificate-errors","--user-data-dir=F:\chirag\projects\STFWs\ISFWTestStepDemoProject\chromeprofile"]}
```
     
 
## VER-2_0-b08 Release Notes

**New Features:**

* Added support localization of xml file. To create xml file for specific local provide local ext after .xml for example testdata.xml.en

* Added test step feature: 

**Step Feature:**

ISFW will provide a mechanism to specify any method as test step by annotating the method with @IsTestStep. Furthermore methods in class specified as step provider by annotating the class with@IsTestStepProvider. Also we have provided provision to supply step description so that it can be reported in report.
 
```java
public class SampleClass{

       @IsTestStep(description="do the needful with {0}")
       public static void testStep(String s) {
              System.out.println("testStep invoked!....." + s);
       }

      @IsTestStep(description="do the needful")
       public void testStep2() {
              System.out.println("testStep without args invoked!.....");
       }
}

@IsTestStepProvider
public class SampleStepProvider {
      public void step() {
              System.out.println("step invoked!.....");
       }
       public void anotherStep() {
              System.out.println("anotherStep invoked!.....");
       }
}
```

* Added Step client for Excel
 
**Bug Fixes:**

* Fixed issue of overriding resources from config file by providing resource file(s)/dir(s)

 
## VER-2_0-b07 Release Notes:

**New Features:**

* Json based reporting support , you need to copy dashboard.htm and jquery directory.

* Added service entry to register testNG listener. Now you don't require to register testng listener of ISFW. Please remove "com.qmetry.qaf.automation.testng.IsTestNGListener" entry from the listener entries in existing build files.

* Added support to directly provide remote webdriver url in case it is not in standard remote webdriver format. ie http://<host>:<port>/wd/hub. In case the remote wd url is different then the defult pattern then insted of providing server and port just provide actual url as value of server property. For example: http://14.97.73.4:7777/wd/

* Added support/compatibility with selenium 2.25 and later for using running browser session


**Bug Fixes:**

* Fixed issue of finding element within component.

 
## VER-2_0-b06 Release Notes

**New Features:**

* Added String matcher and additional wait/assert/verify methods for element text and value with String matcher argument

**example:**

```java
 import static com.qmetry.qaf.automation.util.StringMatcher.containsIgnoringCase
 ele.verifyText(containsIgnoringCase("partial text"));
 ele.verifyText(exact("text"));
 ele.verifyText(startsWith("prefix"));
 ele.verifyText(startsWithIgnoringCase("prefix"));
 ele.verifyText(like("valid regular expr"));
 ele.verifyText(likeIgnoringCase("valid regular expr"));
```
 
 {% include note.html content='ele.verifyText("text") is equivalent to ele.verifyText(exactIgnoringCase ("text"))' %}
   
* Added Support to provide wait time for wait methods.
	
**example:**

```java
 ele.waitForText(containsIgnoringCase("partial text")); //default timeout and interval
 ele.waitForText(containsIgnoringCase("partial text"),5000); //with timeout 5000 ms and default interval 
 ele.waitForText(containsIgnoringCase("partial text"),5000,100); //with timeout 5000 ms and interval 100ms
```

* Added support for xml data provider

```java
 @IsDataProvider(key = "test.set")
 @Test
 public void xmlDPTest(String p1, String p2, String p3) {
  System.out.printf("data - p1:[%s] p2:[%s] p3:[%s]", p1, p2, p3);
 }
 
 @IsDataProvider(key = "test.set")
 @Test
 public void xmlDPTest(Map<String, String> data) {
  System.out.println("data - " + data);
 }
```

below is the content of data file

```xml 
<testdata>
  <test>
    <set>
      <val1>aaa</val1>
      <val2>bbb</val2>
      <val3>ccc</val3>
    </set>
    <set>
      <val1>xxx</val1>
      <val2>yyy</val2>
      <val3>zzz</val3>
    </set>
    <set>
      <val1>111</val1>
      <val2>222</val2>
      <val3>333</val3>
    </set>
  </test>
</testdata>  
```      
* Added support for xml property configuration

* Added locale support

  Any locale or env specific data can be stored in properties file with extension of the locale or env. It can be retrieved by getting subset.
  We need to specify which locales we want to initialize by setting following property   
  #all the locales that can be used
  
  ```properties
  load.locales= zh_CN;hi
  ```
  
  above setting will load properties from *.zh_CN *.hi files
  
  To access hi locale properties

```java
 ConfigurationManager.getBundle().subset("hi").getString("test.prop");
 props.subset("hi").getString("test.prop");
 props.subset("zh_CN").getString("test.prop");
```

There is a provision of setting default locale by providing default.locale

In that case if multiple locale loaded and want to use prop from default locale then don't require to use subset.

**For example:**

```properties
 load.locales= zh_CN;hi
 default.locale= hi
```

Then use properties directly for hi locale: 

```java
props.getString("test.prop");
```

For zh_CN: 

```java
props.subset("zh_CN").getString("test.prop")
```


{% include note.html content=" in this case where 'hi' is set to default. props.subset('hi').getString('test.prop'); will not work as 'hi' locale is default locale so will be added to main configuration." %}

Default encoding for reading locale properties is UTF-8 that can be change by providing system/application property "locale.char.encoding"
 
* Added component class and support to find component from parent element.

```java
public class TestComponent extends Component {
 public TestComponent(String locator) {
 super(locator);
 } ....
 } 
 TestComponent comp = ele.findElement("css=a", TestComponent.class);
 List<TestComponent> testComponents =
 ele.findElements("css=.pass", TestComponent.class);
```

* Added support to link java doc with test description in report. To enable set property  'report.javadoc.link=true' to set docs dir path set property `javadoc.folderpath` default is `"../../../docs/tests/"` to customize link set `report.javadoc.link.format` default value is `<a href="%s" target="_blank">[View-doc]</a>`

* Added support to pass parameter in locator string.

**For exmple**

in locator xpath=//@*/[name()=' wl:trnslate'][.='${foo.bar}']//.. ${foo.bar} will be replaced with the value of property  "foo.bar". Assuming value is xxx then the locator will become xpath=//@*[name()='.. Same way link=${foo.bar} will become link=xxx
 
It supports multiple parameters as well as same parameter multiple times.

* Added QC support - refer QC integration doc

* Support for JSON file to provide test data. Test method should expect Map.

* Added support to use map as parameter for data-driven test where db is used to provide data. column name/alise will be key and column value will be value.

* Added support to use existing driver session

Following are the steps:

 1. Webdriver session can be created through http://localhost:4444/wd/hub/static/resource/hub.html. After starting session you can perform manual steps on the browser!
 2. You need to use only appropriate remote driver for this purpose. (e.g. firefoxRemoteDriver)
 3. You need to provide session id by setting driver extra capability  webdriver.remote.session  or a property  webdriver.remote.session . The session id can
            &lt;driver&gt;.extra.capabilities=webdriver.remote.session=&lt;session id&gt; or
            webdriver.remote.session=&lt;session id&gt;
 
Introduced page launch strategy so that one can define the page launch behavior from one of the following

  * always launch from parent
  * always launch from root
  * launch page only if required (default)         
  * Added support for retry command through command tracker so that in on failure we can set retry to true and give it to one more try.
  * Added support for self-descriptive web element:
  
            For self descriptive locator you can provide json string with key "desc".
            See below example of normal and self descriptive element locator.

```java
 String LOGOUT_LINK_LOC = "{'locator':'link=Logout','desc'='Logout link'}";
 String LOGOUT_LINK_LOC ="{'locator':'key=loc.properyname.in.property.file','desc'='Logout link'}"; 
 //self descriptive
```

the element description is available by ele.getDescription().
Also it will be used in different assertion/verification messages automatically by the fw.

* Added support for fill string in given format for bean property.
	Added randomize parameter format which can be used to provide format of the value. It will work only with String properties of bean.

**Sample format**

  * 999-999-99
  * aaa-a9a-99
  * 99.99.aa
  
alphabet will replaced random alphabet and digit will replaced with random digit

* Added safari driver support. required selenium server 2.21 or latest

* Added support for Date property in data bean. Fill random data will generate random date for day range min-max to current date

* Added new FindBy Annotation so you can find element by selenium-1 locator strategy

* Added order parameter in UiElement annotation. This can be used to set order of bean properties for fillUiElement.

* Support to override any property using testng config or system property. Priority will be system property, xml config, property file.

* Additional flag "selenium.failure.screenshots" to skip screen-shot for each failure and to have single screen-shot in case of failure.

* Added support for providing extra capability through property.

	* For all driver set property "driver.extra.capabilities"
	* For specific driver set property "&lt;driver&gt;.extra.capabilities"
	
**Example:**

```properties
driver.extra.capabilities=acceptSslCerts=false;platform=WINDOWS
chrome.extra.capabilities=acceptSslCerts=true;chrome.switches=[--ignore-
certificate-errors] chrome.extra.capabilities=acceptSslCerts=true;chrome.switches=[-- 
ignore-certificate-errors, --sample-other-switch] -added support for otherDriver or
otherRemoteDriver
```

For that you must need to set property "other.extra.capabilities".
In case of otherDriver (not remote driver) you must need to provide driver class as capability : "driver.class",

**Example :**

```properties
other remote driver
other.extra.capabilities=platform=ANY;browserName=safari
other driver 
other.extra.capabilities=driver.class=org.openqa.selenium.safari.SafariDriver
```
 
* Added new form data bean and UiElement annotation

* Support to java script expr for dependent value match in form data bean

* Added support to use map as parameter for data-driven test, first row will be treated as header row header column will considered as key.

* Added support to populate bean from map having bean properties as keys

* Added skip parameter in Randomizer annotation

 
**Data bean random data generation:**
 
* Added support for min and max value 
 
**Improvements:**

* Updated properties util, derived from PropertiesConfiguration [dependency of commons-configuration.jar]

* Updated data-provider functionality

* Updated data driven functionality

	* added new annotation IsDataProvider 

	* added listener IsAnnotationTransformer

**Properties:**

For test data:
&lt;tc&gt;.testdata=&lt;param&gt;=value;&lt;param&gt;=value globle.
&lt;param&gt; are one of the parameter of IsDataProvider (dataFile, sheetName,hasHeaderRow,labelName,sqlQuery)

Discontinue to support property CMD_EXECUTION_INTERVAL (key:commands.execution.interval) to Set execution interval between two selenium commands.

**Bug Fixes:**

1. Fix for IllegalAccessException while filling random data in non public variables
2. Fixed issue of varification failed test considered as pass with testng 6.

 
## VER-2_0-b05 Release Notes

**New Features:**

* Added support to provide application.properties file by setting system property application.properties.file.

* Added support to provide read test.props.dir from system property if exist.

**Improvements:**

* Separated UI code as ISFW-UI project

* Updated to search server jar in all folders in project if selenium server is not started

* Updated QMetry integration to support new Scheduler xml and web-service

* added support for filter by script name, runid

* added support for multiple script name, tc id, run id

* Separated Qmetry integration from ISFW base test case and provided

**Bug Fixes:**

* Fixed issue of execution fail if application.properties file not found.

 
 
## VER-2_0-b04 Release Notes

**New Features:**

* Added setAttribute custom command

**Improvements:**

* Updated excel data provider

 
## VER-31072011 Release Notes
  
  Posted on July 5, 2011

**New Features:**

* Added new feature to set interval between two commands by setting property

* Added capture screen shot support for remote web-driver

* Added Web-driver support

 
## VER-05042011 Release Notes
  Posted on Apr 5, 2011

**New Features:**

* Added

	* ClipBoardUtil

	* ImageCompareUtil (requires JAI dependency jars )

* Tage: extjs

* Support for Extjs component model

For example purpose inbuilt basic implementation for ExtGrid, ExtTree, ExtCombo
 
**Improvements:**

* Updated ConfigurationManager default application properties file name to "resources/application.properties" from "resources/appication.properties"

* Used property "selenium.screenshots.kwargs" as parameter while capturing screen shot

 
## VER-04032011 Release Notes
  
  Posted on Mar 4, 2011

**New Features:**

* Added flexCall method for support to call flash/flex methods not available in client interface/implementation.

**Improvements:**

* Updated BaseTestCase, moved result updater thread pool to ResultUpdator

**Bug Fixes:**

* Fixed issue of null point exception in testNG group run.

 
## VER-19022011 Release Notes

  Posted on Feb 19,2011

**New Features:**

* Added ext component implementation with basic functionality for

	* Grid

	* Tree

	* Combo-box

	* Window

* Added support for flex api

	* Extended selenium interface

	* Extended selenium implementation


## VER-11022011 Release Notes
  
  Posted on Feb 11, 2011
 
**New Features:**

* Selenium Command Listener

* Ability to create and register listeners to perform before and after events while executing selenium command from command processor.

	* Inbuilt AutoWaitInjector - auto wait listener, that can be registered to inject wait for locator before executing commands

	* Inbuilt IEScreenshotListner - auto scroll window before capturing screen-shot.

* Added ajaxwait ability for phpjs toolkit

 
## VER-05022011 Release Notes

  Posted on Feb 05,2011

**New Features:**

**ExtJs utility:**

* Classes to construct extjs container and component object using framework.

* Any ext component can be pointed using component class object

**Ajax wait:**

  * waitForAjaxToLoad() and waitForAjaxToLoad(JavaScriptToolkit) methods to support wait forajaxrequest to complete.
  * At present supportingajaxwait for following toolkit.

	  * DOJO
	  
	  * EXTJS
	  
	  * JQUERY
	  
	  * YUI
	  
	  * PROTOTYPE

* Tested for extjs with example application, for other toolkit testing not done

**IS Automation framework UI:**

* Settings tab

	* Test runner can
	
	* view list of property files loaded.
	
	* view all properties loaded from selected file.
	
	* set/update property value and load (Hot-fix property value also possible)
		
It might be helpful when updating (Hot-fix) property value after started run for pending test. For example, started test run with initial 10000 wait timeout value which is AVG value for AUT. But found AUT much slow after some test executed and want to change wait time for remaining tests,  then one can set and load property value say 20000 that will take effect immediately .

 
## VER-28012011 Release Notes

  Posted on Jan 28, 2011

**New Features:**

**IS Automation framework QMetry Integration:**

* Scheduler filter

	* Accepts scheduler file as argument to run script; filters test case and run tcs provided in scheduler xml file

* Posting run results to QMetry: for fail and pass test-cases.

* Parameter mapping

	* QMetry TC ID : using one of following way (annotation has priority)

* QMetry annotation TC_ID value eg. @QmetryTestCase(TC_ID = "999")

* test case name with prefix TC eg TC999 maps to Qmetry TC ID 999

	* QMetry project, build, release, suit, platform: from scheduler xml file.

	* QMetry instance URL, user, pwd : from application properties

	* Supported way

		* Executing test runner from other tools :  execute batch file  or ant script

* call bath file: seleniumToolRunner.bat &lt;scheduler file&gt;

* call ant script: ant -f seleniumtestrunner.xml -Dqmetry.schedule.file=&lt;scheduler file&gt;

	* UI (Supposed): Select scheduler file

**IS Automation framework UI:**

* GUI for start/stop automation run

* User can stop automation run at any point of time and can get report of run test-cases, not-run will marked as skipped.

* Integrating third party XML editor tool for configuration file editing

* Stop/check-status/Start selenium server from UI; (start support only for localhost)

Selenium IDE code formator

 
## VER-30122010 Release Notes

  Posted on Dec 30, 2010

**Improvements:**

* Removed use of property selenium.report.dir used test.results.dir instead

* Provided work-around for base URL with basic authentication not working when directly open /*[/*]

 
## VER-28122010 Release Notes
  
  Posted on Dec 12,2010

**Improvements:**

**Bug Fixes:**

* Fixed issue of selenium tear down with parallel data provider

## VER-25122010 Release Notes

  Posted on Dec 25, 2010

**Improvements:**

* Updated framework to provide report message filtering with pass, fail, info check-box with each test case result

* Updated reportNG with JS and CSS for the same. jar: ireportng-1.1.3.jar

* Updated framework to support configurable Selenium base url

* Now supports baseUrl property in testNG configuration file

**Bug Fixes:**

* Fixed issue of opened browser when running in parallel mode with data-provider

 
## VER-Old Release Notes
 
**Features:**

* Excel data provider. It will allow non Windows operating systems to use Excel spreadsheets as data file.

* CSV data provider support.

* Support to distributed server for different browse

* Configurable capturing screen-shot capability

* Parallel support

* Logging selenium

* Test Page based framework design with support of different page concepts.

* Record of assertion/verification messages

* Customized reportNg to provide tc details, assertion/verification messages and selenium command log

* Auto scroll while capturing screen-shot 
