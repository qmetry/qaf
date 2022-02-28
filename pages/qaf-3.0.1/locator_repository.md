---
title: Locator Repository
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/locator_repository.html
folder: qaf-3.0.1
tags: [Selenium,locator,java, webdriver, webelement]
---

QMetry Automation Framework provides concept called **“Locator Repository”** which can be used to abstract your element locator outside the code. Moreover, you can have separate locator repositories per environment/platform and you can configure at runtime to load environment specific locator repository. 

## Creating Locator repository
You can create Locator repository with property file having extension ‘. property’ or ‘.loc’. In repository provide key-value pair of **locator-key** and **locator-value**. While developing test assets instead of providing locator you can use the **locator-key**. To provide locator-value refer [how to locating element](locating_elements.html). 

## Example ##

**loginscreen.loc**

```properties
  login.username.txt = name=uname
  login.password.txt = name=upwd
  login.pageheader.lbl = css=.header
  ...
  
  #self-descriptive locator example
  login.username.txt = {'locator':'name=uname','desc':'User name texbox on Login Page'}
  login.password.txt = {'locator':'name=upwd','desc':'Password texbox on Login Page'}
  login.pageheader.lbl = {'locator':'css=.header','desc':'Header of Login Page'}
  ...
```

## Usage
you can refer locator-key in your java code for creating a web-element for example:

**Java Code:**

```java
  QAFWebElement txtUserName = new QAFExtendedWebElement("login.username.txt");
```

Page Class:

```java
  @FindBy(locator = "login.username.txt")
  private QAFWebElement txtUserName;
```

**BDD**

While using in-built step you can provide locator-key to refer any locator.

```javaScript
  assert 'login.username.txt' is present
  sendKeys 'myusername' into 'login.username.txt'
```

