---
title: Testing Mobile web in desktop Browser with modify header
sidebar: qaf_2_1_7b_sidebar
permalink: qaf-2.1.7b/testing_mobile_web_in_desktop_browser.html
folder: qaf-2.1.7b
---

## Using Firefox

1. Install [modify-headers](https://addons.mozilla.org/en-us/firefox/addon/modify-headers/) plug-in in Firefox.

2. Create new ff profile

3. Set appropriate user-agent in profile

**To run using driver (not remote driver):**

While running script pass additional system property ```-Dwebdriver.firefox.profile=<profilename>```
or in ant target add ```<systemproperty name="webdriver.firefox.profile" value="<profilename>">```.

Set property

```properties
driver.name=firefoxDriver
```

**To use remote driver**

Start selenium server with ```-Dwebdriver.firefox.profile=<profilename>```

Set properties

```properties
remote.server="selenium server host ip"
remote.port="port on which selenium server running"
driver.name=firefoxRemoteDriver
```

## Using Chrome

* Provide following property in application.properties file

```properties
chrome.additional.capabilities={"chromeOptions":{"mobileEmulation":{"deviceName":"Google Nexus 5"}}}
webdriver.chrome.driver = chromedriver path
driver.name=chromeDriver
```

* Follow the link for specifying a device from the [DevTools Emulation panel](https://developer.chrome.com/devtools/docs/mobile-emulation) as the value for “deviceName". [Mobile Emulation](https://sites.google.com/a/chromium.org/chromedriver/mobile-emulation)

**Another way to run using Chrome :**

* Create Chrome Profile on which mobile web tests will execute.
* Provide following property in application.properties file

```properties
chrome.additional.capabilities={"chromeOptions":{"args":["user-data-dir=D:/tools/webdriver/chromedriver_win32/mobile"]}}
```

{% include note.html content="Please update path of user-data-dir as per your chrome profile dir." %}
