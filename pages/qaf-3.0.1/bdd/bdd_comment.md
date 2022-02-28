---
title: Comment and line break in BDD
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/bdd-comment.html
summary: "QAF BDD supports single line and multiline comment"
folder: qaf-3.0.1
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

