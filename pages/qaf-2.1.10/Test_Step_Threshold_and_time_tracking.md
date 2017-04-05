---
title: Test Step Threshold and time tracking
sidebar: qaf_2_1_10-sidebar
permalink: qaf-2.1.10/Test_Step_Threshold_and_time_tracking.html
folder: qaf-2.1.9
tags: [bdd,scenario,java,kwd]
---

## Test Step Threshold
In Built ‘threshold’ Test Step Meta-Data Support.
Example: Login Step should be executed in 10s, if takes more than 10s then it displays as warning in report.
User can specify threshold value by following way,
 
<b>Java Step</b>

```
@QAFTestStep(description = "user logins with username {username} and password {password}", threshold = 10)
public void userLogin(String username,String password){
      //To-do
}
```

<b>BDD STEP DEF</b>

```
STEP-DEF: user logins with username {username} and password {password}
META-DATA: {'threshold':10}
 
    #To-do 
 
END
```

<b>KWD STEP DEF</b>

```
STEP-DEF|login|{"description":"user logins with {username} and {password}","threshold":10}
...
<steps>
...
END|[]|
``` 

## In Built Steps for Time-Tracking

To track time of multiple consultative step, inbuilt steps provided.

<b>Start Tracking :</b> 

```
start (transaction|time-tracker) for {task-name} with {second}s threshold
```

<b>Stop Tracking :</b>

``` 
stop transaction
``` 

Example:

BDD

```
For Example:
 
start transaction for 'Login' with 10s threshold
 
      When user navigates to signin screen
 
      Then verify user should be on login page
 
      And user logins with random valid credentials
 
      Then validate user should be logged in
 
stop transaction
```

KWD

```
startTransaction|["Login","10"]|
...
<steps>
..
stopTransaction|[]|
```
