---
title: Using running Driver Session
sidebar: qaf_2_1_11-sidebar
permalink: qaf-2.1.11/using_running_driver_session.html
folder: latest
---
One of the useful features of QAF, while testing the code, is using running webdriver session. Following are the steps:

1. Start selenium server.
2. Navigate to [http://localhost:4444/wd/hub/static/resource/hub.html](http://localhost:4444/wd/hub/static/resource/hub.html) in browser.
3. Create a new session.
{% include inline_image.html file="new_session.png" alt="Create New Session" %}
4. Select browser and click ok. It will create a new session for selected browser.
{% include inline_image.html file="new_session_started.png" alt="Session Id" %}
5. Set appropriate remote driver as browser and "webdriver.remote.session". For example in this case:

   ```properties
   driver.name=firefoxRemoteDriver
   webdriver.remote.session=c21c48ce-3256-47a7-aa86-f3476ffa0060
   ```

6. Navigate to the page manually in the browser.
7. Start executing code snippet for test on that page. For example in following case I navigated to the page and opened calendar to test calendar component by following test code.

   ```java
   @Test
   void test1() {
       WebCalendar calendar = new WebCalendar(WebHomePageLocators.CALENDAR_COMPONENT_LOC);
       calendar.setDates(DateUtil.getDate(25), DateUtil.getDate(30));
   }
   ```

8. I can run/debug above snippet as testng test to test different aspects or to reproduce any defective behavior.
{% include inline_image.html file="remote_debugging.png" alt="Remote Debugging" %}
    
   
