---
title: Comment and line break in BDD
sidebar: qaf_latest-sidebar
permalink: latest/bdd2-comment.html
summary: "QAF BDD2 is derived from QAF BDD and gherkin. It supports meta-data from qaf bdd as tags and examples from gherkin."
folder: latest
tags: [bdd,scenario]
---

## Comment

Comments can be placed any where in the bdd file. Comment can be single line or multiline. Single line comment starts with `#` or `!`.

Multi-line comments start with `"""` and end with `"""`.Multiline comment in scenario or background logged in report as info message, however comments outside will not logged in report.

```
 # this is example of single line comment
 # single line comment will be ignored by BDD parser
 ! this is also a comment
 
 """
 This is multi line comment
 will be logged in report if it is inside background or scenario.
 """  

```
## line-break
To break statement in multiple line you can use `_&` as line break.

