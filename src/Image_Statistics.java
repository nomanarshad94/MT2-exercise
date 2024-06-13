import java.awt.Rectangle;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.filter.PlugInFilter;
import ij.process.Blitter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;


/**
 * Plugin to compute image statistics from an image or region of interest
 */
public class Image_Statistics implements PlugInFilter {

	/**
	 * @inheritDoc
	 *
	 * Computes mean and standard deviation from an image (or region of 
	 * interest if selected) and saves it in a result-table
	 */
	@Override
	public void run(ImageProcessor ip) {
		/*--------------------------------------*
		 *  Fuegen Sie ab hier Ihren Code ein.  * 
		 *--------------------------------------*/

		// compute mean and standard deviation
		double mean = 0.0;
		double stdDev = 0.0;
//		double mean_test = ip.getStatistics().mean;
//		double std_test = ip.getStatistics().stdDev;
		int height = ip.getHeight();
		int width = ip.getWidth();
//		// compute mean
//		for(int i = 0; i < height; i++){
//			for(int j = 0; j < width; j++){
//				mean += ip.getPixel(i,j);
//			}
//		}
//		mean *= 1.0/(width*height);
//		// compute standard deviation
//		for(int i = 0; i < height; i++){
//			for(int j = 0; j < width; j++){
//				stdDev += Math.pow(ip.getPixel(i,j)-mean,2);
//			}
//		}
//		stdDev = Math.sqrt(stdDev/(width*height));
//		IJ.showMessage(String.valueOf(mean));
//		IJ.showMessage(String.valueOf(stdDev));
//		IJ.showMessage(String.valueOf(mean_test));
//		IJ.showMessage(String.valueOf(std_test));
		// ROI based statistics
		ByteProcessor mask = getFullMask(ip);
		double meanROI = 0.0;
		double stdDevROI = 0.0;
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				if(mask.getPixel(i,j) == 255){
					meanROI += ip.getPixel(i,j);
				}
			}
		}
		int heightROI = mask.getHeight();
		int widthROI = mask.getWidth();
		meanROI *= 1.0/(heightROI*widthROI);
		// compute standard deviation ROI based
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				if(mask.getPixel(i,j) == 255){
					stdDevROI += Math.pow(ip.getPixel(i,j)-meanROI,2);
				}
			}
		}
		stdDevROI = Math.sqrt(stdDevROI/(heightROI*widthROI));

		mean = meanROI;
		stdDev = stdDevROI;


		/*-------------------------------------------------------*
		 *  Hier endet Ihr Code, bitte danach nichts verändern.  *
		 *-------------------------------------------------------*/
		
		// add mean and standard deviation to our results-table
		// and show it	
		outputTable.incrementCounter();
		outputTable.addValue("Mean", mean);
		outputTable.addValue("StdDev", stdDev);
		outputTable.show("Image Statistics");
	}

	/** 
	 * @inheritDoc
	 *
	 * Setup resultstable and limit to 8 bit grayscale image
	 *
	 */
	@Override
	public int setup(String arg0, ImagePlus arg1) {
		if (outputTable == null) {
			outputTable = new ResultsTable();
		}
		return DOES_8G;
	}
	
	/// This table is used to show the results.
	private static ResultsTable outputTable;
	
	/** 
	 * This method returns a mask image of the same size as ip. Pixels outside of 
	 * the region of interest (ROI) have value 0.	
	 *
	 * @param ip input image-processor from which the mask is generated
	 * @return Image buffer with the region of interest as mask, i.e. 255 set 
	 * 	everywhere in the ROI
	 */
	private ByteProcessor getFullMask(ImageProcessor ip) {
		// create new mask with same dimension as image
		ByteProcessor mask = new ByteProcessor(ip.getWidth(), ip.getHeight());
		// if we have a region of interest (which is not a rectangle)
		if (ip.getMask() != null) {
			ImageProcessor ipMask = ip.getMask();
			Rectangle roiRect = ip.getRoi();
			mask.copyBits(ipMask, roiRect.x, roiRect.y, Blitter.COPY);
		}
		// if nothing or a rectangle is selected as ROI then we need to 
		// create our mask on our own
		else {
			Rectangle roiRect = ip.getRoi();
			mask.setValue(255);
			Roi roi = new Roi(roiRect);
			mask.fill(roi);
		}
		return mask;
	}
}
