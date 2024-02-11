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
package com.qmetry.qaf.automation.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 *
 */
public class ReporterTest {
	// early initialization of static constants for performance will not allow to reset and
	// hence only one at time combination can be tested in isolation

	@Test(dataProvider = "shouldReportDP", enabled = true)
	public void testShouldReport(Object skipSuccess, String logLevel, MessageTypes type, boolean expected) {
		Validator.assertThat(type.shouldReport(), Matchers.is(expected));
	}

	@DataProvider(name = "shouldReportDP")
	public static Iterator<Object[]> testData() {
		ArrayList<Object[]> data = new ArrayList<Object[]>();

		// need to set before class loaded
		// getBundle().setProperty(REPORT_SKIP_SUCCESS.key,false);
		// getBundle().setProperty(REPORT_LOG_LEVEL.key,"Info");
		/*
		data.add(new Object[] { true, "Info", MessageTypes.TestStepFail, true });
		data.add(new Object[] { true, "Info", MessageTypes.TestStepPass, true });
		data.add(new Object[] { true, "Info", MessageTypes.TestStep, true });
		data.add(new Object[] { true, "Info", MessageTypes.Fail, true });
		data.add(new Object[] { true, "Info", MessageTypes.Warn, true });
		data.add(new Object[] { true, "Info", MessageTypes.Pass, true });
		data.add(new Object[] { true, "Info", MessageTypes.Info, true });

		
		 * data.add(new Object[]{false,"Pass",MessageTypes.TestStepFail, true});
		 * data.add(new Object[]{false,"Pass",MessageTypes.TestStepPass, true});
		 * data.add(new Object[]{false,"Pass",MessageTypes.TestStep, true});
		 * data.add(new Object[]{false,"Pass",MessageTypes.Fail, true}); data.add(new
		 * Object[]{false,"Pass",MessageTypes.Warn, true}); data.add(new
		 * Object[]{false,"Pass",MessageTypes.Pass, false}); data.add(new
		 * Object[]{false,"Pass",MessageTypes.Info, false});
		 * 
		 * data.add(new Object[]{true,"Fail",MessageTypes.TestStepFail, true});
		 * data.add(new Object[]{true,"Fail",MessageTypes.TestStepPass, true});
		 * data.add(new Object[]{true,"Fail",MessageTypes.TestStep, true}); data.add(new
		 * Object[]{true,"Fail",MessageTypes.Fail, true}); data.add(new
		 * Object[]{true,"Fail",MessageTypes.Warn, false}); data.add(new
		 * Object[]{true,"Fail",MessageTypes.Pass, false}); data.add(new
		 * Object[]{true,"Fail",MessageTypes.Info, false}); // // skipSuccess true with
		 * log level pass should report all but not pass and Info
		 * 
		 * data.add(new Object[]{true,"Pass",MessageTypes.TestStepFail, true});
		 * data.add(new Object[]{true,"Pass",MessageTypes.TestStepPass, true});
		 * data.add(new Object[]{true,"Pass",MessageTypes.TestStep, true}); data.add(new
		 * Object[]{true,"Pass",MessageTypes.Fail, true}); data.add(new
		 * Object[]{true,"Pass",MessageTypes.Warn, true}); data.add(new
		 * Object[]{true,"Pass",MessageTypes.Pass, false}); data.add(new
		 * Object[]{true,"Pass",MessageTypes.Info, false});
		 * 
		 * // skipSuccess true report all but not pass /* data.add(new
		 * Object[]{true,null,MessageTypes.TestStepFail, true}); data.add(new
		 * Object[]{true,null,MessageTypes.TestStepPass, true}); data.add(new
		 * Object[]{true,null,MessageTypes.TestStep, true}); data.add(new
		 * Object[]{true,null,MessageTypes.Fail, true}); data.add(new
		 * Object[]{true,null,MessageTypes.Warn, true}); data.add(new
		 * Object[]{true,null,MessageTypes.Pass, false}); data.add(new
		 * Object[]{true,null,MessageTypes.Info, true});
		 * 
		 * // skipSuccess false report all
		 * 
		 * data.add(new Object[]{false,null,MessageTypes.Fail, true}); data.add(new
		 * Object[]{false,null,MessageTypes.Pass, true}); data.add(new
		 * Object[]{false,null,MessageTypes.Warn, true}); data.add(new
		 * Object[]{false,null,MessageTypes.TestStep, true}); data.add(new
		 * Object[]{false,null,MessageTypes.TestStepPass, true}); data.add(new
		 * Object[]{false,null,MessageTypes.TestStepFail, true}); data.add(new
		 * Object[]{false,null,MessageTypes.Info, true});
		 * 
		 * // logLevel TestStepPass should report fail, TestStepPass and above messages
		 * 
		 * data.add(new Object[] { null, "TestStepPass", MessageTypes.TestStepFail, true
		 * }); data.add(new Object[] { null, "TestStepPass", MessageTypes.TestStepPass,
		 * true }); data.add(new Object[] { null, "TestStepPass", MessageTypes.TestStep,
		 * false }); data.add(new Object[] { null, "TestStepPass", MessageTypes.Fail,
		 * true }); data.add(new Object[] { null, "TestStepPass", MessageTypes.Warn,
		 * false }); data.add(new Object[] { null, "TestStepPass", MessageTypes.Pass,
		 * false }); data.add(new Object[] { null, "TestStepPass", MessageTypes.Info,
		 * false });
		 * 
		 * // logLevel TestStep should report fail, TestStep and above messages
		 * 
		 * data.add(new Object[] { null, "TestStep", MessageTypes.TestStepFail, true });
		 * data.add(new Object[] { null, "TestStep", MessageTypes.TestStepPass, true });
		 * data.add(new Object[] { null, "TestStep", MessageTypes.TestStep, true });
		 * data.add(new Object[] { null, "TestStep", MessageTypes.Fail, true });
		 * data.add(new Object[] { null, "TestStep", MessageTypes.Warn, false });
		 * data.add(new Object[] { null, "TestStep", MessageTypes.Pass, false });
		 * data.add(new Object[] { null, "TestStep", MessageTypes.Info, false });
		 * 
		 * // logLevel Fail should report fail and above messages
		 * 
		 * data.add(new Object[] { null, "Fail", MessageTypes.TestStepFail, true });
		 * data.add(new Object[] { null, "Fail", MessageTypes.TestStepPass, true });
		 * data.add(new Object[] { null, "Fail", MessageTypes.TestStep, true });
		 * data.add(new Object[] { null, "Fail", MessageTypes.Fail, true });
		 * data.add(new Object[] { null, "fail", MessageTypes.Warn, false });
		 * data.add(new Object[] { null, "fail", MessageTypes.Pass, false });
		 * data.add(new Object[] { null, "Fail", MessageTypes.Info, false });
		 * 
		 * // logLevel Pass should report pass and above messages
		 * 
		 * data.add(new Object[] { null, "Pass", MessageTypes.TestStepFail, true });
		 * data.add(new Object[] { null, "Pass", MessageTypes.TestStepPass, true });
		 * data.add(new Object[] { null, "Pass", MessageTypes.TestStep, true });
		 * data.add(new Object[] { null, "pass", MessageTypes.Fail, true });
		 * data.add(new Object[] { null, "Pass", MessageTypes.Warn, true });
		 * data.add(new Object[] { null, "pass", MessageTypes.Pass, true });
		 * data.add(new Object[] { null, "Pass", MessageTypes.Info, false });
		 * 
		 * // logLevel Info should report all messages data.add(new Object[] { null,
		 * "Info", MessageTypes.Fail, true }); data.add(new Object[] { null, "Info",
		 * MessageTypes.Pass, true }); data.add(new Object[] { null, "Info",
		 * MessageTypes.Warn, true }); data.add(new Object[] { null, "Info",
		 * MessageTypes.TestStep, true }); data.add(new Object[] { null, "Info",
		 * MessageTypes.TestStepPass, true }); data.add(new Object[] { null, "Info",
		 * MessageTypes.TestStepFail, true }); data.add(new Object[] { null, "Info",
		 * MessageTypes.Info, true });
		 * */
		  // default should report all messages 
		data.add(new Object[] { null, null, MessageTypes.Fail, true });
		data.add(new Object[] { null, null, MessageTypes.Pass, true });
		data.add(new Object[] { null, null, MessageTypes.Warn, true });
		data.add(new Object[] { null, null, MessageTypes.TestStep, true });
		data.add(new Object[] { null, null, MessageTypes.TestStepPass, true });
		data.add(new Object[] { null, null, MessageTypes.TestStepFail, true });
		data.add(new Object[] { null, null, MessageTypes.Info, true });

		return data.iterator();
	}
}
