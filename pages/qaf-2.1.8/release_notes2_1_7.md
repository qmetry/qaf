---
title: QAF-2.1.7b Release Notes
sidebar: qaf_2_1_7b_sidebar
permalink: qaf-2.1.8/release_notes2_1_7.html
folder: qaf_2_1_7b
---

## New Features

1. custom meta-data support in json report
2. Test step threshold support at step level
3. Inbuilt step to start and stop time tracking with threshold
4. Test step duration and threshold recording in json report
5. Command execution duration recording in json report
6. license validation any of the MAC address against licenseto addresses
7. test step result as warning when one or more warning in sub steps
8. non-java custom step definition updates
9. custom step definition to have parameter names instead of index
   for ex: instead of bdl step def : "login with {0} and {1}" user can define as "login with {username} and {password}"
10. Supported to access map and array argument in step call within custom step.
11. Removed web driver response status from command log for web driver command result
12. Retry analyzer support for data-driven test
13. Introduced new property 'retry.analyzer' : Provide this property to use your custom retry analyzer
14. Added support to provide license file for license
15. property list value passed as system property
16. Specified default driver "firefoxDriver" if driver property not provided, with console message
17. Added support to specify individual capability with "driver.capabilities" prefix for all driver
18. Enhanced step exceptions handling
19. Enhanced verify/assert for text/attribute/value to have proper message if timeout with element not present
20. Added support to have BDD Keyword Synonyms
21. Added support to scan bdd,kwd and excel file with step provider package to load non java steps
22. Support of TestStep meta-data by using MetaData annotation at method level or at class level.
23. Step Provider Package order and priority.Last package in list has highest priority.
24. Underlying configuration of property util from property configuration to xml configuration
25. For Test Step string argument , Single quote (') or Double quote (") is now compulsory
26. Updated cucumber formatter
27. Deprecated "driver.class" use instead "driverClass"
 
## Bug Fixes
 
1. JIRA project doesn't exist or you don't have permission to view it. [ISFW-148](https://jira.infostretch.com/browse/ISFW-148)
2. JIRA project doesn't exist or you don't have permission to view it. [ISFW-150](https://jira.infostretch.com/browse/ISFW-150)

## QAF Reporter

1. Report Enhancements/Improvements
2. Test step duration indication in checkpoints and subcheckpoints
3. BarChart for Test step threshold and duration
4. Actual summary time at scenario level
5. Added duration in command log
6. Added Different Exception type as Error Analysis
7. Added rerun count
8. Added retry icon at scenario level
9. Well formatted Command Logs and Error Trace