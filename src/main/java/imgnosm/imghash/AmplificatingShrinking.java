package imgnosm.imghash;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class AmplificatingShrinking {

	 
	public static void bilinearityInterpolation(String srcPath, String distPath,
			String formatName, float k1, float k2) {
		BufferedImage img = ImageDigital.readImg(srcPath);
		BufferedImage imgOut = bilinearityInterpolation(img, k1, k2);
		ImageDigital.writeImg(imgOut, formatName, distPath);
	}
	 
	public static BufferedImage bilinearityInterpolation(BufferedImage img, float k1, float k2) {
		if (k1 < 1 || k2 < 1) {
			System.err
					.println("this is shrink image funcation, please set k1<=1 and k2<=1��");
			return null;
		}
		float ii = 1 / k1; 
		float jj = (1 / k2); 
		int dd = (int) (ii * jj);
		// int m=0 , n=0;
		int imgType = img.getType();
		int w = img.getWidth(); 
		int h = img.getHeight();
		int m = Math.round(k1 * w); 
		int n = Math.round(k2 * h); 
		int[] pix = new int[w * h];
		pix = img.getRGB(0, 0, w, h, pix, 0, w);
		 
		int[] newpix = new int[m * n];

		for (int j = 0; j < h - 1; j++) {
			for (int i = 0; i < w - 1; i++) {
				int x0 = Math.round(i * k1);
				int y0 = Math.round(j * k2);
				int x1, y1;
				if (i == w - 2) {
					x1 = m - 1;
				} else {
					x1 = Math.round((i + 1) * k1);
				}
				if (j == h - 2) {
					y1 = n - 1;
				} else {
					y1 = Math.round((j + 1) * k2);
				}
				int d1 = x1 - x0;
				int d2 = y1 - y0;
				if (0 == newpix[y0 * m + x0]) {
					newpix[y0 * m + x0] = pix[j * w + i];
				}
				if (0 == newpix[y0 * m + x1]) {
					if (i == w - 2) {
						newpix[y0 * m + x1] = pix[j * w + w - 1];
					} else {
						newpix[y0 * m + x1] = pix[j * w + i + 1];
					}
				}
				if (0 == newpix[y1 * m + x0]) {
					if (j == h - 2) {
						newpix[y1 * m + x0] = pix[(h - 1) * w + i];
					} else {
						newpix[y1 * m + x0] = pix[(j + 1) * w + i];
					}
				}
				if (0 == newpix[y1 * m + x1]) {
					if (i == w - 2 && j == h - 2) {
						newpix[y1 * m + x1] = pix[(h - 1) * w + w - 1];
					} else {
						newpix[y1 * m + x1] = pix[(j + 1) * w + i + 1];
					}
				}
				int r, g, b;
				float c;
				ColorModel cm = ColorModel.getRGBdefault();
				for (int l = 0; l < d2; l++) {
					for (int k = 0; k < d1; k++) {
						if (0 == l) {
				
							if (j < h - 1 && newpix[y0 * m + x0 + k] == 0) {
								c = (float) k / d1;
								r = cm.getRed(newpix[y0 * m + x0])
										+ (int) (c * (cm.getRed(newpix[y0 * m
												+ x1]) - cm.getRed(newpix[y0
												* m + x0])));// newpix[(y0+l)*m
																// + k]
								g = cm.getGreen(newpix[y0 * m + x0])
										+ (int) (c * (cm.getGreen(newpix[y0 * m
												+ x1]) - cm.getGreen(newpix[y0
												* m + x0])));
								b = cm.getBlue(newpix[y0 * m + x0])
										+ (int) (c * (cm.getBlue(newpix[y0 * m
												+ x1]) - cm.getBlue(newpix[y0
												* m + x0])));
								newpix[y0 * m + x0 + k] = new Color(r, g, b)
										.getRGB();
							}
							if (j + 1 < h && newpix[y1 * m + x0 + k] == 0) {
								c = (float) k / d1;
								r = cm.getRed(newpix[y1 * m + x0])
										+ (int) (c * (cm.getRed(newpix[y1 * m
												+ x1]) - cm.getRed(newpix[y1
												* m + x0])));
								g = cm.getGreen(newpix[y1 * m + x0])
										+ (int) (c * (cm.getGreen(newpix[y1 * m
												+ x1]) - cm.getGreen(newpix[y1
												* m + x0])));
								b = cm.getBlue(newpix[y1 * m + x0])
										+ (int) (c * (cm.getBlue(newpix[y1 * m
												+ x1]) - cm.getBlue(newpix[y1
												* m + x0])));
								newpix[y1 * m + x0 + k] = new Color(r, g, b)
										.getRGB();
							}
							
						} else {
					
							c = (float) l / d2;
							r = cm.getRed(newpix[y0 * m + x0 + k])
									+ (int) (c * (cm.getRed(newpix[y1 * m + x0
											+ k]) - cm.getRed(newpix[y0 * m
											+ x0 + k])));
							g = cm.getGreen(newpix[y0 * m + x0 + k])
									+ (int) (c * (cm.getGreen(newpix[y1 * m
											+ x0 + k]) - cm.getGreen(newpix[y0
											* m + x0 + k])));
							b = cm.getBlue(newpix[y0 * m + x0 + k])
									+ (int) (c * (cm.getBlue(newpix[y1 * m + x0
											+ k]) - cm.getBlue(newpix[y0 * m
											+ x0 + k])));
							newpix[(y0 + l) * m + x0 + k] = new Color(r, g, b)
									.getRGB();
						
						}
					}
					if (i == w - 2 || l == d2 - 1) { 
						c = (float) l / d2;
						r = cm.getRed(newpix[y0 * m + x1])
								+ (int) (c * (cm.getRed(newpix[y1 * m + x1]) - cm
										.getRed(newpix[y0 * m + x1])));
						g = cm.getGreen(newpix[y0 * m + x1])
								+ (int) (c * (cm.getGreen(newpix[y1 * m + x1]) - cm
										.getGreen(newpix[y0 * m + x1])));
						b = cm.getBlue(newpix[y0 * m + x1])
								+ (int) (c * (cm.getBlue(newpix[y1 * m + x1]) - cm
										.getBlue(newpix[y0 * m + x1])));
						newpix[(y0 + l) * m + x1] = new Color(r, g, b).getRGB();
					}
				}
			}
		}
		 
		BufferedImage imgOut = new BufferedImage(m, n, imgType);

		imgOut.setRGB(0, 0, m, n, newpix, 0, m);
		return imgOut;
	}

	 
	 

	 
	public static BufferedImage flex(BufferedImage img, float k1, float k2) {
		float ii = 1 / k1; 
		float jj = 1 / k2; 
	
		int imgType = img.getType();
		int w = img.getWidth();
		int h = img.getHeight();
		int m = (int) (k1 * w);
		int n = (int) (k2 * h);
		int[] pix = new int[w * h];
		pix = img.getRGB(0, 0, w, h, pix, 0, w);
		System.out.println(w + " * " + h);
		System.out.println(m + " * " + n);
		int[] newpix = new int[m * n];

		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				newpix[j * m + i] = pix[(int) (jj * j) * w + (int) (ii * i)];
			}
		}
		System.out.println((int) ((m - 1) * ii));
		System.out.println("m:" + m + " n:" + n);

		BufferedImage imgOut = new BufferedImage(m, n, imgType);

		imgOut.setRGB(0, 0, m, n, newpix, 0, m);
		return imgOut;
	}

	 
	public static BufferedImage flex(BufferedImage img, int m, int n) {
		float k1 = (float) m / img.getWidth();
		float k2 = (float) n / img.getHeight();
		return flex(img, k1, k2);
	}
	 
	public static void filexIsometry(String srcPath, String distPath,
			String formatName, int m, int n) {
		BufferedImage img = ImageDigital.readImg(srcPath);
		BufferedImage imgOut = flex(img, m, n);
		ImageDigital.writeImg(imgOut, formatName, distPath);
	}

	 
	public static BufferedImage shrink(BufferedImage img, float k1, float k2) {
		if (k1 > 1 || k2 > 1) {
			System.err
					.println("this is shrink image funcation, please set k1<=1 and k2<=1��");
			return null;
		}
		float ii = 1 / k1;
		float jj = 1 / k2;
		int dd = (int) (ii * jj);

		int imgType = img.getType();
		int w = img.getWidth();
		int h = img.getHeight();
		int m = Math.round(k1 * w);
		int n = Math.round(k2 * h);
		int[] pix = new int[w * h];
		pix = img.getRGB(0, 0, w, h, pix, 0, w);
		System.out.println(w + " * " + h);
		System.out.println(m + " * " + n);
		int[] newpix = shrink(pix, w, h, m, n);

		 

		BufferedImage imgOut = new BufferedImage(m, n, imgType);

		imgOut.setRGB(0, 0, m, n, newpix, 0, m);
		return imgOut;
	}
	 
	public static int[] shrink(int[] pix, int w, int h, int m, int n) {
		float k1 = (float) m / w;
		float k2 = (float) n / h;
		int ii = (int)(1 / k1); 
		int jj = (int)(1 / k2); 
		int dd = ii * jj;

		int[] newpix = new int[m * n];

		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				int r = 0, g = 0, b = 0;
				ColorModel cm = ColorModel.getRGBdefault();
				for (int k = 0; k <  jj; k++) {
					for (int l = 0; l <  ii; l++) {
						r = r
								+ cm.getRed(pix[(jj * j + k) * w
										+  (ii * i + l)]);
						g = g
								+ cm.getGreen(pix[(jj * j + k) * w
										+  (ii * i + l)]);
						b = b
								+ cm.getBlue(pix[ (jj * j + k) * w
										+  (ii * i + l)]);
					}
				}
				r = r / dd;
				g = g / dd;
				b = b / dd;
				newpix[j * m + i] = 255 << 24 | r << 16 | g << 8 | b;

			}
		}
		return newpix;
	}

	 
	public static BufferedImage shrink(BufferedImage img, int m, int n) {
		float k1 = (float) m / img.getWidth();
		float k2 = (float) n / img.getHeight();
		return shrink(img, k1, k2);
	}

}
