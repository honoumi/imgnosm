package imgnosm.imghash;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageDigital {


	
	public static double averageGray(int[] pix, int w, int h) {
		double sum = 0;
		ColorModel cm = ColorModel.getRGBdefault();
		for (int j = 0; j < h; j++)
			for (int i = 0; i < w; i++)
				sum = sum + cm.getRed(pix[i + j * w]);
		double av = sum / (w * h);
		return av;
	}

	
	public static void grayImage(String srcPath, String destPath) {
		OutputStream output = null;
		try {
			 			
			BufferedImage img = ImageIO.read(new File(srcPath));
			int imageType = img.getType();
			int w = img.getWidth();
			int h = img.getHeight();

			 			int[] rgbArray = new int[w*h];
			 			int[] newArray = new int[w*h];
			img.getRGB(0, 0, w, h, rgbArray, 0, w);
			rgbArray = grayImage(rgbArray, w, h);
			 			int gray, rgb;
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					
					gray = rgbArray[i*w + j];

					rgb = 255<<24 | gray<<16 | gray<<8 | gray;
					newArray[i * w + j] = cm.getRGB(rgb);  				}
			}
			 			File out = new File(destPath);
			if (!out.exists())
				out.createNewFile();
			output = new FileOutputStream(out);
			BufferedImage imgOut = new BufferedImage(w, h,
					BufferedImage.TYPE_3BYTE_BGR);
			imgOut.setRGB(0, 0, w, h, newArray, 0, w);
			ImageIO.write(imgOut, "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
				}
		}
	}
	
	public static int[] grayImage(int pix[], int w, int h) {
		 		ColorModel cm = ColorModel.getRGBdefault();
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				 				pix[i*w + j] = (int) (0.3*cm.getRed(pix[i*w + j]) + 0.58*cm.getGreen(pix[i*w + j]) + 0.12*cm.getBlue(pix[i*w + j]) );
			}
		}
		return pix;
	}

	
	public static BufferedImage readImg(String srcPath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(srcPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}



	
	public static int[] thinning(int pix[], int w, int h) {
		int[] tempPixs = new int[9];
		int count = 0;
		ColorModel cm = ColorModel.getRGBdefault();
		int[] newpix = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
					count = 0;
					tempPixs[0] = cm.getRed(pix[x + (y) * w]);
					tempPixs[1] = cm.getRed(pix[x + 1 + (y) * w]);
					tempPixs[2] = cm.getRed(pix[x + 1 + (y - 1) * w]);
					tempPixs[3] = cm.getRed(pix[x + (y - 1) * w]);
					tempPixs[4] = cm.getRed(pix[x - 1 + (y - 1) * w]);
					tempPixs[5] = cm.getRed(pix[x - 1 + (y) * w]);
					tempPixs[6] = cm.getRed(pix[x - 1 + (y + 1) * w]);
					tempPixs[7] = cm.getRed(pix[x + (y + 1) * w]);
					tempPixs[8] = cm.getRed(pix[x + 1 + (y + 1) * w]);
					for (int k = 0; k < tempPixs.length; k++) {
						if (tempPixs[k] == 0)
							count++;
					}
					 					if (count >= 2
							&& count <= 6
							&& tempPixs[0] == 0
							&& (tempPixs[1] * tempPixs[3] * tempPixs[7] == 0 || tempPixs[7] != 0)
							&& (tempPixs[3] * tempPixs[5] * tempPixs[7] == 0 || tempPixs[5] != 0)) {
						newpix[x + y * w] = 255 << 24 | 0 << 16 | 0 << 8 | 0;
					} else {
						newpix[x + y * w] = 255 << 24 | 255 << 16 | 255 << 8
								| 255;
					}
				} else {
					newpix[x + y * w] = 255 << 24 | 255 << 16 | 255 << 8 | 255;
				}
			}
		}
		return newpix;
	}

	
	public static void thinning(String srcPath, String destPath, String format) {
		BufferedImage img = readImg(srcPath);
		int w = img.getWidth();
		int h = img.getHeight();
		int[] pix = new int[w * h];
		img.getRGB(0, 0, w, h, pix, 0, w);
		int newpix[] = thinning(pix, w, h);
		img.setRGB(0, 0, w, h, newpix, 0, w);
		writeImg(img, format, destPath);
	}

	
	public static void writeImg(BufferedImage img, String formatName,
			String destPath) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(destPath);
			ImageIO.write(img, formatName, out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	

}
