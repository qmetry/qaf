Feature: Examples with tag

Scenario Outline: Search Keyword with one tagged examples
 Given I am on Google Search Page
 When I search for "<searchKey>"
 Then I get at least <number> results
 Then it should have "<searchResult>" in search results

@tag1
Examples:
 | searchKey | searchResult | number |
 | tag1 |tag1| 10 |
 | tag1-2 | tag1-2 | 20 |


Scenario Outline: Search Keyword with tagged examples
 Given I am on Google Search Page
 When I search for "<searchKey>"
 Then I get at least <number> results
 Then it should have "<searchResult>" in search results

@tag1
Examples:
 | searchKey | searchResult | number |
 | tag1 |tag1| 10 |
 | tag1-2 | tag1-2 | 20 |

@tag-to-include
Examples:
 | searchKey | searchResult | number |
 | QMetry & QAF | QMetry Automation Framework | 10 |
 | Selenium ISFW | Infostretch Test Automation Framework | 20 |

@group1
@author:Chirag Jayswal
Scenario Outline: Search Keyword with meta-data and tagged examples
 Given I am on Google Search Page
 When I search for "<searchKey>"
 Then I get at least <number> results
 Then it should have "<searchResult>" in search results

@tag1
Examples:
 | searchKey | searchResult | number |
 | tag1 |tag1-10| 10 |
 | tag1-2 | tag1-20 | 20 |

@tag-to-include
Examples:
 | searchKey | searchResult | number |
 | tag2 |tag2-10| 10 |
 | tag2-2 | tag2-20 | 20 |

@tag3
Examples:
 | searchKey | searchResult | number |
 | QMetry & QAF | QMetry Automation Framework | 10 |
 | Selenium ISFW | Infostretch Test Automation Framework | 20 |

 
 Scenario Outline: Search Keyword with default tagged examples
 Given I am on Google Search Page
 When I search for "<searchKey>"
 Then I get at least <number> results
 Then it should have "<searchResult>" in search results

@tag1
Examples:
 | searchKey | searchResult | number |
 | tag1 |tag1| 10 |
 | tag1-2 | tag1-2 | 20 |
 
@tag5 @default
Examples:
 | searchKey | searchResult | number |
 | tag5@default |tag5| 10 |
 | tag5-2@default | tag5-2 | 20 |
 
  Scenario Outline: Search Keyword with tag and default tagged examples
  Given I am on Google Search Page
  When I search for "<searchKey>"
  Then I get at least <number> results
  Then it should have "<searchResult>" in search results

#should be considered as when no include groups provided
@default
Examples:
 | searchKey | searchResult | number |
 | @default |tag1| 10 |
 | @default-2 | tag1-2 | 20 |
 
@tag1
Examples:
 | searchKey | searchResult | number |
 | tag1 |tag1| 10 |
 | tag1-2 | tag1-2 | 20 |
 
  Scenario Outline: Search Keyword with tag and no tagged examples
  Given I am on Google Search Page
  When I search for "<searchKey>"
  Then I get at least <number> results
  Then it should have "<searchResult>" in search results

#no tags should be considered as default when no include groups specified
Examples:
 | searchKey | searchResult | number |
 | @default |tag1| 10 |
 | @default-2 | tag1-2 | 20 |
 
@tag1
Examples:
 | searchKey | searchResult | number |
 | tag1 |tag1| 10 |
 | tag1-2 | tag1-2 | 20 |
 
 
  Scenario Outline: Search Keyword with tag to include and no tagged examples
  Given I am on Google Search Page
  When I search for "<searchKey>"
  Then I get at least <number> results
  Then it should have "<searchResult>" in search results

#no tags should be considered as default when no include groups specified
Examples:
 | searchKey | searchResult | number |
 | @default |tag1| 10 |
 | @default-2 | tag1-2 | 20 |
 
@tag-to-include
Examples:
 | searchKey | searchResult | number |
 | tag1 |tag1| 10 |
 | tag1-2 | tag1-2 | 20 |