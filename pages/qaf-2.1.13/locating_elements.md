---
title: Locating Elements
sidebar: qaf_2_1_13-sidebar
permalink: qaf-2.1.13/locating_elements.html
folder: qaf-2.1.13
tags: [Selenium, locator, java, webdriver, webelement]
---

## Locating Elements

In order to locate element QAF provides selenium-1 style syntax. This approach has two benefits: 
 * First of all, you don't need to hardcode or fix strategy for locator in code (for example By.xpath(myxpath)) with qaf locator strategy you can easily switch form one locator to anoter locator either strategy or locator value. 
 * Secondly, you can seperate out locator outside your code by using locator repository, where you can maintain locator without code change.

### Element locator syntax

```
<locator strategy>=<locator value>
```
Where, &lt;locator strategy> is any of the strategy supported by underlying web-driver and &lt;locator value> is locator in that strategy. 

**Webdriver supported strategies**: `id`, `name`, `xpath`, `css`, `link`, `partialLink`, `className`, `tagName`

###### Examples:

```
css=<css locator for element>
xpath=<xpath locator for element>

```

In order to use other custom locator strategy supported by the underlying driver(s), you need to use strategy name as locator strategy. For example, Appium provides _MobileBy.AccessibilityId_, _MobileBy.AndroidUIAutomator_, _MobileBy.IosUIAutomation_. The strategy name used by appium for these additional selectors is _accessibility id_, _-android uiautomator_ and _-ios uiautomation_ respectively [refer](https://github.com/appium/java-client/blob/master/src/main/java/io/appium/java_client/MobileSelector.java).

###### Examples:

```
accessibility id=<accessibilityId for element>
-android uiautomator=<uiautomatorText for element>
-ios uiautomation=<iOSAutomationText for element>
-ios predicate string=<predicate string for element>
-windows uiautomation=<uiautomationText for element>

```


## Self-descriptive locator
Self-descriptive locator holds locator for element along with description of the element. Description will be used by the framework in assertion/verification messages for the element. You also can take advantage of self-descriptive locator to provide additional custom meta-data with element locator. 

### Self-descriptive locator syntax
Self-descriptive locator expects JSON map of _locator-mata-key_ and _locator-metadata-value_ pair:

**Syntax:**

``` javascript
{'locator':'<locator strategy>=<locator value>','<locator-mata-key1>' = '<locator-mata-value1>','<locator-mata-keyN>' = '<locator-mata-valueN>'}
```

Following are reservered  _locator-mata-keys_
  * locator: actual locator of element (**mandatory**)
  * desc: description of the locator (optinal)
  * cacheable: flag to indicate is element is cashable or not (optional)
  * component-class: to specify object to be created of. Possible value fully qualified class name that extends QAFWebComponent 

Following  _locator-mata-keys_ is used by `ElementMetaDataListener`
  * type: for send-keys to specify element type, possible values:
	 * password/encrypted - required to decode before send keys
	 * select - specify this is select (basic support only) with options and choose option
	 * Examples:
	 	* {'type': 'password'}
	 	* {'type': 'select'}
  * scroll: to specify scroll behavior, possible values:
	   * Always/true - always scroll before commands required scroll
	   * OnFail - retry with scroll on failure for commands required scroll
  * scroll-options: to specify scroll options, A value that indicates the type of the align Examples:
	 	* {'scroll-options': 'true'}
	 	* {'scroll-options': 'false'}
	 	* {'scroll-options': '{block: \'center\'}'}
	  Refer https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollIntoView
  * sendkeys-options: to specify one or more send-keys options, possible values:
	 * click: to specify click before send-keys
	 * clear: to specify clear before send-keys
	 * Examples:
	 	* {'sendkeys-options': 'clear'}
	 	* {'sendkeys-options': 'click'}
	 	* {'sendkeys-options': 'click clear'}
	 

**Examples:**
```
{'locator':'<locator strategy>=<locator value>','desc':'Description of element'}
```
Where <locator strategy> is any of the strategy supported by underlying web-driver and <locator value> is locator in that strategy. 


```java
{'locator':'css=.header';'desc':'Header of Page'}
{"locator":"xpath=//*[@name='Result']","desc":"Input box"}
```


**Custom meta-data example:**

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

