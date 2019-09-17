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

/**
 * Adapter class for {@link QAFTestStepListener}. Extend this class if you don't
 * want to implement all listener methods.
 * 
 * @author chirag.jayswal
 */
public class QAFTestStepAdapter implements QAFTestStepListener {

	/**
	 * 
	 */
	public QAFTestStepAdapter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.step.QAFTestStepListener#onFailure(com.
	 * infostretch .automation.step.StepExecutionTracker)
	 */
	@Override
	public void onFailure(StepExecutionTracker stepExecutionTracker) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.qmetry.qaf.automation.step.QAFTestStepListener#beforExecute(com.
	 * infostretch.automation.step.StepExecutionTracker)
	 */
	@Override
	public void beforExecute(StepExecutionTracker stepExecutionTracker) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.qmetry.qaf.automation.step.QAFTestStepListener#afterExecute(com.
	 * infostretch.automation.step.StepExecutionTracker)
	 */
	@Override
	public void afterExecute(StepExecutionTracker stepExecutionTracker) {

	}

}
