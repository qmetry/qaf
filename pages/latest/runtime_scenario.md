---
title: Runtime Scenario
sidebar: qaf_latest-sidebar
permalink: latest/runtime_scenario.html
folder: latest
tags: [bdd,scenario,java]
summary: "QAF support BDD in pure java enables development team to follow behavior driven development approach for test automation implementation in best efficient way."

---

## BDD in Java

QAF supports BDD with scenario implementation in Java. Test developer don't need to relay on plain text bdd or feature file and still follow Behavior Driven Development. It will help in making BDD simpler and still will have all feature of QAF BDD without sacrificing any benefit of TestNG. Moreover, it will eliminate extra layer of feature files and certainly step definition files.
You can construct scenario inside your test method and run it. As test implementation in java, you can have full programming control and take all benefits of TestNG. Runtime Scenario supports all qaf bdd features listed below:
  - Meta data
  - reporting steps
  - reporting not-run steps, if test failed in between and remaining step not executed 
  - step listeners
  - <b>dryrun mode</b> same as running scenario from BDD plain text file.

Just like reading from BDD or Feature file, [RuntimeScenarioFactory](javadoc/com/qmetry/qaf/automation/step/client/RuntimeScenarioFactory.html) can be useful to create scenario during execution in Java code. It will look like: 

```java
 import static com.qmetry.qaf.automation.step.client.RuntimeScenarioFactory.scenario;
 

 @MetaData("{'TestCaseID':'TC-12345'}")
 @Test(description="a sample scenario", ...)
 public void testWithGivenWhenThen() {

        scenario().
        given("a precondition",()->{
                //write appropriate code...
        }).
        when("some action performed",()->{
                //write appropriate code...
        }).
        then("it should have expected outcome",()->{
                //write appropriate code...
        }).
        execute();
 }

```

## BDD Generator

When using Runtime Scenario, bdd plain text files or feature file or are not required to run your tests. In case, if you required to have feature file for any reason you can generate feature files using [Behavior Generator](bdd_generator.html).