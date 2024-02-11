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
