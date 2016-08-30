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

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

import com.qmetry.qaf.automation.core.ConfigurationManager;

/**
 * com.qmetry.qaf.automation.util.ImageCompareUtil.java Utility class for image
 * comparison
 * <dl>
 * <dt>Useful porperties:</dt>
 * <dd>img.allow.max.diff: default is 100</dd>
 * </dl>
 * 
 * @author chirag
 */
public class ImageCompareUtil {
	private static final int baseSize = 300;
	private static final int maxDiff = ConfigurationManager.getBundle().getInt("img.allow.max.diff", 100);

	public boolean contains(String reference, String template, Point start) throws Exception {
		RenderedImage ref = (ImageIO.read(new File(reference)));

		// Calculate the signature vector for the reference.
		// Now we need a component to store X images in a stack, where X is the
		// number of images in the same directory as the original one.
		// For each image, calculate its signature and its distance from the
		// reference signature.
		RenderedImage other = ImageIO.read(new File(template));

		int x, y, h, w, th, tw;
		double distance = Double.MAX_VALUE;
		h = ref.getHeight();
		w = ref.getWidth();
		System.out.println("px width: " + ref.getData().getWidth() + "px height: " + ref.getData().getHeight());
		System.out.println("width: " + ref.getWidth() + "height: " + ref.getHeight());
		System.out.println("min x: " + ref.getData().getMinX() + " y: " + ref.getData().getMinY());

		th = other.getHeight();
		tw = other.getWidth();

		for (int r = 0; r <= (h - th); r += 5) {
			for (int c = 0; c <= (w - tw); c += 5) {
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(ref);
				pb.add((float) c);
				pb.add((float) r);
				pb.add((float) tw);
				pb.add((float) th);
				pb.add(new InterpolationNearest());
				// Creates a new, scaled image and uses it on the DisplayJAI
				// component

				try {
					double tdistance = calcDistance(rescale(JAI.create("crop", pb)), rescale(other));
					if ((tdistance < distance)) {
						distance = tdistance;
					}

					if (distance == 0) {
						break;
					}
					System.out.println("distance" + distance + " x: " + r + " y: " + c);
				} catch (Exception e) {
					System.out.print("Error: " + e.toString());
					e.printStackTrace();
				}
			}

			if (distance == 0) {
				break;
			}
		}
		return distance < maxDiff;
	}

	public boolean compare(String img1, String img2) throws IOException {

		return getDifference(img1, img2) < maxDiff;

	}

	public double getDifference(String img1, String img2) throws IOException {
		RenderedImage ref = rescale(ImageIO.read(new File(img1)));

		RenderedImage other = rescale(ImageIO.read(new File(img2)));
		return calcDistance(ref, other);
	}

	public boolean doMatch(String img1, String img2, Rectangle... rectangles) throws IOException {
		RenderedImage search = (ImageIO.read(new File(img1)));
		RenderedImage template = (ImageIO.read(new File(img2)));

		return doMatch(search, template, rectangles);
	}

	public boolean doMatch(BufferedImage search, String img2, Rectangle... rectangles) throws IOException {
		RenderedImage template = (ImageIO.read(new File(img2)));

		return doMatch(search, template, rectangles);
	}

	/**
	 * @param search
	 *            : image to search.
	 * @param template
	 *            : base/template image to which target image
	 * @param rectangles
	 *            : arg[0] area of image to search, arg[1] area of template
	 * @return
	 */
	public boolean doMatch(RenderedImage search, RenderedImage template, Rectangle... rectangles) {
		RandomIter searchiterator = RandomIterFactory.create(search, null);

		RandomIter templateiterator = RandomIterFactory.create(template, null);

		double minSAD = Double.MAX_VALUE;

		Rectangle sRect = (rectangles == null) || (rectangles.length < 1) ? new Rectangle() : rectangles[0];
		Rectangle tRect = (rectangles == null) || (rectangles.length < 2) ? new Rectangle() : rectangles[1];

		int T_rows = tRect.width > 0 ? tRect.x + tRect.width : template.getWidth();
		int S_rows = sRect.width > 0 ? sRect.x + sRect.width
				: (tRect.width > 0 ? search.getWidth() - tRect.width : search.getWidth() - template.getWidth());
		int T_cols = tRect.height > 0 ? tRect.y + tRect.height : template.getHeight();
		int S_cols = (sRect.height > 0 ? sRect.y + sRect.height
				: ((tRect.height > 0 ? (search.getHeight() - tRect.height)
						: search.getHeight() - template.getHeight())));

		// loop through the search image

		double[] p_SearchIMG = new double[5];
		double[] p_TemplateIMG = new double[5];
		double[] S_accum = new double[3];
		Point bMatch = null;
		boolean match = false;
		double SAD = 0.0;
		int samplesCnt = 0;
		int x = 0, y = 0, i = 0, j = 0;
		for (x = sRect.x; x <= S_rows; x += 1) {

			for (y = sRect.y; y <= S_cols; y += 1) {
				match = true;
				SAD = 0.0;
				samplesCnt = 0;
				// loop through the template image
				for (i = tRect.x; i < T_rows - 5; i += 5) {

					for (j = tRect.y; j < T_cols - 5; j += 5) {
						S_accum[0] = S_accum[1] = S_accum[2] = 0;
						for (int ploat = 0; ploat < 5; ploat++) {
							searchiterator.getPixel(x + i + ploat - tRect.x, y + j + ploat - tRect.y, p_SearchIMG);
							templateiterator.getPixel(i + ploat, j + ploat, p_TemplateIMG);
							S_accum[0] += Math.abs(p_SearchIMG[0] - p_TemplateIMG[0]);
							S_accum[1] += Math.abs(p_SearchIMG[1] - p_TemplateIMG[1]);
							S_accum[2] += Math.abs(p_SearchIMG[2] - p_TemplateIMG[2]);

						}
						Double tempSad = (S_accum[0] + S_accum[1] + S_accum[2]) / 5;

						SAD += tempSad;

						if (tempSad < minSAD) {
							minSAD = tempSad;
							bMatch = new Point(x, y);
						}
						samplesCnt += 5;
						if ((tempSad > maxDiff)) {

							match = false;
							break;

						}
						// System.out.println("***************\nLast SAD: " +
						// tempSad
						// + " for : " + samplesCnt + " samples: i: " + i +
						// "j: "
						// + j);

					}

				}
				// int samplecnt = (i + 1) * (i + 1) / 25;
				SAD = SAD / samplesCnt;

				// System.out.println("***************\nLast SAD: " + SAD +
				// " for : "
				// + samplesCnt + " samples: x: " + x + "y: " + y);

				if (match) {

					break;
				}

			}
			if (match) {

				break;
			}
			// System.out.println("Match: " + match + " x: " + x + " y: " + y +
			// " dist: "
			// + minSAD + " bMatch: " + bMatch);
		}
		System.out
				.print("Match: " + match + " x: " + x + " y: " + y + " dist: " + minSAD + " bMatch: " + bMatch + tRect);
		return match;
	}

	/*
	 * This method calculates and returns signature vectors for the input image.
	 */
	private Color[][] calcSignature(RenderedImage i) {
		// Get memory for the signature.
		Color[][] sig = new Color[5][5];
		// For each of the 25 signature values average the pixels around it.
		// Note that the coordinate of the central pixel is in proportions.
		float[] prop = new float[] { 1f / 10f, 3f / 10f, 5f / 10f, 7f / 10f, 9f / 10f };
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				sig[x][y] = averageAround(i, prop[x], prop[y]);
			}
		}
		return sig;
	}

	private Color averageAround(RenderedImage i, double px, double py) {
		return averageAround(i, px, py, baseSize, baseSize);
	}

	private Color averageAround(RenderedImage i, double px, double py, int width, int height) {
		// Get an iterator for the image.
		RandomIter iterator = RandomIterFactory.create(i, null);
		// Get memory for a pixel and for the accumulator.
		double[] pixel = new double[5];
		double[] accum = new double[5];
		// The size of the sampling area.
		int sampleSize = 15;
		int numPixels = 0;
		// Sample the pixels.
		for (double x = px * width - sampleSize; x < px * width + sampleSize; x++) {
			try {
				for (double y = py * height - sampleSize; y < py * height + sampleSize; y++) {
					iterator.getPixel((int) x, (int) y, pixel);
					accum[0] += pixel[0];
					accum[1] += pixel[1];
					accum[2] += pixel[2];
					numPixels++;
				}
			} catch (Exception e) {

			}
		}
		// Average the accumulated values.
		accum[0] /= numPixels;
		accum[1] /= numPixels;
		accum[2] /= numPixels;
		return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
	}

	/*
	 * This method calculates the distance between the signatures of an image
	 * and the reference one. The signatures for the image passed as the
	 * parameter are calculated inside the method.
	 */
	private double calcDistance(RenderedImage ref, RenderedImage other) {
		Color[][] signature = calcSignature(ref);

		// Calculate the signature for that image.
		Color[][] sigOther = calcSignature(other);
		// There are several ways to calculate distances between two vectors,
		// we will calculate the sum of the distances between the RGB values of
		// pixels in the same positions.
		double dist = 0;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				int r1 = signature[x][y].getRed();
				int g1 = signature[x][y].getGreen();
				int b1 = signature[x][y].getBlue();
				int r2 = sigOther[x][y].getRed();
				int g2 = sigOther[x][y].getGreen();
				int b2 = sigOther[x][y].getBlue();
				double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2));
				dist += tempDist;
			}
		}
		return dist;
	}

	/*
	 * This method rescales an image to 300,300 pixels using the JAI scale
	 * operator.
	 */
	private RenderedImage rescale(RenderedImage i) {
		float scaleW = ((float) baseSize) / i.getWidth();
		float scaleH = ((float) baseSize) / i.getHeight();
		// Scales the original image
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(i);
		pb.add(scaleW);
		pb.add(scaleH);
		pb.add(0.0F);
		pb.add(0.0F);
		pb.add(new InterpolationNearest());
		// Creates a new, scaled image and uses it on the DisplayJAI component
		return JAI.create("scale", pb);
	}

	public void doAvgMatch(String img1, String img2) throws IOException {
		RenderedImage search = JAI.create("fileload", img1);// (ImageIO.read(new
		// File(img1)));
		RandomIter searchiterator = RandomIterFactory.create(search, null);

		RenderedImage template = JAI.create("fileload", img2);// (ImageIO.read(new
		// File(img2)));
		RandomIter templateiterator = RandomIterFactory.create(template, null);

		int S_rows = search.getWidth();
		int T_rows = template.getWidth();
		int S_cols = search.getHeight();
		int T_cols = template.getHeight();
		// loop through the search image

		double[] p_SearchIMG = new double[5];
		double[] p_TemplateIMG = new double[5];
		int[] accum = new int[3];

		double SAD = 0.0;
		double minSAD = Double.MAX_VALUE;

		int max = T_rows * T_cols;
		Point bMatch = null;
		boolean match = false;
		int x = 0, y = 0, i = 0, j = 0;
		for (x = 0; x < S_rows - T_rows; x += 1) {

			for (y = 0; y < S_cols - T_cols; y += 1) {
				accum[0] = 0;
				accum[1] = 0;
				accum[2] = 0;
				// loop through the template image
				for (i = 0; i < T_rows; i += 1) {

					for (j = 0; j < T_cols; j += 1) {

						templateiterator.getPixel(i, j, p_TemplateIMG);
						searchiterator.getPixel(x + i, y + j, p_SearchIMG);
						accum[0] += Math.abs(p_SearchIMG[0] - p_TemplateIMG[0]);
						accum[1] += Math.abs(p_SearchIMG[1] - p_TemplateIMG[1]);
						accum[2] += Math.abs(p_SearchIMG[2] - p_TemplateIMG[2]);

						// double tempDist =
						// (int) Math.abs(p_SearchIMG[0] - p_TemplateIMG[0])
						//
						// + (int) (Math.abs(p_SearchIMG[1] - p_TemplateIMG[1])
						//
						// + (int) Math.abs(p_SearchIMG[2] - p_TemplateIMG[2]));
						// // System.out.println("i: " + match + i + " j: " +
						// j);
						// if (tempDist < dist) {
						// dist = tempDist;
						// bMatch = new Point(x, y);
						// }
						// if ((tempDist > 1000)) {
						//
						// match = false;
						// break;
						//
						// }

					}
				}
				SAD = (accum[0] / max + accum[1] / max + accum[2] / max) / 3;
				if (SAD < minSAD) {
					minSAD = SAD;
					bMatch = new Point(x, y);
				}
				if (SAD < 2) {
					minSAD = SAD;
					bMatch = new Point(x, y);
					match = true;
					break;
				}
				System.out.println(" dist: " + SAD);

			}
			if (match) {
				break;
			}

		}
		System.out.println("Match: " + match + " x: " + x + " y: " + y + " dist: " + SAD + " bMatch: " + bMatch
				+ " minSAD: " + minSAD);

	}

}
