---
title: Test Data
sidebar: qaf_2_1_8_sidebar
permalink: qaf-2.1.8/testdata.html
folder: qaf-2.1.8
tags: [scenario,test data]
---

{% include inline_image.html
file="worddavbc574dd694d63ef066aa760b1d31af80.png" alt="Test Data Diagram" %}

## Properties:

You can create properties file as per your requirement under resources dir. Properties will be available in your test case and test page. You can directly access by **props/pageProps** object available at test/page level.

Example: 

String someprop = props.getString("some.property");

## Context

A context object is available in every class that extends BaseTestCase. It contains all the information for a given test run. You can set/get attributes to/from context, provides a way for inter test communication.

## [Data Beans](databeans.html)

## [Make Tests Data Driven](maketest_data_driven.html)

