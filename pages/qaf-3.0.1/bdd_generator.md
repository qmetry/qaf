---
title: BDD Generator
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/bdd_generator.html
folder: qaf-3.0.1
tags: [bdd,scenario,java]
summary: "BDD Generator is useful to generate BDD2 feature file that can be used to review or to share with others."

---

You can generate BDD2 feature files from your java/non-java scenario implementation using [Behavior Generator](javadoc/com/qmetry/qaf/automation/report/BDDGenerator.html). Generated feature files can be used to review or to share with other team(s) within project and will enable development team to follow behavior driven development with test automation implementation in best efficient way.

To generate feature files on demand: 
  - add result updater `com.qmetry.qaf.automation.report.BDDGenerator`
  - set `dryrun.mode=true`
  - run your tests

Even if you don't provide dryrun mode or it is false, BDD generator will generate feature files. The generated feature files can be found under `auto_generated/features` folder under project root (it can be changed by setting `bddgenerator.dest` property). Each generated file will be using BDD2 syntax. In case of data driven test/scenario, generated scenario in feature file will have embedded examples, regardless of test data provider.

NOTE: If your scenario implementation is in java, it is recommended to use [runtime scenario](runtime_scenario.html) for better outcome. In order to have feature name in feature file, bdd generator will look for meta-data `story` or `Feature` (can be provided at class level).