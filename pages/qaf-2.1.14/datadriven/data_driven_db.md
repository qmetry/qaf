---
title: Database Data Provider
sidebar: qaf_2_1_14-sidebar
permalink: qaf-2.1.14/database.html
tags: [datadriven]
folder: qaf-2.1.14
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


