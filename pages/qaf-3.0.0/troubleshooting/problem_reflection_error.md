---
title: Reflection Error
sidebar: troubleshoot_sidebar
permalink: qaf-3.0.0/problem_reflection_error.html
folder: qaf-3.0.0
---
You can solve reflection error by following steps.

Option 1 :

     step 1: Delete selenuim-test.jar from lib .

     step 2: Clean Project .

     step 3: Build Project.

 

If it will not resolve using option 1 then follow option 2.

 

Option 2 :

    step 1: Go to java build path.

    step 2: Set jre7 (jre-7u45).

    step 3: Delete all run configuration file of QAF Automated Test.

    step 4: Right click on build.xml.  

    step 5: Go to Run as -> External Tools Configurations.

    step 6: Go to ant build configuration and click on JRE tab.

    step 7: set jre7 in separate JRE.

    step 8: apply config setting.

    step 9: Clean & Build Project.