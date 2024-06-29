import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;


public class Kaczmarz_Reconstruction implements PlugIn {
	
	@Override
	public void run(String arg0) {
		float[][] A = new float[][]{																								
			{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f},
			{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f},
			{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f},
			{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f},
			{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f},
			{1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
			{0.0f, (float) (2.0*(Math.sqrt(2.0)-1.0)), (float) ((2.0-Math.sqrt(2.0))), 0.0f, 0.0f, (float) (2.0*(Math.sqrt(2.0)-1.0)), 0.0f, 0.0f, 0.0f},
			// TODO: Complete matrix
		};
		
		// TODO: Implement method here
	}
}
