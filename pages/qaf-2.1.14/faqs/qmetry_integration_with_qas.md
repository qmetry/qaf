---
title: Qmetry6 Test Management Tool Integration with Qmetry Automation framework
sidebar: faq_sidebar
permalink: qaf-2.1.14/qmetry_integration_with_qas.html
folder: qaf-2.1.14
---


QMetry Automation Studio Multi-Property Editor

## How to integrate QMetry6 Test Management Tool?

QMetry 6 integration require necessary properties which should be provided in application properties.

Following properties must be available in application properties.

integration.param.qmetry.service.url=<QMetry service URL>

integration.param.qmetry.user=<username>

integration.param.qmetry.pwd=<password>

integration.param.qmetry.project=<project_id>

integration.param.qmetry.release=<release_id>

integration.param.qmetry.cycle=<cycle_id>

integration.param.qmetry.build=<build_id>

integration.param.qmetry.platform=<platform_id>

integration.param.qmetry.drop=<drop_id>

integration.param.qmetry.suitid=<suite_id>

or

integration.param.qmetry.suitrunid=<suite_run_id>

There are four possibility to integrate test case.

**Scenario 1:**

If **suite_id** and **TC_ID** are known then provide suite_id in application.properties.

For BDD:

Provide **TC_ID** by using tc_id. For Example,

```

SCENARIO: Sample
META-DATA: {"tc_id":"", "description":"Sample Description", "groups":["SMOKE"]}
       #write content here
END

```

For JAVA:

```java	

@QmetryTestCase(TC_ID = "your test case id")
public void sample() {
}

```

**Scenario 2:**

If **suit_run_id** and **TC_ID** are known then provide suite_id in application.properties.

integration.param.qmetry.suitid=<suite_id>

For BDD:

Provide **TC_ID** by using tc_id. For Example,

```

SCENARIO: Sample
META-DATA: {"tc_id":"", "description":"Sample Description", "groups":["SMOKE"]}
       #write content here
END

```

For JAVA:

```java

@QmetryTestCase(TC_ID = "your test case id")
public void sample() {
}

```

**Scenario 3:**


If **suit_id** and **TC_RUN_ID** are known then provide suite_id in application.properties.

For BDD:

Provide **TC_RUN_ID** by using runId. For Example,

```

SCENARIO: Sample
META-DATA: {"runId":"", "description":"Sample Description", "groups":["SMOKE"]}

       #write content here
	   
END

```

For JAVA:

```java

@QmetryTestCase(runId = "your test case run id")
public void sample() {
}

```

**Scenario 4:**

If suit_run_id and TC_RUN_ID are known then provide suite_id in application.properties.

integration.param.qmetry.suitid=<suite_id>

For BDD:

Provide **TC_RUN_ID** by using runId. For Example,

```

SCENARIO: Sample
META-DATA: {"runId":"", "description":"Sample Description", "groups":["SMOKE"]}
       #write content here
END

```


For JAVA:

```java

@QmetryTestCase(runId = "your test case run id")
public void sample() {
}

```

