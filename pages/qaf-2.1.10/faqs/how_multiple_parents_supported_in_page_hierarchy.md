---
title: How multiple parents supported in page hierarchy?
sidebar: faq_sidebar
permalink: qaf-2.1.10/how_multiple_parents_supported_in_page_hierarchy.html
folder: qaf-2.1.9
---


The framework has multiple route support. In case of multiple route, to navigate to specific page you need to define launcher interface that extends BaseTestPage and provide that interface as parent to the page with multiple route.Implement launcher interface in each possible parent class. At the time of defining test provide parent as argument while creating page object.
Here is the example for better understanding:

```java

interface MultiRoutePageLauncher extends TestPage {
public void launchMultiRoutePage(PageLocator loc);
}

public class MultiRoutePage extends BaseTestPage {

public MultiRoutePage(MultiRoutePageLauncher parent) {
super(parent);
}


public MultiRoutePage() {
// either provide default route
//or remove this constructor

this(new Route1Page());
}


@Override
protected void openPage(PageLocator loc) {
parent.launchMultiRoutePage(loc);

}
…
}


public class Route1Page extends BaseTestPage implements MultiRoutePageLauncher {
@Override
public void launchMultiRoutePage(PageLocator loc) {
// steps to open page.
}
…
}


public class Route2Page extends BaseTestPage implements MultiRoutePageLauncher {
@Override
public void launchMultiRoutePage(PageLocator loc) {
// steps to open page.
}
…
}

```

Your test method will look like:

```java

@Test
public void testMethod() {
// Default Route
MultiRoutePage multiRoutePage = new MultiRoutePage();
// route 1
// MultiRoutePage multiRoutePage = new MultiRoutePage(new Route1Page());
// route 2
//MultiRoutePage multiRoutePage = new MultiRoutePage(new Route2Page());
MultiRoutePage.launchPage(pageLocator);
…
}
```
