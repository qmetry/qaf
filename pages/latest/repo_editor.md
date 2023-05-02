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
 
 
## File types

 By default repo editor will list  `.wsc`,`.loc`,`.proto`,`properties`,`.locj`,`.wscj` files. It can be modified using `repoeditor.filetypes` property.
 
 ```
 repoeditor.filetypes=.wsc;.loc;.proto;properties;.locj;.wscj;.xml
 ```