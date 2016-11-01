---
title: Locating Elements
sidebar: qaf_latest-sidebar
permalink: latest/locating_elements.html
folder: latest
tags: [Selenium, locator, java, webdriver, webelement]
---

## Locating Elements

In order to locate element QAF provides selenium-1 style syntax. This approach has two benefits: First of all you don't need to hardcode or fix strategy for locator in code (for example By.xpath(myxpath)) with qaf locator strategy you can easily switch form one locator to anoter locator either strategy or locator value. Secondly you can seperate out locator outside your code by using locator repository, where you can maintain locator without code change.

### Element locator syntax

```
<locator strategy>=<locator value>
```
Where <locator strategy> is any of the strategy supported by underlying web-driver and <locator value> is locator in that strategy. 

Webdriver supported strategies: id, name, xpath, css, link, partialLink, className, tagName

###### Examples:

```
css=<css locator for element>
xpath=<xpath locator for element>

```

In order to use other custom locator strategy supported by the underlying driver(s), you need to use strategy name as locator strategy. For example, Appium provides MobileBy.AccessibilityId, MobileBy.AndroidUIAutomator, MobileBy.IosUIAutomation. The strategy name used by appium for these additional selectors is accessibility id, -android uiautomator and -ios uiautomation respectively [refer](https://github.com/appium/java-client/blob/master/src/main/java/io/appium/java_client/MobileSelector.java).

###### Examples:

```
accessibility id=<accessibilityId for element>
-android uiautomator=<uiautomatorText for element>
-ios uiautomation=<iOSAutomationText for element>
-ios predicate string=<predicate string for element>
-windows uiautomation=<uiautomationText for element>

```


## Using @FindBy
```
@FindBy(locator = "<Element Locator>")
@FindBy(locator = "css=<css locator>")
@FindBy(locator = "xpath=<xpath locator>")
@FindBy(locator = "id=<element id>")
@FindBy(locator = "name=<element name>")
@FindBy(locator = "link=<link text>")
@FindBy(locator = "partialLink=<partial link text>")
@FindBy(locator = "className=<class name>")
@FindBy(locator = "tagName=<tagName>")
@FindBy(locator = "<attribute-name>=<attribute-value>")
@FindBy(locator = "key=<property that holds actual locator>")
@FindBy(locator = "property that holds actual locator")

```

## Self-descriptive locator
Self-descriptive locator holds locator for element along with description of the element. Description will be used by the framework in assertion/verification messages for the element.

### Self-descriptive locator syntax
Self-descriptive locator expects JSON string with following keys:

  * locator: actual locator of element
  * desc: description of the locator (optinal)
  * cacheable: flag to indicate is element is cashable or not (optional)


```
{'locator':'<locator strategy>=<locator value>','desc':'Description of element'}
```
Where <locator strategy> is any of the strategy supported by underlying web-driver and <locator value> is locator in that strategy. 

**Example:**

```java
{'locator':'css=.header';'desc':'Header of Page'}
{"locator":"xpath=//*[@name='Result']","desc":"Input box"}
```

Below interface holds locators that are used in "HomePage". Here locators are self descriptive locators.

```java
public interface HomePageLocators {
 static final String HEADER_LOC = "{'locator':'css=.header';'desc':'Header of Page'}";
    static final String MENU_LOC = "{'locator':'css=.menu_area';'desc':'Menu of Page'}";
    static final String SLIDER_LOC = "{'locator':'css=.nivoSlider';'desc':'Slid Show in Home Page'}";
    static final String SEARCH_TEXTBOX_LOC = "{'locator':'css=#q';'desc':'Search Text Box'}";
    static final String SEARCH_BUTTON_LOC = "{'locator':'css=.search_bg a';'desc':'Search Button'}";
}
```

You also can take advantage of self-descriptive locator to provide additional custom meta-data with element locator. For example:

```
{"locator":"xpath=//*[@name='Result']","desc":"Input box","context":"WEBVIEW"}
```
In above locator "context" is custom meta-data which can be accessed in code by element.getMetadata().


## Alternate Locator

You can provide more than one locator to locate the webelement. In such case when element will not found with first locator then it will try to find with second and so forth.

**Normal locator:**

```
['css=#qa','name=eleName']
['css=#qa','name=eleName','xpath=.//[@id=\'issue-tabs\']']
```

**Self Descriptive locator:**

```
{'locator' : ['css=#qa','name=eleName']; 'desc' : 'dummy element'}
{'locator' : ['css=#qa','name=eleName','xpath=.//*[@id=\'issue-tabs\']']; 'desc' : ' dummy element '}
```

## jQuery Locator

Borrowing from CSS 1–3, and then adding its own, jQuery offers a powerful set of selectors for matching a set of elements. QAF have now provided one more locator strategy named "jQuery", by using it you can locate element by [jQuey css selectors.](http://api.jquery.com/category/selectors/)
**Usage:**

**Normal locator:**

```properties
jquery = <jqueryloc>
Jquery = .classname
Jquery = div:contains('some text')
```

**Self Descriptive locator:**

```
{'locator' : 'jquery=<jqueryloc>', 'desc' : 'dummy element'}
```

