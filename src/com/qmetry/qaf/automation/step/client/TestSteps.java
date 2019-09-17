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
package com.qmetry.qaf.automation.step.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for testSteps element declaration.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;element name="testSteps">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="testStep">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="arg" maxOccurs="unbounded" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="lineNo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                             &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="noOfaArgs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "testStep" })
@XmlRootElement(name = "testSteps")
public class TestSteps {

	@XmlElement(required = true)
	protected List<TestStep> testStep;

	/**
	 * Gets the value of the testStep property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the testStep property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getTestStep().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link TestStep
	 * }
	 */
	public List<TestStep> getTestStep() {
		if (testStep == null) {
			testStep = new ArrayList<TestStep>();
		}
		return testStep;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="arg" maxOccurs="unbounded" minOccurs="0">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *                 &lt;attribute name="lineNo" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *               &lt;/restriction>
	 *             &lt;/complexContent>
	 *           &lt;/complexType>
	 *         &lt;/element>
	 *       &lt;/sequence>
	 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *       &lt;attribute name="noOfaArgs" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "arg" })
	public static class TestStep {

		@XmlElement(required = true)
		protected List<Arg> arg;
		@XmlAttribute
		protected String description;
		@XmlAttribute
		protected String name;
		@XmlAttribute
		protected String noOfaArgs;
		@XmlAttribute
		protected String returns;

		/**
		 * Gets the value of the arg property.
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the arg property.
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getArg().add(newItem);
		 * </pre>
		 * <p>
		 * Objects of the following type(s) are allowed in the list {@link Arg }
		 */
		public List<Arg> getArg() {
			if (arg == null) {
				arg = new ArrayList<Arg>();
			}
			return arg;
		}

		/**
		 * Gets the value of the description property.
		 * 
		 * @return possible object is {@link String }
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Sets the value of the description property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 */
		public void setDescription(String value) {
			description = value;
		}

		/**
		 * Gets the value of the name property.
		 * 
		 * @return possible object is {@link String }
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the value of the name property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 */
		public void setName(String value) {
			name = value;
		}

		/**
		 * Gets the value of the noOfaArgs property.
		 * 
		 * @return possible object is {@link String }
		 */
		public String getNoOfaArgs() {
			return noOfaArgs;
		}

		/**
		 * Sets the value of the noOfaArgs property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 */
		public void setNoOfaArgs(String value) {
			noOfaArgs = value;
		}

		/**
		 * @return the returns
		 */
		public String getReturns() {
			return returns;
		}

		/**
		 * @param returns
		 *            the returns to set
		 */
		public void setReturns(String returns) {
			this.returns = returns;
		}

		/**
		 * <p>
		 * Java class for anonymous complex type.
		 * <p>
		 * The following schema fragment specifies the expected content
		 * contained within this class.
		 * 
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
		 *       &lt;attribute name="lineNo" type="{http://www.w3.org/2001/XMLSchema}string" />
		 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
		 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
		 *     &lt;/restriction>
		 *   &lt;/complexContent>
		 * &lt;/complexType>
		 * </pre>
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "")
		public static class Arg {

			@XmlAttribute
			protected String description;
			@XmlAttribute
			protected String index;
			@XmlAttribute
			protected String name;
			@XmlAttribute
			protected String type;

			/**
			 * Gets the value of the description property.
			 * 
			 * @return possible object is {@link String }
			 */
			public String getDescription() {
				return description;
			}

			/**
			 * Sets the value of the description property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 */
			public void setDescription(String value) {
				description = value;
			}

			/**
			 * Gets the value of the lineNo property.
			 * 
			 * @return possible object is {@link String }
			 */
			public String getIndex() {
				return index;
			}

			/**
			 * Sets the value of the lineNo property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 */
			public void setIndex(String value) {
				index = value;
			}

			/**
			 * Gets the value of the name property.
			 * 
			 * @return possible object is {@link String }
			 */
			public String getName() {
				return name;
			}

			/**
			 * Sets the value of the name property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 */
			public void setName(String value) {
				name = value;
			}

			/**
			 * Gets the value of the type property.
			 * 
			 * @return possible object is {@link String }
			 */
			public String getType() {
				return type;
			}

			/**
			 * Sets the value of the type property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 */
			public void setType(String value) {
				type = value;
			}

		}

	}

}
