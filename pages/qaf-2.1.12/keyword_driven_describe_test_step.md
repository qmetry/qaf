---
title: Keyword driven describe test step
sidebar: qaf_2_1_12-sidebar
permalink: qaf-2.1.12/keyword_driven_describe_test_step.html
folder: latest
---

Test step can be defined in .java or .kwl files. It consists of stepname, description and step metadata.

## Define Step In KWL

```	
STEP-DEF|<stepName>|{"description":"<meaningfull step description>",<meta-key>:<meta-value>}
<first stepName>|<step input parameters>|<step out parameters>
...
<nth stepName>|<step input parameters>|<step out parameters>
END||
```

To define step, which contains parameters:

Example: Login Step can be defined in KWL by

Example: KWD Step

```	
STEP-DEF|login|{"description":"user logins with {0} and {1}",<meta-key>:<meta-value>}
  
<first stepName>|<step input parameters>|<step out parameters>
...
<nth stepName>|<step input parameters>|<step out parameters>
 
END||
```
 
This step can be consumed from *.kwd file as

## Consume KWD Step

```	
login|["username","password"]|  
```

**Using/Passing Argument of Step**

Test Step Declaration In java 

## Java Step Def

```java	
@QAFTestStep(description = "user logins with {username} and {password}")
public void login(String username, String password) {
// step implementation
}
```

Test Step Declaration In KWL

## KWD Step Def

```	
STEP-DEF|login|{"description":"user logins with {username} and {password}"}
comment|["${args[0]}"]|
comment|["${args[1]}"]|
comment|["${username}"]|
comment|["${password}"]|
END||
```

Can be consumed as

## Consume Step in KWD

```	
login|["username","password"]|  
```
