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
 
 
