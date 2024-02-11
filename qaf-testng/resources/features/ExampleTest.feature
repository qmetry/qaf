Feature: Examples Test

Scenario: Embedded Examples
 Then it should have  ['searchKey', 'searchResult', 'number'] columns in "<args[0]>"
Examples:

 | searchKey | searchResult | number |
 | QMetry QAF | QMetry Automation Framework | 5 |
 | null val|  | 5 |
 | null val|  |  |
 | null val| null | null |
 | empty | "" | 0 |
 | space | " " | 5 |
 
 
Scenario: Embedded Examples step call
 Then it can have "<age>" status "<married>" of "<name>"
Examples:

 | description| name | married | age |
 | go right | uname  | true | 5 |
 | go right | "uname  "| false | 0 |
 | check null | null | null | null |
 | check null | "null" | "null" | "null" |
 | check empty |  |  |  |
  | check number | " 12345" | true | 0 |
 
 
 @dataFile:resources/testdata2.txt
 Scenario: External test data step call
 Then it can have "<age>" status "<married>" of "<name>"
 
 #empty or null not allowed for Primitive types step argument
 Scenario: Embedded Examples step call primptive
 And it should have <age> status "<married>" of "<name>"
Examples:

 | description| name | married | age |
 | go right | uname | true | 5 |
 | go right | uname | false | 0 |
 | empty string |  | false | 0 |
 | null string | null | false | 0 |
 
  
 
 
