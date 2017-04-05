---
title: How can I provide desired capabilities when using Qmetry Automation Framework?
sidebar: faq_sidebar
permalink: qaf-2.1.10/how_can_i_provide_desired_capabilities_when_using_qas.html
folder: qaf-2.1.9
---

For all driver set property 

```properties

driver.additional.capabilities={<capabilityname1>=<value1>,<capabilityname2>=<value2>}

```

For specific driver set property 

```properties

<driver>.additional.capabilities={<capabilityname1>=<value1>,<capabilityname2>=<value2>}

```

**Example:**

```properties
chrome.additional.capabilities={"chromeOptions":{"mobileEmulation":{"deviceName":"Apple iPhone 5"}}}
```

