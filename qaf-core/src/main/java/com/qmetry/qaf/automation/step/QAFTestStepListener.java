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
package com.qmetry.qaf.automation.step;

import org.testng.SkipException;

import com.qmetry.qaf.automation.core.QAFListener;

/**
 * This is listener class to track test-step execution. The
 * {@link StepExecutionTracker step execution tracker} will be passed to each of
 * the listener method which will provide you all the information that you
 * required including step name, type, index, exception.
 * <p>
 * The very common example of this listener is command prompt reporting during
 * the step execution. Another useful use case is skip the test if step with
 * type "Given" is failed (PreCondition failed). In that case on failure you can
 * check if type is "Given" then set exceptions as {@link SkipException}.
 * 
 * @see QAFTestStep
 * @author chirag.jayswal
 */
public interface QAFTestStepListener extends QAFListener {
	/**
	 * This method will called when ever step failed. You can get the exception
	 * from tracker argument and do the needful. You can set your custom
	 * exception by replacing the original one or if you have recovered the
	 * failure you also can remove the exception from the tracker.
	 * 
	 * @see QAFTestStep
	 * @param stepExecutionTracker
	 */
	public void onFailure(StepExecutionTracker stepExecutionTracker);

	/**
	 * This method will be called before execution of test step. You can get
	 * step , type, parameters information from the argument.
	 * 
	 * @param stepExecutionTracker
	 */
	public void beforExecute(StepExecutionTracker stepExecutionTracker);

	public void afterExecute(StepExecutionTracker stepExecutionTracker);
}
