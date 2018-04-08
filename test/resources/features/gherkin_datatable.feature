Feature: Data-table argument in step


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