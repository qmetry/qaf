Feature: Google Search


Background:
Given I am on Google Search Page

@Web
Scenario: Search InfoStrech
When I search for "git qmetry"
Then I get at least 5 results
And it should have "QMetry Automation Framework" in search results

Scenario: Search InfoStrech with results
 When I search for "QAFWebElement"
 Then it should have following search results:
 | QMetry Automation Framework |
 | Custom & component |

Scenario Outline: Search Keyword
 When I search for "<searchKey>"
 Then I get at least <number> results
 Then it should have "<searchResult>" in search results

Examples:
 | searchKey | searchResult | number |
 | QMetry QAF | QMetry Automation Framework | 10 |
 | Selenium ISFW | Infostretch Test Automation Framework | 20 |

Scenario Outline: Search Keyword using data from file
 When I search for "<searchKey>"
 Then I get at least <number> results
 Then it should have "<searchResult>" in search results

Examples:{'datafile':'resources/testdata.txt'}
