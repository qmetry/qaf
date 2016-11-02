@Web
Feature: Google Search


@Smoke
Scenario: Search InfoStrech

 Given I am on Google Search Page
 When I search for "git qmetry"
 Then I get at least 5 results
 And it should have "QMetry Automation Framework" in search results
