/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/


package com.qmetry.qaf.automation.step;

import org.testng.SkipException;

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
 * @author chirag
 */
public interface QAFTestStepListener {
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
