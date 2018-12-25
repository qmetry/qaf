Feature: meta-data test

@desc:sharing calss objct for steps
SCENARIO: add amount using bdd2
Given i have 10 rupees
When i add 5 rupees
And i should have 15 rupees


@nagetive @desc:this should not executed @enabled:false
SCENARIO: add amount disabled
Given i have 10 rupees
When i add 5 rupees
And i should have 25 rupees

