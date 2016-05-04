package imgnosm.imghash;

import java.awt.image.BufferedImage;

public class Transformation {

	public static final int N = 256;

	public int[] FFT() {

		return null;
	}

	public int[] DCT(int[] pix, int n) {
		double[][] iMatrix = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				iMatrix[i][j] = (double) (pix[i * n + j]);
			}
		}
		double[][] quotient = coefficient(n);
		double[][] quotientT = transposingMatrix(quotient, n);
		double[][] temp = new double[n][n];
		temp = matrixMultiply(quotient, iMatrix, n);
		iMatrix = matrixMultiply(temp, quotientT, n);

		int newpix[] = new int[n * n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				newpix[i * n + j] = (int) iMatrix[i][j];
			}
		}
		return newpix;
	}

	private double[][] transposingMatrix(double[][] matrix, int n) {
		double nMatrix[][] = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				nMatrix[i][j] = matrix[j][i];
			}
		}
		return nMatrix;
	}

	private double[][] coefficient(int n) {
		double[][] coeff = new double[n][n];
		double sqrt = 1.0 / Math.sqrt(n);
		for (int i = 0; i < n; i++) {
			coeff[0][i] = sqrt;
		}
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < n; j++) {
				coeff[i][j] = Math.sqrt(2.0 / n)
						* Math.cos(i * Math.PI * (j + 0.5) / (double) n);
			}
		}
		return coeff;
	}

	private double[][] matrixMultiply(double[][] A, double[][] B, int n) {
		double nMatrix[][] = new double[n][n];
		double t = 0.0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				t = 0;
				for (int k = 0; k < n; k++) {
					t += A[i][k] * B[k][j];
				}
				nMatrix[i][j] = t;
			}
		}
		return nMatrix;
	}

	public void dctTrans(String srcPath, String destPath, String format) {
		BufferedImage img = ImageDigital.readImg(srcPath);
		int w = img.getWidth();
		int h = img.getHeight();
		int[] pix = new int[w * h];
		img.getRGB(0, 0, w, h, pix, 0, w);
		pix = AmplificatingShrinking.shrink(pix, w, h, N, N);
		int newpix[] = ImageDigital.grayImage(pix, N, N);

		int dctPix[] = DCT(newpix, 256);
		for (int i = 0; i < N * N; i++) {
			newpix[i] = 255 << 24 | dctPix[i] << 16 | dctPix[i] << 8
					| dctPix[i];
		}
		img.setRGB(0, 0, w, h, newpix, 0, w);
		ImageDigital.writeImg(img, format, destPath);
	}

}
