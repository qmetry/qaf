---
title: Managing Locales
sidebar: qaf_2_1_13-sidebar
permalink: qaf-2.1.13/managing_locales.html
folder: qaf-2.1.13
---

While Automating AUT that supports to different languages, regional differences, we need to take care of different translation/text on UI for different locales.
Assume that the AUT supports Hindi, English and French locales then morning greeting may differ according to the locale selected by user.

For instance, Hindi: "शुभ प्रभात", English: "Good Morning" and French: "Bonjour".


During development you need to define a key for translation data and use that key in code. Specify that key value for different locale file.
In the example below, we had considered that translation data files for Hindi local will have extension "hi", for English: "en" and for French: "fr".

So at the time of execution you need to specify locale as "hi" for Hindi, "en" for English and "fr" for French.
 
```java	
@Test
public void test1() {
...
page.getGreetText().verifyText(
props.getString("morning.greeting.text"));
...
}
```

You need to create different locale files under resources as below:

**Translation.hi**

```properties
#translation messages for hindi locale
morning.greeting.text="शुभ प्रभात"
```

**Translation.en**

```properties
#translation messages for English locale
morning.greeting.text=Good Morning
```

**Translation.fr**

```properties
#translation messages for French locale
morning.greeting.text= Bonjour
```

At the time of execution you need to specify local to load and default local for the execution by setting following two properties. Here locale name must match to the file extension you have given for specific locale.

**application.properties**

```properties
#you can load one or more locale at a time
load.locales=hi;fr
default.locale=hi
```

As you can load more than one local for execution, if you want to access other loaded local which is not default (French in this example):

```java
props.subset("fr").getString("morning.greeting.text ");
```
