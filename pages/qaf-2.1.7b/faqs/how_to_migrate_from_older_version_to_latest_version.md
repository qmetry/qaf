---
title: How to migrate from older version to latest version
sidebar: faq_sidebar
permalink: qaf-2.1.7b/how_to_migrate_from_older_version_to_latest_version.html
folder: qaf-2.1.7b
---

Follow below steps to use QAF 2.1.7b:

1. Rename below properties.
    * selenium.defaultBrowser-->driver.name
    * selenium.server-->remote.server
    * selenium.port-->remote.port
2. Single quote (‘) or Double quote (“) is compulsory for Test Step string argument
3. Copy dashboard folder and dashboard.html file for latest report. [Github Repo](https://github.com/qmetry/qaf-report)
