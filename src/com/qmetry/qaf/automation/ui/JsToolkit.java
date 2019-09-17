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
package com.qmetry.qaf.automation.ui;


/**
 * @author chirag.jayswal
 *
 */
public enum JsToolkit {
	DOJO("dojo", "dojo.io.XMLHTTPTransport.inFlight.length==0"), EXTJS("Ext",
			"Ext.Ajax.isLoading()==false"), JQUERY("jQuery", "jQuery.active==0"), YUI("YAHOO",
					"YAHOO.util.Connect.isCallInProgress==false"), PHPJS("PHP_JS",
							"PHP_JS.resourceIdCounter==0"), PROTOTYPE("Ajax", "Ajax.activeRequestCount==0");

	String identifier;
	String expr;

	private JsToolkit(String identifier, String expr) {
		this.identifier = identifier;
		this.expr = expr;
	}
	
	

	public String waitCondition() {
		return "return " + getExpr() +";";
	}

	public static String globalWaitCondition() {
		StringBuilder sb = new StringBuilder("return ");
		for(JsToolkit toolkit: JsToolkit.values()){
			sb.append(" ("+ toolkit.getExpr() + ") &&");
		}
		sb.append(";");
		return sb.toString().replace(" &&;", ";");
	}
	
	public String getExpr(){
		return "((typeof "+ identifier +" === 'undefined') || (" + expr + "))";
	}
}
