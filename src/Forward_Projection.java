import ij.ImagePlus;
import ij.gui.Plot;
import ij.gui.WaitForUserDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.util.Arrays;

//		1. For θ = 0, what structures in the image correspond to the two peaks at s = 100 and s = 200?
//		Answer: The two peaks at s = 100 and s = 200 correspond to the eyes in the image.

//		2. For θ = 90, what structure does the peak between s = 0 and s = 20 correspond to?
// 		Answer: The peak between s = 0 and s = 20 corresponds to the bottom white space of the image.

//		3. For θ = 90, what structures in the image correspond to the two peaks at s = 100 and s = 200?
//		Answer: The two peaks at s = 100 and s = 200 correspond to the smile in the image.


public class Forward_Projection implements PlugInFilter {

	@Override
	public void run(ImageProcessor ip) {
		/*--------------------------------------*
		 *  		Enter your code here.		*
		 *--------------------------------------*/
		// Get the image dimensions
		int width = ip.getWidth();
		int height = ip.getHeight();
		//1. Create a double[] array to store the x values for the plot, and fill it
		//appropriate values based on the number of projections per rotation.
		double[] xValues = new double[width];
		double[] projection = new double[width]; // max of width or height?
		int total_angles = 18;
		int[] thetas = new int[total_angles + 1];
		for (int i = 0; i <= total_angles; i++) {
			thetas[i] = i * 10;
		}
		// Loop through each angle
		for (int i = 0; i < total_angles; i++) {
			// Get the current angle
			double theta = thetas[i];
			// Rotate the input image
			ImageProcessor rotated_ip = ip.duplicate();
			rotated_ip.rotate(theta);

			// Display the rotated image
			ImagePlus rotated_imp = new ImagePlus("Rotated Image at Angle "+ theta, rotated_ip);
			rotated_imp.show();
			// Initialize the projection array
            Arrays.fill(projection, 0);
			// Loop through each column
			for (int x = 0; x < width; x++) {
				// Loop through each row
				for (int y = 0; y < height; y++) {
					// Add the pixel value to the projection
					projection[x] += rotated_ip.getPixelValue(x, y);
				}
				// Set the x value
				xValues[x] = x;
			}
			// Create a new plot
			Plot plot = new Plot("Rotated Image with Angle "+ theta, "S", "Projection", xValues,projection);
			plot.show();
			new WaitForUserDialog("Close the plot window to continue.").show();
			// Close the plot window
			rotated_imp.close();
			plot.getImagePlus().close();
		}
	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		// TODO Auto-generated method stub
		return DOES_8G;
	}
}
