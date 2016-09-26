---
title: Locator Repository
sidebar: qaf_latest-sidebar
permalink: latest/locator_repository.html
folder: latest
tags: [Selenium,locator,java, webdriver, webelement]
---

QMetry Automation Framework provides concept called **“Locator Repository”** which can be used to abstract your element locator outside the code. Moreover, you can have separate locator repositories per environment/platform and you can configure at runtime to load environment specific locator repository. 

## Creating Locator repository
You can create Locator repository with property file with extension ‘. property’ or ‘.loc’. In repository you can provide key-value pair of locator-key and locator-value. While developing test assets insted of providing locator you can use the locator-key. to Provide locator value refer [how to locating element](locating_elements.html)

## Usage

**loginscreen.loc**

```properties
  login.username.txt = name=uname
  login.password.txt = name=upwd
  login.pageheader.lbl = css=.header
  ...
  
  #self-descriptive example
  login.username.txt = {'locator':'name=uname'; 'desc':'User name texbox on Login Page'}
  login.password.txt = {'locator':'name=upwd'; 'desc':'Password texbox on Login Page'}
  login.pageheader.lbl = {'locator':'css=.header'; 'desc':'Header of Login Page'}
  ...
```
**Java Code:**

  QAFWebElement dateOfBirhth = new QAFExtendedWebElement("login.username.txt");

Page Class:

```java
  @FindBy(locator = "login.username.txt")
  private QAFWebElement txtUserName;
```

**BDD**

```javaScript
  assert 'login.username.txt' is present
  sendKeys 'myusername' into 'login.username.txt'
```

