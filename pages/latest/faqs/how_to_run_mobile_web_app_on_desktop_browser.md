---
title: How to run mobile web app on desktop browser?
sidebar: faq_sidebar
permalink: qaf-2.1.9/how_to_run_mobile_web_app_on_desktop_browser.html
folder: qaf-2.1.9
---

You can run your mobile web app on Firefox as well as on chrome with modify header.


## Using Firefox:

1. First close all of the Firefox instances then create new Firefox profile. Profile can be created by following [Use the Profile Manager to create and remove Firefox profiles](https://support.mozilla.org/en-US/kb/profile-manager-create-and-remove-firefox-profiles#w_starting-the-profile-manager)
2. Install [Modify Header](https://addons.mozilla.org/en-US/firefox/addon/modify-headers/)
3. Add user agent string as follows

	* In Firefox, click on the Tools menu, and choose Modify Headers. The Modify Headers dialog appears (it isn’t the most intuitive interface):

	* In the drop-down list at the top-left of the screen, choose Add

	* In the first text box, type 'User agent Name'

	* In the second box, paste in the User Agent Profile URL for the desired mobile device. UA strings can be found [here](http://www.useragentstring.com/pages/useragentstring.php)

	* Click the Add button

4. Now turn on the Modify Headers and Test whether it works by navigating to any mobile website.
5. Close the browser
6. To use this profile in automation with QAF, provide <b>webdriver.firefox.profile=&lt;PROFILE_NAME&gt;</b> in application.properties file.
7. Provide sysproperty in testng task of runtests target in scripts/seleniumtestrunner.xml file as follows.

**sysproperty key="webdriver.firefox.profile" value="PROFILE_NAME" /**

## Using Chrome:

1.Provide following property in application.properties file

```properties

chrome.additional.capabilities={"chromeOptions":{"mobileEmulation":{"deviceName":"Google Nexus 5"}}}
webdriver.chrome.driver = D:/chromedriver.exe
driver.name=chromeDriver
```	

2.Follow the link for specifying a device from the [DevTools Emulation panel](https://developer.chrome.com/devtools/docs/mobile-emulation) as the value for “deviceName". [https://sites.google.com/a/chromium.org/chromedriver/mobile-emulation](https://sites.google.com/a/chromium.org/chromedriver/mobile-emulation)


**Another way to run using Chrome :**

Create Chrome Profile on which mobile web tests will execute.

Provide following property in application.properties file


```properties
chrome.additional.capabilities={"chromeOptions":{"args":["user-data-dir=D:/tools/webdriver/chromedriver_win32/mobile"]}}
```		

{% include note.html content="Please update path of user-data-dir as per your chrome profile dir." %}

