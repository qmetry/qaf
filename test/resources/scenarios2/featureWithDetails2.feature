@Web
Feature: Google Search
Search should return result only related to serach term,
not other

@Smoke
Scenario: Search InfoStrech

 Given I am on Google Search Page
 When I search for "git qmetry"
 Then I get at least 5 results
 And it should have "QMetry Automation Framework" in search results

 @Smoke
Scenario: Search InfoStrech
"""
Additional notes
And another notes

"""

 Given I am on Google Search Page
 When I search for "git qmetry"
 Then I get at least 5 results
 And it should have "QMetry Automation Framework" in search results