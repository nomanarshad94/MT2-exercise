import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class Bilateral_Filter implements PlugInFilter {

	/*--------------------------------------*
	 *  		Enter your code here.		* 
	 *	Define size, sigma_r and sigma_s	*
	 *--------------------------------------*/
	private int size;
	private double sigma_r;
	private double sigma_s;

	private double gauss(double sigma, double val) {
		/*--------------------------------------*
		 *  		Enter your code here.		* 
		 *--------------------------------------*/
		return Math.exp(-0.5 * Math.pow(val/sigma, 2));
	}

	// 9P fuer ganze Funktion
	public float getBFValue(float[][] bildwerte, int xx, int yy) {
		/*--------------------------------------*
		 *  		Enter your code here.		* 
		 *--------------------------------------*/
		int width = bildwerte.length;
		int height = bildwerte[0].length;
		double sum = 0;
		double sumWeight = 0;
		int r = size/2;
		for (int i = -r; i <= r; i++) { // -size to size
			for (int j = -r; j <= r; j++) {
				int x = xx + i;
				int y = yy + j;
				if (x >= 0 && x < width && y >= 0 && y < height) {
					double weight = gauss(this.sigma_s, Math.sqrt(i*i + j*j)) * gauss(this.sigma_r, bildwerte[x][y] - bildwerte[xx][yy]);
					sum+= weight * bildwerte[x][y];
					sumWeight += weight;
				}
			}
		}
		return (float) (sum / sumWeight);
	}

	@Override
	public void run(ImageProcessor ip) {
		/*--------------------------------------*
		 *  		Enter your code here.		* 
		 *--------------------------------------*/
		GenericDialog gd = new GenericDialog("Bilateral Filter");
		gd.addNumericField("Size", 5, 0);
		gd.addNumericField("Sigma_r", 25, 0);
		gd.addNumericField("Sigma_s", 10, 0);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		this.size = (int) gd.getNextNumber();
		this.sigma_r = gd.getNextNumber();
		this.sigma_s = gd.getNextNumber();
		float[][] ipFloat = ip.convertToFloatProcessor().getFloatArray();
		int width = ipFloat.length;
		int height = ipFloat[0].length;
		float[][] newPixels = new float[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				newPixels[i][j] = getBFValue(ipFloat, i, j);
			}
		}
		ImageProcessor newIp = new FloatProcessor(newPixels);
		ImagePlus f = new ImagePlus("Filtered Image", newIp);
		f.show();

	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		return DOES_8G;
	}

}
