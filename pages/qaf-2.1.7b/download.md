---
title: Download
sidebar: download_sidebar
permalink: qaf-2.1.7b/download.html
folder: qaf-2.1.7b
---

## Blank Project

If you are starting form scratch, please refer <a href="create_test_project.html">Creating Test Project</a> under Get started.

## Maven

You can use QAF as a Maven Artifact. Users would need to add this to their pom.xml:

**Repository entry:**

```xml
<repository>
    <id>qaf</id>
    <url>{{site.data.strings.qaf_repository}}</url>
</repository>
```

**Maven Dependency Entry:**

```xml
<dependency>
    <groupId>com.qmetry</groupId>
    <artifactId>qaf</artifactId>
    <version>2.1.7b</version>
</dependency>
<dependency>
    <groupId>com.qmetry</groupId>
    <artifactId>qaf-support</artifactId>
    <version>2.1.7b</version>
</dependency>
```

## IVY

Create or update ivysettings.xml file to add new repository. Alternately you can add settings block into ivy.xml as well.

**IVY settings**

```xml
<?xml version="1.0" encoding="ISO-8859-1"?>
<ivysettings>
    <settings defaultResolver="qaf"/>
    <resolvers>
        <chain name="qaf">
            <ibiblio name="central" m2compatible="true"/>
            <ibiblio name="QAF" m2compatible="true" root="{{site.data.strings.qaf_repository}}" />
        </chain>
    </resolvers>
</ivysettings>
```

**IVY Dependency Entry**

```xml
<dependency org="com.qmetry" name="qaf" rev="2.1.7b" />
<dependency org="com.qmetry" name="qaf-support" rev="2.1.7b" />
```

## Direct Download

You can download QAF jar from [here]({{site.data.strings.qaf_repository}}/com/infostretch/qaf/2.1.7b/qaf-2.1.7b.jar).
