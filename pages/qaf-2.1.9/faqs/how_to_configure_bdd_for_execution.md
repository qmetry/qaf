---
title: How to configure BDD for execution
sidebar: faq_sidebar
permalink: qaf-2.1.9/how_to_configure_bdd_for_execution.html
folder: qaf-2.1.9
---

**Please refer xml configuration examples provided here.**

**Example-1**

```xmls

<?xml version="1.0"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">
<parameter name="driver.name" value="firefoxDriver" />
       <test name="Sample BDD Test" enabled="true">
               <classes>
                         <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory"/>
               </classes>
       </test>
</suite>

```

Above configuration will execute all the bdd test in scenario directory and all its subdirectory

**Example-2: specifying file(s) location**

You can specify bdd file location by using scenario.file.loc parameter. The default value is “scenarios”.

The scenario.file.loc parameter value can be a file or directory from which you want to configure test for execution. It can also hold multiple values separated by semicolon (;) for example:

```xml

<parameter name="scenario.file.loc" value="scenarios/testsuite1.bdd; scenarios/testsuite2.bdd "/>

```

**OR**

```xml

<parameter name="scenario.file.loc" value="scenarios/testsuite1.bdd; scenarios/module1; scenarios/module2"/>

```

```xml


<?xml version="1.0"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">


<parameter name="driver.name" value="firefoxDriver" />
          <test name="Sample BDD Test" enabled="true">

               <parameter name="scenario.file.loc" value="scenarios/testsuite.bdd"/>
              
               <classes>
                         <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory"/>
               </classes>
       </test>
</suite>

```

Above configuration will execute only testsuite.bdd .

**Example-3: specifying file(s) location**

```xml

<?xml version="1.0"?>

<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">
<parameter name="driver.name" value="firefoxDriver" />
       <test name="Sample BDD Test" enabled="true">
               <parameter name="scenario.file.loc" value=" scenarios/testsuite1.bdd; scenarios/module1; scenarios/module2"/>
              
               <classes>
                      <class
                          name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory"/>
               </classes>
       </test>
</suite>

```
 

Above configuration will execute testsuite.bdd and all .bdd file(s) in module1 directory and its sub-directory and all .bdd file(s) of module2 directory and its sub directory.

**Example-4 specifying groups**

```xml

<?xml version="1.0"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite verbose="0" name="QAF Demo">
<parameter name="driver.name" value="firefoxDriver" />
       <test name="Sample BDD Test" enabled="true">
               <groups>
                         <run>
                               <include name="SMOKE"/>
                         </run>
               </groups>
               <classes>
                     <class
                          name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory"/>
               </classes>
       </test>
</suite>

```

Above configuration will execute scenarios with group SMOKE. You can use include and exclude as per your requirement.
