---
title: How to run test on suace lab?
sidebar: faq_sidebar
permalink: qaf-2.1.14/how_to_run_test_on_suace_lab.html
folder: qaf-2.1.14
---


First of all user must required valid sauce lab account which user can create from sauce lab web site.

To run QAS test on Sauce Lab user required to add following keys either in  **application.properties** or in **testrun_config.xml**

```properties

driver.name=remotedrivername

```

where **"remotedrivername"** can be "firefoxRemoteDriver","chromeRemoteDriver"  or any other valid remote driver

```properties

remote.server=http://<username>:<access-key>@ondemand.saucelabs.com:80/wd/hub

```

where **"username"** = sauce lab user name , **"access-key"** = sauce lab access key

User can also provide additional properties of driver using **"browser.additional.capabilities"** key

where **"browser"** can be "firefox", "chrome" or any other browser

**Given below keys with proper values**

```properties

driver.name=firefoxRemoteDriver

remote.server=http://<username>:<access-key>@ondemand.saucelabs.com:80/wd/hub

firefox.additional.capabilities={'name':'SampleTestOnFirefox','platform':'Windows 8','version':'40.0'}

```



