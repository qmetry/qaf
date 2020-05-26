/*******************************************************************************
 * Copyright (c) 2019 Infostretch Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.qmetry.qaf.automation.impl.step.qaf;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hamcrest.Matchers;

import com.qmetry.qaf.automation.impl.Item;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chiragj.ayswal
 */
public class QAFTestStepImpl {
	@QAFTestStep(description = "I am on Google Search Page")
	public void step1() {
		System.out.println("I am on Google Search Page");

	}

	@QAFTestStep(description = "I search for {0}")
	public void iSearchFor(String s) {
		System.out.println("I search for " + s);

	}

	@QAFTestStep(description="it should have following search results:{0}")
	public void itShouldHaveAllSearchResults(List<String> s) {
		System.out.printf("List: %s\n", s);

	}

	@QAFTestStep(description="it should have {0} in search results")
	public void itShouldHave_inSearchResults(String s) {
		System.out.printf("it should have %s in search results\n", s);

	}

	@QAFTestStep(description="I get at least {num} results")
	public void iGet_inSearchResults(Integer n) {
		System.out.printf("I get at least %d results\n", n);

	}
	
	@QAFTestStep(description="I have {fruit} and {anotherFruit}")
	public void parameter2(String param1, String optParam) {

	    System.out.println("text:" + param1 + " forText:" + optParam);
	}
	
	@QAFTestStep(description="I have {fruit}")
	public void parameter1(String param1) {

	    System.out.println("text:" + param1);
	}
	
	@QAFTestStep(description="I see following colors:{colors}")
	public void listOfColors(List<String> colors) {
	    System.out.println("colors:" + colors);
	}
	
	
	@QAFTestStep(description="system with following users:{users}")
	public void listOfUser(List<Map<String, Object>> users) {
	    System.out.println("users:" + users);
	    Validator.verifyThat(users.get(0), Matchers.instanceOf(Map.class));
	}
	
	@QAFTestStep(description="system may have following user:{users}")
	public void oneOrMoreUser(Map<String, Object>... users) {
	    System.out.println("users:" + users);
	    Validator.verifyThat(true, Matchers.is(users.getClass().isArray()));
	    for(Map<String, Object> user : users){
		    Validator.verifyThat(user, Matchers.hasKey("name"));
		    Validator.verifyThat(user, Matchers.hasKey("type"));
	    }
	}
	@QAFTestStep(description="I may see following color:{colors}")
	public void oneOrMoreColors(String... colors) {
	    System.out.println("colors:" + colors);
	}
	@QAFTestStep(description="user is:{user}")
	public void aUser(Map<String, Object> user) {
	    System.out.println("users:" + user);
	}
	
	@QAFTestStep(description = "I have one:{item}")
	public void iHaveAnItem(Item item) {
		System.out.println(ClassUtil.getField("name", item));
	}
	
	@QAFTestStep(description = "I have set of:{items}")
	public void iHaveItems(Set<Item> items) {
	    Validator.verifyThat(items.toArray()[0], Matchers.instanceOf(Item.class));
		System.out.println(ClassUtil.getField("name", items.iterator().next()));

	}
	
	@QAFTestStep(description = "I can have one or more:{items}")
	public void iCanHaveOneOrMoreItems(Item... item) {
		System.out.println(ClassUtil.getField("name", item[0]));
	}
	
	@QAFTestStep(description = "test arguments:{map}")
	public Map<String, Object> testArguments(Map<String, Object> map) {
		return map;
	}
	
	@QAFTestStep(description = "test value\\(s) {str} with \\{esc}")
	public String stepWithEsc(String str) {
		return str;
	}
	
	@QAFTestStep(description = "it should have  {cols} columns in {data}")
	public void itShouldHaveColsInData(String[] cols, Map<String, Object> data) {
		for(String col:cols){
			Validator.verifyThat("contains col " + col, data, Matchers.hasKey(col));
		}
	}
	
	@QAFTestStep(description = "it should have  {entries} in {data}")
	public void itShouldHaveEntriesInData(Map<String, Object> entries, Map<String, Object> data) {
		for(Entry<String, Object> entry:entries.entrySet()){
			Validator.verifyThat("contains entry " + entry, data, Matchers.hasEntry(entry.getKey(),entry.getValue()));
		}
	}
	
	@QAFTestStep(description = "it should have {age} status {married} of {name}")
	public void acceptTestDataWithPrimitive (int age, boolean mstatus, String name ) {
		System.out.println("acceptTestDataWithPrimitive age: " + age + " status: " + mstatus+" name: "+name );
		System.out.println("acceptTestDataWithPrimitive is Name not null: " + (null!=name? "yes":"No"));
	}
	@QAFTestStep(description = "it can have {age} status {married} of {name}")
	public void acceptTestDataWithClass(Integer age, Boolean mstatus, String name ) {
		System.out.println("acceptTestDataWithClass age: " + age + " status: " + mstatus+" name: "+name );
		System.out.println("acceptTestDataWithClass is Name not null: " + (null!=name? "yes":"No"));

	}
}
