---
title: How to configure KWD for execution
sidebar: faq_sidebar
permalink: qaf-2.1.15/how_to_configure_kwd_for_execution.html
folder: qaf-2.1.15
---


**Please refer xml configuration examples provided here.**

**Example-1**

```xml

<?xml version="1.0"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">
<parameter name="driver.name" value="firefoxDriver" />
    <test name="Sample KWD Test" enabled="true">
       <classes>
          <class name="com.qmetry.qaf.automation.step.client.text.KWDTestFactory"/>
       </classes>
    </test>
</suite>

```

Above configuration will execute all the kwd test in scenario directory and all its subdirectory

**Example-2: specifying file(s) location**

You can specify kwd file location by using scenario.file.loc parameter. The default value is “scenarios”.

The scenario.file.loc parameter value can be a file or directory from which you want to configure test for execution. It can also hold multiple values separated by semicolon (;) for example:

```xml

<parameter name="scenario.file.loc" value="scenarios/testsuite1.kwd; scenarios/testsuite2.kwd "/>

```


```xml


<?xml version="1.0"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">
<parameter name="scenario.file.loc" value="scenarios/testsuite1.kwd; scenarios/module1; scenarios/module2"/>

<parameter name="driver.name" value="firefoxDriver" />
    <test name="Sample KWD Test" enabled="true">
       <parameter name="scenario.file.loc" value="scenarios/testsuite.kwd"/>
           <classes>
              <class name="com.qmetry.qaf.automation.step.client.text.KWDTestFactory"/>
           </classes>
    </test>
</suite>

```

Above configuration will execute only testsuite.kwd .

**Example-3: specifying file(s) location**

You can specify kwd file location by using scenario.file.loc parameter. The default value is “scenarios”. You can provide “scenario.file.loc” either in properties file or in xml configuration file or from command line.

Value for scenario.file.loc can be file or directory (or combination of both) from which you want to configure test for execution. It can hold multiple values separated by semicolon (;).

```xml

<parameter name="scenario.file.loc" value="scenarios/testsuite1.kwd; scenarios/testsuite2.kwd "/>

```

**OR**

```xml

<parameter name="scenario.file.loc" value="scenarios/testsuite1.kwd; scenarios/module1; scenarios/module2"/>

```

```xml

<?xml version="1.0"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">
<parameter name="driver.name" value="firefoxDriver" />
   <test name="Sample KWD Test" enabled="true">
       <parameter name="scenario.file.loc" value=" scenarios/testsuite1.kwd; scenarios/module1; scenarios/module2"/>
        <classes>
            <class name="com.qmetry.qaf.automation.step.client.text.KWDTestFactory"/>
        </classes>
   </test>
</suite>

```

Above configuration will execute testsuite.kwd and all .kwd file(s) in module1 directory and its sub-directory and all .kwd file(s) of module2 directory and its sub directory.

**Example-4 specifying groups**

```xml

<?xml version="1.0"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">
<parameter name="driver.name" value="firefoxDriver" />
       <test name="Sample KWD Test" enabled="true">
               <groups>
                         <run>
                               <include name="SMOKE"/>
                         </run>
               </groups>
               <classes>
                         <class name="com.qmetry.qaf.automation.step.client.text.KWDTestFactory"/>
               </classes>
       </test>
</suite>

```

Above configuration will execute scenarios with group SMOKE. You can use include and exclude as per your requirement.
