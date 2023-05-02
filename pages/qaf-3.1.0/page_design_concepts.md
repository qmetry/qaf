---
title: Page Design Concepts
sidebar: qaf_3_1_0-sidebar
permalink: qaf-3.1.0/page_design_concepts.html
folder: qaf-3.1.0
tags: [page,java,locator,testng]
---


## Page Hierarchy
One page becomes parent/launcher of another page(s) results in creating navigation hierarchy.

### Single navigation route

This is the normal page hierarchy were a page can be launched from only one page. Consider the example where page P is home page, P11 and P12 can be navigated from P and P111 and P121 can be navigated trough P11 and P12 respectively. Considering page hierarchy for P->P11->P111, P will become parent page for P11 and P11 will become parent page for P111.
Below is the selenium2 API code sample for creating page hierarchy for above navigation.
As P is home page or have no parent page, while creating page P we will just place a test page interface.


```java
public class P extends WebDriverBaseTestPage<WebDriverTestPage> {
    @FindBy(locator = P1l_LINK_LOC)
    private QAFWebElement p1Link;
    @Override
    protected void openPage(PageLocator arg0, Object... arg1) {
        driver.get("/");
    }
    public QAFWebElement getP1Link() {
        return p1Link;
    }
}
```

While creating page P1 you need to specify "P" as parent page by providing "P" as base class parameter.

```java
public class P1 extends WebDriverBaseTestPage<P> {
    @FindBy(locator = "P1l_LINK_LOC")
    private QAFWebElement p11Link;
    @Override
    public boolean isPageActive(PageLocator loc, Object... args) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    protected void openPage(PageLocator arg0, Object... arg1) {
        parent.getP1Link().click();
    }
}
```

While creating page P2 you need to specify "P1" as parent page by providing "P1" as base class parameter.


```java
public class P11 extends WebDriverBaseTestPage<P1> {
    @Override
    public boolean isPageActive(PageLocator loc, Object... args) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    protected void openPage(PageLocator arg0, Object... arg1) {
        parent.getP11Link().click();
    }
}
```

### Multiple navigation routes

The framework has multiple route support. In case of multiple routes, to navigate to specific page you need to define launcher interface that extends TestPage and provide that interface as parent to the page with multiple route. Implement launcher interface in each possible parent class. At the time of defining test provide parent as argument while creating page object.
Selenium1 API Implementation

 * Create child interface of WebDriverBaseTestPage that defines one method to open page under question.

```java
interface SamplePageLauncher extends WebDriverBaseTestPage {
 public void launchSamplePage(PageLocator loc, Object... args);
}
```

 * Implement that interface in all possible parent pages.

```java
class Route1Page extends SeleniumBaseTestPage<Route1ParentPage> implements SamplePageLauncher {
    ...
     
    @Override
    public void launchSamplePage(PageLocator loc, Object... args) {
        selenium.click("locator");
    }
     
    ...
}
```

 * Another parent:

```java
class Route2Page extends SeleniumBaseTestPage<Route2ParentPage> implements SamplePageLauncher {
     
    ...
     
    @Override
    public void launchSamplePage(PageLocator loc, Object... args) {
        // steps to open sample page from this page. For example:
        selenium.click("locator");
    }
     
    ...
}
```

 * Set Interface created in as parent page parameter of the page class and call interface method in open page.

```java
public class SamplePage extends SeleniumBaseTestPage<SamplePageLauncher> {
    public SamplePage(SamplePageLauncher parent) {
        super(parent);
    }
    public SamplePage() {
        // set default route
        this(new Route1Page());
    }
    @Override
    protected void openPage(PageLocator loc, Object... args) {
        parent.launchSamplePage(loc, args);
    }
    ...
}
```

**Selenium2 API implementation**

 * Create child interface of WebDriverTestPage that defines one method to open multi route page under question.

```java
interface SamplePageLauncher extends WebDriverTestPage {
    public void launchSamplePage(PageLocator loc , Object... args);
}
```

 * Implement that interface in all possible parent pages.

```java
class Route1Page extends WebDriverBaseTestPage<Route1ParentPage> implements SamplePageLauncher {
  
    ...
  
    @Override
    public void launchSamplePage(PageLocator loc, Object... args) {
        // steps to open page. For example:
        pageLink.click();
    }
  
    ...
}
```

Another parent of Sample page:

```java
class Route2Page extends WebDriverBaseTestPage<Route2ParentPage> implements SamplePageLauncher {
    ...
    @Override
    public void launchSamplePage(PageLocator loc, Object... args) {
        // steps to open child page from this page. For example:
        pageLink.click();
    }
    ...
}
```

   * Set Interface created in as parent page parameter of the page class and call interface method in open page.

```java
public class SamplePage extends WebDriverBaseTestPage<SamplePageLauncher> {
    public SamplePage() {
        // set default route
        this(new Route1Page());
    }
    public SamplePage(SamplePageLauncher parent) {
        super(parent);
    }
    @Override
    protected void openPage(PageLocator loc, Object... args) {
        parent.launchSamplePage(loc, args);
    }
     
    ...
}
```

Your test method will look like:

```java
@Test
public void testMethod() {
    // Default Route
    SamplePage samplePage = new SamplePage();
 
 
    // route 1
    // SamplePage samplePage = new SamplePage(new Route1Page());
    // route 2
    // SamplePage samplePage2 = new SamplePage(new Route2Page());
    samplePage.launchPage(pageLocator);
}
```

### Dynamic Page Flow

This is the case same as multiple route page hierarchy, the only difference over here there will be only one route will exist with specific workflow configuration of AUT. Here you can define parent either in constructor or in initParent method according to workflow.

{% include inline_image.html file="Dynamic_Page_Flow.png" alt="Dynamic Page Flow " %}

You need to create interface and implement that interface in all possible parents same as multi route page example steps 1 and 2. Step 3 will look like given below

```java
public class PaymentPage extends WebDriverBaseTestPage<PaymentsPageLauncher> {
    public PaymentPage() {
        // set route according to current workflow
        parent = (pageProps.getInt("review.next.flow") == 6) ? new ReviewFlightPage() : new PassengerPage();
    }
}
```

Or alternatively override init parent:

```java
@Override
protected void initParent() {
    // set route according to current workflow
    parent = (pageProps.getInt("review.next.flow") == 6) ? new ReviewFlightPage() : new PassengerPage();
}
```

### Page Template

{% include inline_image.html file="PageTemplate.png" alt="Page Template " %}

Template page implementation derives common functionality from Template. It will result in reusability of code and higher level of maintainability. TopNavTab and TabbedMenuItem perform like template for other pages. TopNavTab is extended by each Tab e.g. OverviewTab, MyCompanyTab

### Linked Pages

#### Static Linked Page

```java
public ViewPage clickSave() {
    saveBtn.click();
    ViewPage expectedPage = new ViewPage();
    expectedPage.waitForPageToLoad();
    return expectedPage;
}
```

#### Dynamic Linked page

Page can be linked with action in another page. Expected page can be provided while writing the test. For Example, in registration flow, expected page on "Save" action in "Edit Basic Info page" is "Edit contact Info Page".

{% include inline_image.html file="Dynamic_Linked_Pages.png" alt="Dynamic Linked Pages " %}

```java
public <T extends BaseTestPage<TestPage>> T clickSave(T expectedPage) {
    saveContinueBtn.click();
    expectedPage.waitForPageToLoad();
    return expectedPage;
}
```
