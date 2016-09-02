---
title: Page Locator and Object arguments in Launch page
sidebar: qaf_2_1_7b-sidebar
permalink: qaf-2.1.7b/pagelocator_objectarg_launchpage.html
folder: qaf-2.1.7b
tags: [page,java,Selenium,locator]
---


The page locator is intended for identifying exact locator when there are multiple locators available to open the page from the parent page.

The ideal example is item details page which can be open from item list page.

On item list page there can be list of items with available action for each item and each item's "view details" action takes you to item details page.

Here the page locator provides a way to locate specific item's details page. For example open Item Details page for Item xxx. In other cases you can ignore it.
Above example of item details page be accomplished by use of page locator. In Item details page you might have openPage implementation like:

```java
@Override
protected void openPage(PageLocator loc){
    if (loc != null) {
        parent. openItemDetails (loc.getLocator());
    }
}
```


In itemLists page there can be

```java
//Selenium1 API
public void openItemDetails(String itemName) {
    // create element locator for example String eleLoc = "link=" + itemName;
    selenium.click(eleLoc);
}
```
 
```java
// Webdriver API
public void openItemDetails(String itemName) {
    // assuming getItem(String) available in parent page
    parent.getItem(itemName).click();
}
```

While launching the Item Details page you need to provide appropriate page locator as below:

```java
ItemDetailsPage itemDetailsPage = new ItemDetailsPage();
itemDetailsPage.launchPage(new DefaultPageLocator("XYZ"));
```

## Complex example of page hierarchy and page locator

Consider that in online shopping cart ecommerce application, through admin UI, admin can upload items, supplier etc from excel or another file. After upload there is a status page which shows upload details and provides a link to see details of upload. From status page admin can select and edit individual supplier or item information.

```java	
public class EditSupplierDetailPage extends WebDriverBaseTestPage<WebDriverTestPage> {
    @Override
    protected void openPage(PageLocator locator, Object... args) {
        parent.editSupplierData(locator.getLocator());
    }
    // helper method
    public static PageLocator creatPageLocator(String supplierName, String fileNameOrJobID) {
        return new DefaultPageLocator(supplierName, UploadDetailsPage.createPageLocator(supplierName, fileNameOrJobID));
    }
}
```

```java
public class UploadDetailsPage extends WebDriverBaseTestPage<WebDriverTestPage> {
    @Override
    protected void openPage(PageLocator locator, Object... args) {
        parent.viewSupplierUploadDetails(locator.getLocator());
    }
    public void editSupplierData(String supplierName) {
        selenium.click("link=Edit " + supplierName);
    }
    public static PageLocator createPageLocator(String supplierName, String fileNameOrJobID) {
        return new DefaultPageLocator(supplierName, UploadStatusPage.createPageLocator(fileNameOrJobID));
    }
}
```

UploadStatusPage requires jobid or uploaded filename.

```java	
public class UploadStatusPage extends WebDriverBaseTestPage<WebDriverTestPage> {
    @Override
    protected void openPage(PageLocator locator, Object... args) {
        parent.viewPostUploadResults(locator.getLocator());
    }
    public void viewSupplierUploadDetails(String supplierName) {
        getSupplier(supplierName).click();
    }
    public static PageLocator createPageLocator(String fileNameOrJobID) {
        return new DefaultPageLocator(fileNameOrJobID);
    }
}
```

```java
public class UploadHistoryPage extends WebDriverBaseTestPage<WebDriverTestPage> {
    @Override
    protected void openPage(PageLocator locator) {
        parent.openLink(SUPPLIER_TAB_LINKS.UPLOAD_HISTORY_LINK_LOC);
    }
    public void viewPostUploadResults(String filenameOrJobID) {
        String postUploadResultLink = filenameOrJobID == null || filenameOrJobID.trim().equalsIgnoreCase("")
                ? DEFAULT_POSTUPLOAD_RESULT_LINK_LOC : getPostUploadResultLink(filenameOrJobID);
        postUploadResultLink.click();
    }
} 
```

## Object arguments

Page locator has some time limited scope for providing launch hierarchy parameters.

 

