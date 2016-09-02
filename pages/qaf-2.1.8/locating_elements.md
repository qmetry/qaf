---
title: Locating Elements
sidebar: qaf_2_1_8_sidebar
permalink: qaf-2.1.8/locating_elements.html
folder: qaf-2.1.8
tags: [Selenium,locator,java]
---
You can observe that @FindBy annotation is used from QAF. It has one string parameter locator that's value can be provided either by direct locator in selenium 1 locator format or a self descriptive locator.

Following are different possible way to provide locator value:


## Id or name or value

Example: 

```java
@FindBy(locator = "elementID")
```


## Selenium 1 style

```
<locator strategy>=<value>
```

```java
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
```

## Self-descriptive locator
Self-descriptive locator holds locator for element along with description of the element. Description will be used by the framework in assertion/verification messages for the element.

Self-descriptive locator expects JSON string with following keys:

  * locator: actual locator of element
  * desc: description of the locator
  * cacheable: flag to indicate is element is cashable or not

**Example:**

```java
@FindBy(locator = "{'locator':'css=.header';'desc':'Header of Page'}")
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

