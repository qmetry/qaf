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
|formatVal|format||


### collectors

Collector has following properties:

|property|description|example|
|--------|-----------|-------|
|type    |required one of the collector type| "type": "file"|
|disabled|optional boolean default false, set true to disable collector| "disabled":true|
|options |options for selected type| {"type": "file","options": {"seperator": ",","headers": true,"append": false}}|


Below are supported `type` for collector:
 - file
 - database or db
 - json
 - expr
 
|type|options|description|
|--------|-----------|-------|
|file|file|Required string file path |
||headers|Boolean value to add add header line |
||append|Boolean value to append if file exist |
||seperator|Optional column seperator |
||outputFormat|Optional format for e|
|db|sql|sql statement with field names. INSERT INTO table_name (column1, column2, column3) VALUES ("${fldName1str}", ${fldName2}, ${fldName3})|
||prefix|optional database connection prefix|
|json|file|file path|
|expr|call|expression|


 examples:
 
 ```
 	"collectors": [
		{
			"type": "file",
			"options": {
				"file": "auto-appointment-data.log",
				"seperator": ",",
				"headers": true,
				"append": false
			}
		},
		{
			"type": "expr",
			"disabled":true,
			"options": {
				"call": "com.qmetry.qaf.automation.step.WsStep.userRequests('oai.appointments.req.post',_record)"
			}
		},
		{
			"type": "expr",
			"disabled":false,
			"options": {
				"call": "java.lang.System.out.println(_record)"
			}
		}
	]
 ```

#### Example Schema File: 

Example - 1 `appointments_structure.json` will call api for each record in generated data.
 
```
{
	"samples": 10,
	"master": {},
	"fields": [
		{
			"name": "_id",
			"formatVal": "java.util.UUID.randomUUID()"
		},
		{
			"name": "mrn",
			"dataset": [
				"aaa"
			]
		},
		{
			"name": "provider",
			"dataset": [
				"Dr Henry Godin",
				"Dr Jackson Myers",
				"Dr Henry Myers"
			]
		},
		{
			"name": "appointmentDate",
			"type": "date",
			"min": 0,
			"max": 10,
			"formatVal": "com.qmetry.qaf.automation.util.DateUtil.getFormatedDate(_value, 'MM/dd/yyyy')"
		},
		{
			"name": "appointmentTime",
			"min": 6,
			"max": 12,
			"formatVal": "_value.intValue() +'pm'"
		},
		{
			"name": "appointmentDuration",
			"dataset": [
				"30",
				"60"
			]
		},
		{
			"name": "appointmentRecurrence",
			"type": "date",
			"min": 10,
			"max": 15,
			"formatVal": "com.qmetry.qaf.automation.util.DateUtil.getFormatedDate(_value, 'MM/dd/yyyy')"
		}
	],
	"collectors": [
		{
			"type": "file",
			"options": {
				"file": "auto-appointment-data.log",
				"seperator": ",",
				"headers": true,
				"append": false
			}
		},
		{
			"type": "expr",
			"options": {
				"call": "com.qmetry.qaf.automation.step.WsStep.userRequests('oai.appointments.req.post',_record)"
			}
		},
		{
			"type": "expr",
			"disabled":true,
			"options": {
				"call": "java.lang.System.out.println(_record)"
			}
		}
	]
}
```
Example - 2 `mrn-data-schema.json`

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


Example:3

```
{
	"master":{
		"addresses":{"metadata":{"datafile":"resources/data/citymaster.csv"}}
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
			"dataset":["${gender}-name-samples"]
		},
		{
			"name":"last_name",
			"dataset":["last-name-samples"]
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
			"name":"phon",
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
			"type":"file","options":{"file":"mrnds.log","headers":true,"seperator":","}
		}
	]
}

```

### Usage
Example:

```
public static void generateAppointMents(String mrn) throws ScriptException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mrn.dataset", new Object[] { mrn });

		Reporter.log("Generating upcoming appointments for MRN " + mrn);

		params.put("appointmentDate.min", 1);
		params.put("appointmentDate.max", 10);

		JsonDataBean bean = JsonDataBean.get("resources/data/appointments_structure.json", params);

		bean.setSamples(5);
		bean.generateData();

		Reporter.log("Generating past appointments for MRN " + mrn);

		params.put("collectors.file.options.append", true);
		params.put("collectors.file.options.headers", false);
		params.put("appointmentDate.min", -10);
		params.put("appointmentDate.max", -1);

		bean = JsonDataBean.get("resources/data/appointments_structure.json", params);

		bean.setSamples(5);
		bean.generateData();
	}
```

Another example of setting collector:

```
		JsonDataBean bean = JsonDataBean.get("resources/data/env_struct.json");

		bean.setSamples(5);
		bean.removeAllCollectors();
		JsonCollector jsonCollector = new JsonCollector();
		bean.addCollector(jsonCollector );
		bean.generateData();
		//get generated data from collector to use it in your code
		System.out.println(jsonCollector.getDataset());
```