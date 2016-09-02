---
title: Property List
sidebar: qaf_2_1_8-sidebar
permalink: qaf-2.1.8/properties_list.html
folder: qaf-2.1.8
tags: [getting_started]
---

| Key | Default Value | Description | 
|-------|--------|---------|
| env.baseurl |	| Base URL of application under test
| env.resources | resources |	List of files or folder from that you want to load resources like testdata, locators and properties.
| step.provider.pkg | |	Provide a list of packages to load teststep.
| remote.server | localhost |	If you are using remote driver, specify server.
| remote.port |	4444 | If you are using remote driver, specify port on which selenium server is running.
| driver.name	| | Name of driver to create instance of that driver. Available Possible Values.<br> firefoxDriver, iExplorerDriver, chromeDriver, operaDriver, androidDriver, iPhoneDriver, appiumDriver, <br/><b>otherDriver</b> : To use custom driver, provide driver class as capability. <br/> **Example**:<br/>To use PhantomJSDriver<br/><br/> driver.name=otherDriver <br/> other.additional.capabilities={'driverClass':'org.openqa.selenium.phantomjs.PhantomJSDriver'}
| webdriver.remote.session | | selenium remote session id for debugging purpose.
| selenium.wait.timeout	| 30000	| Default timeout in ms for all the element related commands and for waitservices methods.
| webdriver.ie.driver	| |	If driver.name = iExplorerDriver then sepcify IEDriverServer file path here.
| webdriver.chrome.driver | |	If driver.name = chromeDriver then sepcify chromedriver file path here.
| selenium.success.screenshots | | 1	Capturing screenshot if checkpoint is success.
| wd.command.listeners | | [WebDriver Listener](/qaf/qaf_listeners.html)
| we.command.listeners	 | | [WebElement Listener](/qaf/qaf_listeners.html)
| teststep.listeners | | [TestStep Listener](/qaf/qaf_listeners.html)
| retry.count	| 0	| To retry testcase if testcase failed cause of any exception.<br> **Note**: testcase will not retry if there is any checkpoint failure.
| bean.populate.random | 	false	| Set true to fill bean randomly from available data sets, more details [fill Databean from multiple dataset.](databeans.html)
| selenium.singletone | |	To create testbase instance singleton scope.Possible value can be Tests or Methods or Groups.

