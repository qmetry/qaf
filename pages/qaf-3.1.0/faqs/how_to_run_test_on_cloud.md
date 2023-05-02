---
title: How to run test on cloud or selenium grid?
sidebar: faq_sidebar
permalink: qaf-3.1.0/how_to_run_test_on_cloud_or_selenium_grid.html
folder: qaf-3.1.0
---


First of all user must required valid cloud exection provider's account which user can create from their web site.

To run QAS test on cloud user required to add following keys either in  **application.properties** or in **testrun_config.xml**

```properties

driver.name=remotedrivername

```

where **"remotedrivername"** can be "firefoxRemoteDriver","chromeRemoteDriver"  or any other valid remote driver

```properties

remote.server=<url_where_selenium_grid_is_running>

```


User can also provide additional properties of driver using **"browser.additional.capabilities"** key

where **"browser"** can be "firefox", "chrome" or any other browser

**Given below keys with proper values**

```properties

driver.name=firefoxRemoteDriver

remote.server=<url_where_selenium_grid_is_running>

firefox.additional.capabilities={'name':'SampleTestOnFirefox','platform':'Windows 8','version':'40.0'}

```



