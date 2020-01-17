Feature: Data-table argument in step

@issue:305
@bug
Scenario: need to test step call with edge cases
#map with one key #305
And user is:
|name|
|auser|
#list of map with one key #305
And system with following users:
|name|
|auser|
|test|

@bug
@feature:303
Scenario: Auto parse List of complex objects in step argument
#map with one key #305
And I have one:
|name|
|item-1|
#list of map with one key #305
And I have set of:
|name|
|item-1|
|item-2|
And I can have one or more:
|name|
|item-1|
And I can have one or more:
|name|
|item-1|
|item-2|
And I have one:
|name|price|
|new-item|5|
#list of map with one key #305
And I have set of:
|name|price|
|item-01|2|
|item-02|1.99|
And I can have one or more:
|name|price|
|item-3|2.99|
And I can have one or more:
|name|price|
|item-101|29.99|
|item-102|101.0|


Scenario: need to test steps with different combination
#variable args
Given system may have following user:
|name|type|
|auser|admin|
|test|user|
And system may have following user:
|name|type|
|auser|admin|
#List of map args
And system with following users:
|name|type|
|auser|admin|
And system with following users:
|name|type|
|auser|admin|
|test|user|
#map arg
And user is:
|name|type|
|auser|admin|
#List arg
And I see following colors:
|red|
|green|
And I see following colors:
|green|
#var args
And I may see following color:
|red|
|green|
And I may see following color:
|green|

Scenario: Optional parameter

 Given I have "Grapes" and "Oranges"
 And I have "Bananas"