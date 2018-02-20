---
title: Creating Test Pages
sidebar: qaf_latest-sidebar
permalink: latest/creatingtestpages.html
folder: latest
tags: [java,page]
---

## Implementation of abstract methods

The **BaseTestPage** class contains following abstract methods that need to be implemented in test page class.
	
```java
protected void initParent()
protected void openPage(PageLocator loc)
public boolean isPageActive()
```

**initParent :**

Initialize parent in this method. **For example** in MyAccount class the implementation look like:

```java	
protected void initParent(){
    parent = new HomePage();
}
```


**openPage :**

This method provides page locator as argument which is passed when calling launch page. You can assume that at the time when this method gets executed, the parent page will be available in browser. So you can call any of the method available in parent class that navigates you to this page or perform some action to open this page.

**For HomePage :**

```java	
protected void openPage(PageLocator loc){
    driver.get("/");
}
```

**For MyAccountPage:**

Assuming that in parent page (HomePage in this example), there is openMyAccount method defined to navigate to my account from home page.

```java	
protected void openPage(PageLocator loc){
    parent.openMyAccount();
}
```

Here is another example that directly access parent page element to navigate to My Account page.

```java	
protected void openPage(PageLocator loc){
    parent.getMyAccountLink().click();
}
```

Here is another example that uses locator to identify unique item on parent page for which item detail view page can be open.

```java	
protected void openPage(PageLocator loc){
    parent.viewCartItem(loc.getLoc());
}
```

## Wait Service

Each page derives wait service from **BaseTestPage**. Wait service provide different wait method that can be used in your page functionality.

**Overloading methods of Base class**

There are some methods that have implementation in **BaseTestPage** and can be overridden as per nature of the page or to provide some additional functionality before and after launch and wait for page to load.

```java	
@Override
public void waitForPageToLoad() {
    //custom implementation
}
```

Override before launch and after launch method to provide before/after launch steps to be taken. 

{% include inline_image.html file="testpageflow.png" alt="Test Page Flow" %}

To create test page using webdriver API you need to extend **WebdriverBaseTestPage<P>** class. It supports all the above features and provide handle of webdriver instead of selenium, taking care to initialize webelement annotated with **@FindBy** annotation provided by selenium or QAF. It also supports custom components.


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
When you are using @FindBy annotation in page or in component following element meta-data will be added to element by the QAF:

	* pageClass : name of the class that declares the element (e.g: HomePage in example below)
	* objectName : name of the object (e.g: header in example below for HomePage.header)

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

Below is sample test page:

```java	
import com.qmetry.qaf.automation.ui.annotations.FindBy;
...
public class HomePage extends WebDriverBaseTestPage<WebDriverTestPage>
        implements
            HomePageLocators {
    @FindBy(locator = HEADER_LOC)
    private TopHeader header;
    @FindBy(locator = MENU_LOC)
    private TopMenu menu;
    @FindBy(locator = SLIDER_LOC)
    private QAFWebElement slider;
    @FindBy(locator = SEARCH_TEXTBOX_LOC)
    private QAFWebElement searchTextbox;
    @FindBy(locator = SEARCH_BUTTON_LOC)
    private QAFWebElement searchButton;
    @Override
    public boolean isPageActive(PageLocator loc, Object... args) {
        return header.isPresent();
    }
    @Override
    protected void openPage(PageLocator arg0, Object... arg1) {
        driver.get("/");
    }
    public TopHeader getTopHeader() {
        return header;
    }
    public TopMenu getTopMenu() {
        return menu;
    }
    public QAFWebElement getSlider() {
        return slider;
    }
    public QAFWebElement getSearchtextBox() {
        return searchTextbox;
    }
    public QAFWebElement getSearchButton() {
        return searchButton;
    }
}
```

### Page identifiers (since 2.1.13)

You can annotate webelement with `@Qualifier` annotation. All element with `@Qualifier` Annotation will be considered as page identifier. It will be used by `waitForPageToLoad()` and `isPageActive()` to check page is active or to wait for to page load.

```java	
import javax.inject.Qualifier;
...
public class HomePage extends WebDriverBaseTestPage<WebDriverTestPage>
        implements
            HomePageLocators {
    
    @FindBy(locator = HEADER_LOC)
    private TopHeader header;
    
    ...
    
    @Qualifier
    @FindBy(locator = SEARCH_TEXTBOX_LOC)
    private QAFWebElement searchTextbox;
    
    @Qualifier
    @FindBy(locator = SEARCH_BUTTON_LOC)
    private QAFWebElement searchButton;
    
    ...
}

```
    