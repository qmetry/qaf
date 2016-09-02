---
title: Describe Test Step
sidebar: qaf_2_1_7b_sidebar
permalink: qaf-2.1.7b/Describe_Test_Step.html
folder: qaf-2.1.7b
tags: [bdd,scenario,bdl,java]
---



Test step can be defined in .java or .bdl files. It consists of description, with which it is known.

## Step in Java

```java
@QAFTestStep(description="meaning full step descriptor")
public <return_type> stepMethod(<parameters>) {
//step implementation
}
```

## Step in BDL
```
STEP-DEF:<meaningfull step description>
META-DATA:<valid json object>
[Keyword] <first step description with parameters>
…
[Keyword] <nth step description>
END
```
  
Regular expressions are supported for <b>description</b> of test step

To define step, which contains parameters:

```
user logins with {0} and {1}
user logins with {0:string} and {1:string}
user (logins|signins) with {0} and {1}
user (log|sign)ins with {0} and {1}
This step can be consumed from *.bdd file as:
user logins with 'username' and 'password'
user logins with 'username' and 'password'
```
  
More examples for defining step and consuming them:

### Using method argument name instead of numbers for parameters in step description

Test Step Declaration In java 
 
```java
@QAFTestStep(description = "user logins with {username} and {password}")
public void login(String username, String password) {
	// step implementation
}
```

Test Step Declaration In BDL 

```
STEP-DEF:user logins with {0} and {1}
Given COMMENT: '${args[0]}'
Given COMMENT: '${args[1]}'
// step implementation
END
``` 

As well as

```
STEP-DEF: Login
Meta-Data: user (log|sign)ins with {0:map}
Given COMMENT: '${args[0]}'
END
``` 

Can be consumed as:

```cucumber
Given user logins with 'username' and 'password123#'
```

### Using regular expression for flexibility in using the step

Test Step Declaration In java 
 
```java
@QAFTestStep(description = "user (log|sign)ins with {username} and {password}")
public void login(String username, String password) {
	// step implementation
}
```

Test Step Declaration In BDL
 
```
STEP-DEF:user (log|sign)ins with {0} and {1}
Given COMMENT: '${args[0]}'
Given COMMENT: '${args[1]}'
// step implementation
END
``` 

Can be consumed as: 

```cucumber
Given user logins with 'username' and 'password123#' 
Given user signins with 'username' and 'password123#'
```

### Using/Passing map structured data in argument of step

Test Step Declaration In java
 
```java
@QAFTestStep(description = "user logins with {credentials}")
public void login(Map<String, String> creds) {
	// step implementation
}
```
Test Step Declaration In BDL
 
```
STEP-DEF:user logins with {0}
Given COMMENT: '${args[0]}'
END
```
 
Can be consumed as: 

```
Given user logins with {'username':'user123','password':'password123#'}
```

### Using/Passing Array data in argument of step

Test Step Declaration In java

```java
@QAFTestStep(description = "user should have {access_types} access")
public void login(String[] access_types) {
	// step implementation
}
```

Test Step Declaration In BDL
 
```
STEP-DEF:user should have {0} access
Given COMMENT: '${args[0]}'
END
```
 
Can be consumed as: 

```cucumber
Given user should have ['read','write','modify'] access
```
