---
title: Database Data Provider
sidebar: qaf_3_0_0-sidebar
permalink: qaf-3.0.0/database.html
tags: [datadriven]
folder: qaf-3.0.0
prev: maketest_data_driven.html
---

Below are the properties which required to configure in property file to configure database.

```
db.driver.class=com.mysql.jdbc.Driver
db.connection.url=jdbc:mysql://localhost:3306/db
db.user=username
db.pwd=password

```

## Usage
### Java
```java
@QAFDataProvider(sqlQuery="select username,password,isvalid from login_table")

```
### BDD
```
Meta-data:{"sqlQuery":"select username,password,isvalid from login_table"}
```

### BDD2
```
@sqlQuery:select username,password,isvalid from login_table

```
Or

```
Examples: {"sqlQuery":"select username,password,isvalid from login_table"}

```

### Gherkin
```
Examples: {"sqlQuery":"select username,password,isvalid from login_table"}

```


