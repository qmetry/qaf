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

package com.qmetry.qaf.automation.core;

public enum MessageTypes {
	Pass, Fail, Info, TestStep, TestStepPass, TestStepFail, Warn;

	/**
	 * Get message in HTML format for the type.
	 * 
	 * @param message
	 *            : message to be formated
	 * @return HTML formated message for the type
	 */
	public String formatMessage(String message) {
		return String.format(MSG_FORMAT, name().toLowerCase(), name().toLowerCase(), name().charAt(0), message);
	}

	/**
	 * Get message in text format for this type.
	 * 
	 * @param message
	 *            : message to be formated
	 * @return Text formated message for the type
	 */
	public String formatText(String message) {
		return String.format("%s: %s", name(), message);

	}

	private static final String MSG_FORMAT = "<div  class=\"%s\"><span class=\"%s-label\">%s </span>%s</div>";

	public boolean isFailure() {
		return name().toUpperCase().contains("FAIL");
	}

}