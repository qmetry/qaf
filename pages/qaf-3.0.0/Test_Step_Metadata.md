---
title: Test Step Metadata
sidebar: qaf_3_0_0-sidebar
permalink: qaf-3.0.0/Test_Step_Metadata.html
folder: qaf-3.0.0
tags: [bdd,scenario,java,kwd]
---

Supports for Custom Meta-Data at test step level.

Example: Here, groups is a custom meta-data for step.
 
<b>JAVA</b>

```
@MetaData(value = "{'groups':['login']}")
@QAFTestStep(description = "user login with username {username} and password {password}")
public void login(String username,String password) {
 
      //To-do
}
```

<b>BDD STEP DEF</b>

```
STEP-DEF: user login with username {username} and password {password}
META-DATA: {'groups':['login']}
 
    #To-do 
 
END
```

<b>KWD STEP DEF</b>

```
STEP-DEF|login|{"description":"user logins with {username} and {password}","groups":["login"]}
...
<steps>
...
END||
```
 
 
