import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.IJ;

import java.util.Arrays;

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
			{(float) Math.sqrt(2.0), 0.0f, 0.0f, 0.0f, (float) Math.sqrt(2.0), 0.0f, 0.0f, 0.0f, (float) Math.sqrt(2.0)},
			{0.0f, 0.0f, 0.0f, (float) (2.0*(Math.sqrt(2.0)-1.0)), 0.0f, 0.0f,(float) ((2.0-Math.sqrt(2.0))), (float) (2.0*(Math.sqrt(2.0)-1.0)),0.0f},
		};
		float[] p = new float[]{1.8f,0.8f,2.8f,1.0f,2.6f,1.8f,1.3f,4.0f,0.7f};
		float[] x = new float[9];
		for (int i = 0; i < 100; i++) {
			int k = i % 9;
			float denominator = scalarProduct(A[k], A[k]);
			float numerator = p[k]- scalarProduct(A[k],x);

			for (int j=0; j<x.length; j++) {
				x[j] = x[j] + (numerator / denominator) * A[k][j];
			}
		}

		IJ.showMessage(Arrays.toString(Arrays.copyOfRange(x, 0, 3))+ "\n"+ Arrays.toString(Arrays.copyOfRange(x, 3, 6))+ "\n"+Arrays.toString(Arrays.copyOfRange(x, 6, 9)));
		// Confused between ide console and Image J console so printing the output to both!
		System.out.println(Arrays.toString(Arrays.copyOfRange(x, 0, 3))+ "\n"+ Arrays.toString(Arrays.copyOfRange(x, 3, 6))+ "\n"+Arrays.toString(Arrays.copyOfRange(x, 6, 9)));
	}

	private float scalarProduct(float[] a, float[] b) {
		assert a.length == b.length;
		float result = 0.0f;
		for (int i = 0; i < a.length; i++) {
			result += a[i] * b[i];
		}
		return result;
	}
}
