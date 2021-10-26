---
title: The Project Was Not Built Since Its Build Path Is Incomplete.
sidebar: troubleshoot_sidebar
permalink: qaf-3.0.0/incomplete_build_path.html
folder: qaf-3.0.0
---
It is require to delete and add IVY dependency management library.You can delete and add IVY dependency management library by using following steps: 

	1)Right click on Project -> Properties -> Java Build Path -> Library -> Select ivy and remove.

	2)Add ivy dependency management library again.Go to same place and click add library -> Ivy dependency management.

	3)Resolved all dependency from scratch .Right click on project -> Ivy -> Resolve.

 

 
