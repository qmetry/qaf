---
title: Scenario Meta-Data Filtering Using Include-Exclude Property
sidebar: qaf_2_1_10-sidebar
permalink: qaf-2.1.10/scenario_metadatata_filter_include_exclude_prop.html
folder: qaf-2.1.9
---


There are 2 properties to filter scenario/test cases which is written in bdd or kwd or java.

1. include
2. exclude

You can filter scenario using include-exclude property when you want to run test cases or scenarios that have (or don't have) meta-data with certain value. 

## Include property using specific meta-data

Generally, "include" property can be used to include test cases/scenario at run time. Give include property value as map.

**For example,**

There are two scenarios with different meta-data.


```	
SCENARIO: checkout for mobile
META-DATA: {"description": "checkout scenario", "groups": ["SMOKE"], "channel": "Mobile", "module": "checkout"}
#TODO: call test steps
END
```
 
```  
SCENARIO: login
META-DATA: {"description": "login scenario", "groups": ["SMOKE"], "channel": "Web"}
#TODO: call test steps
END
```

Now apply filter to these two scenarios using below include property.

```
include= {'channel': ['Mobile'], 'module': ['checkout', 'search']}
```

**Conclusion:** It will include only 'checkout for mobile' scenario.


QAF perform Logical AND and OR operation in include property

When you give multiple value for same key, it will perform logical OR operation between them. When you give different keys, it will perform logical AND operation.

 
**For example,**

```
include = {'channel': ['Mobile', 'Web'], 'module': ['checkout', 'PDP', 'search']}
```
It will include test cases/scenario which has meta-data 'channel' whose value is Mobile OR Web AND 'module' whose value is checkout OR PDP OR search.

## Exclude property using specific meta-data

Generally, "exclude" property can be used to exclude (not to run) scenarios/test-cases. Give exclude property value as map. You can exclude test cases/scenarios based on multiple meta-data keys or same key. At that time it will perform OR operation between each values across different meta keys.


**For example,**

There are two scenarios with different meta-data.

```	
SCENARIO: checkout for mobile
META-DATA: {"description": "checkout scenario", "groups": ["SMOKE"], "channel": "Mobile", "module": "checkout"}
#TODO: call test steps
END
```
 
```  
SCENARIO: login
META-DATA: {"description": "login scenario", "groups": ["SMOKE"], "channel": "Web"}
#TODO: call test steps
END
```

Now apply filter to these two scenarios using below include property.

```
exclude={'channel: ['MobileWeb'], 'module': ['checkout', 'PDP']}
```

It will exclude only 'checkout for mobile' scenario. Because it will exclude test cases/scenario 
with meta-data 'channel' whose value is MobileWeb OR 'module' whose value is checkout OR PDP.

**Conclusion:** It will exclude only 'checkout for mobile' scenario.

Example:
  
```	
SCENARIO: checkout for mobile
META-DATA: {"description": "checkout scenario", "groups": ["SMOKE"], "channel": "Mobile", "module": "checkout"}
#TODO: call test steps
END
``` 

```  
SCENARIO: search item
META-DATA: {"description": "login scenario", "groups": ["SMOKE"], "channel": "Web", "module": "search"}}
#TODO: call test steps
END
```

Now apply filter to these two scenarios using below include and exclude property.

 
```
include={'channel':['Mobile','MobileWeb'],'module':['PDP','search']} exclude={'channel': ['web'], 'module': ['cart', 'profile']}
```

 It will include scenario which has meta-data 'channel' with value Mobile OR MobileWeb AND 'module' with value PDP OR search and exclude scenario which has meta-data channel with value web OR module with value cart OR profile.


