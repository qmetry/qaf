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


package com.qmetry.qaf.automation.util;

import java.util.Random;

public abstract class RandomStringGenerator {

	public enum RandomizerTypes {
		DIGITS_ONLY, LETTERS_ONLY, MIXED
	}

	private static final String LETTERS = "qwertyuiopzxcvbnmasdfghjklAZERTYUIOPMLKJHGFDSQWXCVBN";
	private static final int LETTERS_LENGTH = LETTERS.length();
	private static final String NUMBERS = "1357924680";
	private static final int NUMBERS_LENGTH = NUMBERS.length();

	public static String get(int length) {
		return get(new Random(System.currentTimeMillis()), length, RandomizerTypes.MIXED);
	}

	public static String get(int length, RandomizerTypes type) {
		return get(new Random(System.currentTimeMillis()), length, type);
	}

	public static String get(Random random, int length, RandomizerTypes type) {
		random.setSeed(System.currentTimeMillis());
		if (length <= 0) {
			throw new IllegalArgumentException("length has to be bigger zero");
		}

		StringBuilder generated_str = new StringBuilder("");
		boolean type_selector = false;

		for (int i = 0; i < length; i++) {
			type_selector = random.nextBoolean();

			// characters
			if ((RandomizerTypes.LETTERS_ONLY == type) || ((type != RandomizerTypes.DIGITS_ONLY) && type_selector)) {
				int ele1 = random.nextInt(LETTERS_LENGTH);
				System.out.println(ele1);
				char c = LETTERS.charAt(ele1);
				if (random.nextDouble() > 0.5D) {
					c = Character.toUpperCase(c);
				}
				generated_str.append(c);
			}
			// digits
			else {
				int ele = random.nextInt(NUMBERS_LENGTH);
				generated_str.append(NUMBERS.charAt(ele));
			}
		}

		return generated_str.toString();
	}
}
