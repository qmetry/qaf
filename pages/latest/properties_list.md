---
title: Property List
sidebar: qaf_latest-sidebar
permalink: latest/properties_list.html
folder: latest
tags: [getting_started]
---

| Key | Default Value | Description | 
|-------|--------|---------|
| env.baseurl |	| Base URL of web application under test
| env.resources | resources |	List of files or folder from that you want to load resources like testdata, locators and properties.
| step.provider.pkg | |	Provide a list of packages to load teststep, required only for step client other than java.
| scenario.file.loc | scenarios | list of file/folder from where bdd/kwd scenarios need to be executed
| remote.server | localhost |	If you are using remote driver, specify server.
| remote.port |	4444 | If you are using remote driver, specify port on which selenium server is running.
| driver.name	| | Name of driver to create instance of that driver. Available Possible Values.<br> firefoxDriver, iExplorerDriver, chromeDriver, operaDriver, androidDriver, iPhoneDriver, appiumDriver, <br/><b>otherDriver</b> : To use custom driver, provide driver class as capability. <br/> **Example**:<br/>To use PhantomJSDriver<br/><br/> driver.name=otherDriver <br/> other.additional.capabilities={'driverClass':'org.openqa.selenium.phantomjs.PhantomJSDriver'}
| webdriver.remote.session | | selenium remote session id for debugging purpose.
| selenium.wait.timeout	| 30000	| Default timeout in ms for all the element related commands and for waitservices methods.
| webdriver.ie.driver	| |	Sepcify IEDriverServer file path here. Required when using iExplorerDriver
| webdriver.chrome.driver | |	Sepcify chromedriver file path here. Required when using chromeDriver 
| selenium.success.screenshots | 1 |	Specify whether to capture screenshot or not on checkpoint success. Possible values 0 or 1, true or false.
| selenium.failure.screenshots | 1 |	Specify whether to capture screenshot or not on checkpoint failure. Possible values 0 or 1, true or false.
| wd.command.listeners | | list of qualified class name that implements [WebDriver Listener](qaf_listeners.html). From 2.1.9 You can use `qaf.listeners` as well instead of this property to register webdriver command listener.
| we.command.listeners	 | | list of qualified class name that implements [WebElement Listener](qaf_listeners.html). From 2.1.9 You can use `qaf.listeners` as well instead of this property to register webelement command listener.
| teststep.listeners | | list of qualified class name that implements [TestStep Listener](qaf_listeners.html). From 2.1.9 You can use `qaf.listeners` as well instead of this property to register teststep listener.
| qaf.listeners | | list of qualified class name that implements any of the QAF listener . Single property that can be used to registe any of the qaf listener (webdriver, webelement to teststep). (:new:since 2.1.9)
| retry.count	| 0	| To retry testcase if testcase failed cause of any exception.<br> **Note**: testcase will not retry if there is any checkpoint failure.
| bean.populate.random | 	false	| Set true to fill bean randomly from available data sets, more details [fill Databean from multiple dataset.](databeans.html)
| selenium.singletone | |	To define driver instance scope.Possible value can be Tests or Methods or Groups.
|driver.init.retry.timeout|0|Duration in multiplication of 10 seconds for example 50. Set time out for retry driver initialization when driver initialization fail (:new:since 2.1.9).
|step.provider.sharedinstance|false|specify wherether to share class object among step in the same class. ( :new: sice 2.1.9)

