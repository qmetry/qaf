---
title: How can I Use Selenium grid with Qmetry Automation Framework?
sidebar: faq_sidebar
permalink: latest/how_can_i_use_selenium_grid_with_qas.html
folder: latest
---

You need to set appropriate server, port and remote driver.

**For example** you can set following properties in application property file.

```properties

remote.server=<ip or machine name>
remote.port=<port used by grid/server normally 4444>
driver.name=firefoxRemoteDriver

```

The same can be provided either by command line as system property or in testNG configuration file as parameter.

