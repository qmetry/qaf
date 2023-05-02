---
title: Sauce on demand Integration
sidebar: faq_sidebar
permalink: qaf-3.1.0/sauce_on_demand_integration.html
folder: qaf-3.1.0
---

To run test on cloud with Qmetry Automation Framework, all you need to change is the server, port and browser info provided by sauce labs. You can run your test in one or more environments without having your own infrastructure. The most powerful feature of QMetry Automation Framework is that it provides parallel-ready test harness to connect tests to Sauce Labs’ service.

**Follow below steps to configure**

go to souce lab web site and sign up. **[sign up here](https://saucelabs.com/signup)**
Souce lab provide you a username and Access Key.
Open Application.properties file and add Desired Capabilities mentioned in table.

remote.server=**"http://username:AccessKey@ondemand.saucelabs.com:PortNumber/wd/hub"**

You can specify extra capabilities for the driver you are using as follows

**driver.additional.capabilities={json_key_value_pair}**

i.e. for firefoxDriver or firefoxRemoteDriver this would be

```properties

firefox.additional.capabilities={"version":"28","platform":"windows"}

```

Now you can run your test case and it will execute on souce Lab VM.
You can view the execution on Dashboard of Sauce Lab.

