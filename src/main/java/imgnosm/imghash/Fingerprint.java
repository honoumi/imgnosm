package imgnosm.imghash;

import java.awt.image.BufferedImage;
import java.math.BigInteger;


public class Fingerprint {
	 
	public static final int FWIDTH = 8;
	 
	public static final int FHEIGHT = 8;
	 
	public static String getFingerprint(String srcPath) {
		BufferedImage img = ImageDigital.readImg(srcPath);
		int w = img.getWidth();
		int h = img.getHeight();
		int pix[] = new int[w * h];
		pix = img.getRGB(0, 0, w, h, pix, 0, w);
		long l = getFingerprint(pix, w, h);
		StringBuilder sb = new StringBuilder(Long.toHexString(l));
		if(sb.length() < 16) {
			int n = 16-sb.length();
			for(int i=0; i<n; i++) {
				sb.insert(0, "0");
			}
		}
	
		return sb.toString();
	}
	 
	public static long getFingerprint(int[] pix, int w, int h) {
		pix = AmplificatingShrinking.shrink(pix, w, h, FWIDTH, FHEIGHT);
		int[] newpix = ImageDigital.grayImage(pix, FWIDTH, FHEIGHT);
		int avrPix = averageGray(newpix, FWIDTH, FHEIGHT);
	
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<FWIDTH*FHEIGHT; i++) {
			if(newpix[i] >= avrPix) {
				sb.append("1");
			
			} else {
				sb.append("0");
			
			}
		
		}
	
		long result = 0;
		if(sb.charAt(0) == '0') {
			result = Long.parseLong(sb.toString(), 2);
		} else {
		
			result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
		}

		return result;
	}
	 
	private static int averageGray(int[] pix, int w, int h) {
		int sum = 0;
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				sum = sum+pix[i*w + j];
			}

		}
		return (int)(sum/(w*h));
	}
	 
	public static int hammingDistance(String s1, String s2) {
		int count = 0;
		for(int i=0; i<s1.length(); i++) {
			if(s1.charAt(i) != s2.charAt(i)) {
				count ++;
			}
		}
		return count;
	}
	 
	public static String getFingerprintPhash(String srcPath) {
		BufferedImage img = ImageDigital.readImg(srcPath);
		int w = img.getWidth();
		int h = img.getHeight();
		int pix[] = new int[w * h];
		pix = img.getRGB(0, 0, w, h, pix, 0, w);

		pix = AmplificatingShrinking.shrink(pix, w, h, 32, 32);
		pix = ImageDigital.grayImage(pix, 32, 32);
		Transformation tf = new Transformation();
		int[] dctPix = tf.DCT(pix, 32);
		int avrPix = averageGray(dctPix, FWIDTH, FHEIGHT);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<FHEIGHT; i++) {
			for(int j=0; j<FWIDTH; j++) {
				if(dctPix[i*FWIDTH + j] >= avrPix) {
					sb.append("1");
				} else {
					sb.append("0");
				}
			}
		}
		//System.out.println(sb.toString());
		long result = 0;
		if(sb.charAt(0) == '0') {
			result = Long.parseLong(sb.toString(), 2);
		} else {

			result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
		}

		sb = new StringBuilder(Long.toHexString(result));
		if(sb.length() < 16) {
			int n = 16-sb.length();
			for(int i=0; i<n; i++) {
				sb.insert(0, "0");
			}
		}
		return sb.toString();
	}


}

 
