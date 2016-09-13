---
title: Data Beans
sidebar: qaf_latest-sidebar
permalink: latest/databeans.html
folder: latest
tags: [java,testdata]
---
To provide data you can use data beans also. To create data bean you required to extend **BaseDataBean** class. Data bean can be created to provide data to filling forms to communicate data between two or more test case/page and so on. Main advantage of using data beans is

 * Ease of use
 * it can be populated with
    * Random data
    * CSV data
    * Java Map object

Below is the sample data bean.

```java
public class ContactInfoBean extends BaseDataBean{
     
    @Randomizer(prefix="http://www.", length=4, suffix=".com")
    String webSiteURL;
     
    @Randomizer(length=3,type=RandomizerTypes.DIGITS_ONLY)
    String phoneAreaCode, faxAreaCode;
     
    @Randomizer(length=7,type=RandomizerTypes.DIGITS_ONLY)
    String phoneNum, faxNum, mailPostal;
     
    @Randomizer(length=7)
    String mailStreet1, mailCity;
     
    //getters and setters
}

```
The **Randomizer** annotation can be useful to specify random value nature to be set for the property when filling bean with random data using **fillRandomData()** method.

Below are useful examples of using randomizer:

```java
public class SampleBean extends BaseDataBean {
     
    // default randomizer length: 10 type: mixed
     
    @Randomizer(type = RandomizerTypes.LETTERS_ONLY)
    private String name;
     
    @Randomizer(type = RandomizerTypes.DIGITS_ONLY, minval = 18, maxval = 100)
    private String age;
     
    @Randomizer(minval = 7, maxval = 15)
    private Date dateOfTravel;
     
    @Randomizer(suffix = "@mailinator.com", length = 6)
    private String email;
         
    private String pwd;
    @Randomizer(format = "999-99-9999")
    private String ssn;
     
    @Randomizer(skip = true)
    private String dontRandomizeMe;
     
    //getters and setters
}
```
To fill random data: **sampleBean.fillRandomData("sample.data");**

## Data bean property random value from list

Now there is a provision to provide list of values to choose random value with Randomizer. A "dataset" parameter is added in Randomizer annotation. The value of dataset can be a list or single value (ideally name of property that holds list of values).

```java
@Randomizer(dataset = {"male", "female"})
public String gender;
@Randomizer(dataset = "country.list")
public String country;
country.list (in properties file)
country.list=India;China;United States;Africa
```
In first examples list is directly provided as value of dataset, in second example a property name is provided which will have country list as value. 
Sample of xml data to be filled in bean

```xml
<testdata>
    <search>
        <searchinput>Infostretch</searchinput>
    </search>
    <sample>
        <data>
            <name>xmldata</name>
            <email>xmldata@malinator.com</email>
            <pwd>xmldata123#</pwd>
            <ssn>252-06-0029</ssn>
            <!-- <dateOfTravel>3/15/2015</dateOfTravel> -->
            <!-- future date -->
            <dateOfTravel>15</dateOfTravel>
        </data>
    </sample>
</testdata>
```
Populating bean using above xml data. Assumed that the xml file is under resources.
**sampleBean.fillFromConfig("sample.data");**

## Databean population from XML having multiple dataset/node:

Consider the example below UserBean and sample xml test data:

```java
Class UserBean extends BaseDataBean {
    String fname;
    String lname;
    int age;
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}
```

Sample of xml:

```xml
<root>
    <data>
        <search>
            <keyword>infostretch</keyword>
            <verifyText>Ispl</verifyText>
        </search>
        <users>
            <registered>
                <user>
                    <fname>mishal</fname>
                    <lname>shah</lname>
                    <age>35</age>
                </user>
                <user>
                    <fname>shalin</fname>
                    <lname>shah</lname>
                    <age>30</age>
                </user>
            </registered>
        </users>
    </data>
</root>
```
Assumed that above xml file is under configured resources. When populating bean using key **data.users.registered.user**, you can observe there are 2 nodes available with same key. 

```java
UserBean baseDataBean = new UserBean ();
baseDataBean.fillFromConfig("data.users.registed.user"); 
```

As the default behavior bean will be populated with first node (fname=mishal,lname=shah,age=35).
With framework version 2.1.6+, additional Boolean property **bean.populate.random** is introduced to set bean population behavior random. If user set **bean.populate.random=true** then bean will populated with any random node from available nodes.

For instance, in above example, because 2 nodes are available with key **data.users.registered.user**, bean will get populated randomly either from first node (fname=mishal,lname=shah,age=35) or second node (fname=shalin,lname=shah,age=30). 
In general, in the case N available nodes, framework will choose any random node from 1 to N to populate bean.

**Multiple dataset/node and retry failed test**
With framework version 2.1.6+, assuming **bean.populate.random=false**, If test case failed and retry count >0, While rerunning the test case framework will populate bean with next sequential node.
For Instance, If 'retry.count'=1 and test case failed, while rerunning the test case, it will populate bean with second node (fname=shalin,lname=shah,age=30). 
In case of retry count exceeds the XML node count, it will start from first xml data set.

## Form data bean

Form data bean is extended data bean which have capability to interact with UI. In order to create new Form data bean you need to extend **BaseFormDataBean** class. In addition to @Randomizer it provides @UiElement annotation support.

```java
public class FlightSearchForm extends BaseFormDataBean {
...
@UiElement(fieldLoc = FROM_TXT_LOC)
public String from;
@UiElement(fieldLoc = TO_TXT_LOC)
public String to;
...
}
```

## Setting dependency and order for dynamic form fields:

There may be some dynamic UI form fields those are additional fields depending on selection of value in field. For example, take example of flight booking form. If user selects trip type as "return trip" then there will be additional required field of return date. For this requirement there are following two parameters available with UiElement annotation.

   * **dependsOnField :** specify the field name on which this field is dependent
   * **dependingValue :** value of the field that can be either string or a valid JavaScript statement. The JavaScript statement must be valid logical condition returning Boolean value true/false. In JavaScript statement you can pass any of the form field as parameter (i.e. ${fieldName}).

Another aspect in case of dependency is; you must need to ensure order for fill UI data. For example, assuming "return trip" in trip selection form, you must fill trip type and after selection, return date will be available in UI as required field that you need to fill. 
To fill UI data in specific order, you can set order parameter.

```java
@UiElement(fieldLoc = TRIP_TYPE_OPT_LOC, fieldType = Type.optionbox, order=1)
public String tripType;
...
@UiElement(fieldLoc = RETURN_DATE_LOC, dependsOnField = "tripType", dependingValue = "roundTrip", order=9)
public String returnDate;
@UiElement(fieldLoc = RETURN_TIME_LOC, dependsOnField = "tripType", dependingValue = "roundTrip", fieldType = Type.selectbox, order=10)
public String returnTime;
 
...
@Randomizer(minval=0, maxval=2)
@UiElement(fieldLoc = NUB_OF_CHLD_SEL_LOC, fieldType = Type.selectbox, order=13)
public int numChild;
@Randomizer(minval=1, maxval=17)
@UiElement(fieldLoc = CHLD1_AGE_LOC,dependsOnField = "numChild", fieldType = Type.selectbox, dependingValue = "${numChild}>0", order=14)
public int chield1Age = "1";
@Randomizer(minval=1, maxval=17)
@UiElement(fieldLoc = CHLD2_AGE_LOC, fieldType = Type.selectbox, dependsOnField = "numChild", dependingValue = "${numChild}>1",order=15)
public int chield2Age = "1";
```






