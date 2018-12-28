---
title: How can I use existing driver session?
sidebar: faq_sidebar
permalink: qaf-2.1.13/how_can_i_use_existing_driver_session.html
folder: qaf-2.1.13
---

**Following are the steps:**

1. Webdriver session can be created through [http://localhost:4444/wd/hub/static/resource/hub.html](http://localhost:4444/wd/hub/static/resource/hub.html). After starting session you can perform manual steps on the browser!

2. You need to use only appropriate remote driver for this purpose. (e.g. firefoxRemoteDriver)

3. You need to provide session id by setting a property “webdriver.remote.session”. The session id can be found at [http://localhost:4444/wd/hub/static/resource/hub.html](http://localhost:4444/wd/hub/static/resource/hub.html) from where you had created session.
