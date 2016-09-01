---
title: appication.properties 
sidebar: faq_sidebar
permalink: qaf-2.1.7b/appication_properties.html
folder: qaf_2_1_7b
---

```properties

##################################################################

**appication.properties**

**this file should be located under ./resources directory**

**if not then you need to specify file by system property "application.properties.file"**

##################################################################

#provide base URL of application under test 

env.baseurl=http://www.google.com

env.resources=resources

resources.load.subdirs=1


#set appropriate teststep provider package

step.provider.pkg=com.sample.page

#[selenium]

remote.server=http://<username>:<access-key>@ondemand.saucelabs.com:80/wd/hub

remote.port=80

#you can override this property from config file by providing "browser" parameter

driver.name=firefoxRemoteDriver

#webdriver.remote.session=fd313470-4e25-4122-9009-5db2ae04c98d

selenium.wait.timeout=30000

#webdriver.ie.driver=F:/Downloads/selenium/IEDriverServer_Win32_2.22.0/IEDriverServer.exe

#webdriver.chrome.driver=F:/Downloads/selenium/chromedriver.exe

#selenium.screenshot.dir=test-results/img/

#selenium.report.dir=test-results/html/

#switch capturing screenshots on/off on checkpoint success 

selenium.success.screenshots=1

#wd.command.listeners=

#we.command.listeners=

retry.count=0

#set 1 to suppress success log, when 0 it will also show verification success message

report.log.skip.success=0

#[Test]

#DataDrivenTest.testdata=datafile=resources/data.csv

#step.provider.pkg=com.ispl.automation.sample.mock.step.brand1;com.ispl.automation.sample.mock.step.common;com.ispl.automation.sample.orbitz.web.page

#set 0 in case of parallel execution of test-case

selenium.singletone=1


#integration with rally 

#######################################################

**requires rally dependency jar's**

#######################################################

integration.tool.rally=0

#integration.param.rally.service.url=

#integration.param.rally.user=

#integration.param.rally.pwd=

#integration.param.rally.project=

#integration.param.rally.workspace=

#integration.param.rally.build=

#integration.tool.rally.testset=


#integration with QMetry

#######################################################

**requires QMetry dependency jar's**

#######################################################


integration.tool.qmetry=0

#integration.param.qmetry.service.url=

#integration.param.qmetry.user=

#integration.param.qmetry.pwd=


#integration.param.qmetry.project=

#integration.param.qmetry.release=

#integration.param.qmetry.build=

#integration.param.qmetry.suit.path=

#integration.param.qmetry.suit.rundesc=

license.key=<provide license key>

firefox.additional.capabilities = {\"version\":\"29\"}

```
