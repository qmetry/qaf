---
title: Download
sidebar: download_sidebar
permalink: latest/download.html
folder: latest
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
<repository>
	<id>jai</id>
	<url>https://repository.jboss.org/nexus/content/repositories/thirdparty-releases</url>
</repository>
```

**Maven Dependency Entry:**

```xml
<dependency>
    <groupId>com.qmetry</groupId>
    <artifactId>qaf</artifactId>
    <version>latest.integration</version>
</dependency>
<dependency>
    <groupId>com.qmetry</groupId>
    <artifactId>qaf-support</artifactId>
    <version>latest.integration</version>
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
            <ibiblio name="jai" m2compatible="true"
				root="https://repository.jboss.org/nexus/content/repositories/thirdparty-releases" />
        </chain>
    </resolvers>
</ivysettings>
```

**IVY Dependency Entry**

```xml
<dependency org="com.qmetry" name="qaf" rev="latest.integration" />
<dependency org="com.qmetry" name="qaf-support" rev="latest.integration" />
```

## Direct Download

You can download QAF jar from [here](https://qmetry.github.io/qaf/dist).

You can download QAF Support jar from [here](https://qmetry.github.io/qaf/dist).