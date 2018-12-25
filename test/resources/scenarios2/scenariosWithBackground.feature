Feature: google search

Background:
Given I am on Google Search Page
"""
As prerequisite,
User is expected to be on search page
"""

Scenario: Search InfoStrech using bdd2
When I search for "git qmetry"
Then I get at least 5 results
And it should have "QMetry Automation Framework" in search results


Scenario: Search InfoStrech with results  using bdd2
 When I search for "QAFWebElement"
 Then it should have following search results:['QMetry Automation Framework','Custom component']

Scenario: Search InfoStrech with results with formatted arg
 When I search for "QAFWebElement"
 Then it should have following search results:
 |QMetry Automation Framework|
 |Custom component|


@dataFile:resources/testdata.txt
Scenario: Search Keyword  using bdd2
 When I search for "${searchKey}"
 Then I get at least ${number} results
 Then it should have "${searchResult}" in search results