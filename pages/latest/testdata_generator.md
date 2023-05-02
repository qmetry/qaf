---
title: Test Data Generator
sidebar: qaf_latest-sidebar
permalink: latest/testdata_generator.html
folder: latest
tags: [testdata]
---
QAF provides test data generator tool that can be used to generate random data on demand for different testing needs. This includes generating data files with sample data for input to batch processing, generation of random data to feed back-end using api call or database insert statements to ensure test environment has required test data to satisfy testing needs/use cases.

### Schema file

Test data generator uses json schema file as input to generate test data. It is used to provide definition of each field in record, and collector that is used to collect generated data. In addition it also supports master data-set that can be used to select data combination.

##### Example Schema File: 
`mrn-data-schema.json`

```json
{
	"master":{
		"addresses":{"data":[{"city":"Fremont","state":"CA","zip":[94536,94537,94538,94539,94555,94560]},{"city":"Santa Clara","state":"CA","zip":95054}]}
	},
	"fields":[
		{
			"name":"mrn",
			"format":"A99999999"
		},
		{
			"name":"gender",
			"dataset":["male","female"]
		},
		{
			"name":"first-name",
			"length":6
		},
		{
			"name":"last_name",
			"length":8
		},
		{
			"name":"dob",
			"type":"date",
			"min":-1825,
			"max":-36500
		},
		{
			"name":"fulladdress",
			"type":"reference",
			"dataset":["master.addresses"]
		},
		{
			"name":"address",
			"length":10
		},
		{
			"name":"city",
			"reference":"fulladdress.city"
		},
		{
			"name":"state",
			"reference":"fulladdress.state"
		},
		{
			"name":"zip",
			"type":"int",
			"reference":"fulladdress.zip"
		},
		{
			"name":"phone",
			"format":"9999999999"
		},
		{
			"name":"email",
			"type":"calculated",
			"formatVal":"'${first-name}.'+last_name+'@test.com'"
		}
	],
	"collectors":[
		{
			"type":"file",
			"options":{
				"file":"mrnds.csv",
				"headers":true,
				"seperator":",",
				"headers": true,
				"append": false
			}
		}
	]
}
```

### Field:

Each field can be represented using following properties.

|property|description|example|
|--------|-----------|-------|
|name|name of the field||
|type|type of the field. Value can be one of the `int`,`short`,`long`,`double`,`float`,`char`,`date`, `reference`, `calculated` or `cal`. This is optional with default value `char`. Type `calc` represents calculated field and used with `formatVal`. Refer email field in above example.|'type':'int'|
|length|length of the string, used when type is char for random value. Default value is 10|'length':5|
|min|Used for numeric and date types to specify starting range to generate random value. Value can be positive or negative |'min':10|
|max|Used for numeric  and date types to specify ending range to generate random value. Value can be positive or negative|'max':100|
|dataset|Select one of the value from list of possible values for the field.|'dataset':['male','female']|
|format|generate random value using format|'format':'aaa-9999'|
|prefix|add prefix to generated random value|'prefix':'auto'|
|suffix|add suffix to generated random value|'suffix':'@test.com'|
|reference|To refer value from another object field with type as `reference`|'"reference":"fulladdress.zip"|
|charSet|char set to be used for when generating random string. Optional|'charSet':'abcdefghijklmnopqrstuvwxyz*!@#$%^&1234567890'|
|formatVal|name of the field||






