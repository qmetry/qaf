---
title: QAF Repository Editor
sidebar: qaf_latest-sidebar
permalink: latest/repo_editor.html
folder: latest
tags: [locator, webservice, getting_started]
---

With 3.2.0, QAF provides web based editor for locator and web services repository. It allows to create, edit and execute request call from UI.
QAF repository editor can be used as API testing tool with automation first approach. It also allows to manually reproduce or verify the bug in web-services which found through automation.

## Features

Following are high level features:
 - Create, edit, delete, rename, locator repository, request call repository, properties file
 - Execute any available test steps
 - Import open-api specification
 - Import postman collection
 - Request call editor with manual execution ability
 - GRPC proto viewer with manual execution ability
 - Convert locator repository from `.loc` to `.locj` and wise-versa
 - Convert request call repository from `.wsc` to `.wscj` and wise-versa
 - View report dashboard in any browser
 
## Usage

Repository editor can be used with or without automation project. 

 - Setup:
	 - If you have existing automation project with QAF make sure you are using qaf 3.2.0 or later version. Also make suer you have latest updates from [qaf-report](https://github.com/infostretch/qaf-report). 
	 - If you don't have existing automation project and you just want to use repository editor for web services manual testing download and extract [qaf-report](https://github.com/infostretch/qaf-report/archive/refs/heads/master.zip)
 - Run server:
 	- go to command prompt and navigate to the project directory (or where you extracted qaf-report.zip)
 	- execute `mvn -f repo-editor-runner.xml exec:java`
 - Once server started, Open http://localhost:2612/repo-editor in browser.
	 
 
Following end-points available with repository editor server:

   - /repo-editor
   - /browse
   - /

 <img width="1439" alt="Screen Shot 2022-10-02 at 11 51 39 PM" src="https://user-images.githubusercontent.com/110619/193645905-411e8c90-782a-40d7-9384-84c4f9797c32.png">
 
 
## File types

 By default repo editor will list  `.wsc`,`.loc`,`.proto`,`properties`,`.locj`,`.wscj` files. It can be modified using `repoeditor.filetypes` property.
 
 ```
 repoeditor.filetypes=.wsc;.loc;.proto;properties;.locj;.wscj;.xml
 ```