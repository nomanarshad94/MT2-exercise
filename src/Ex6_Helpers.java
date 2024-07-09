import ij.ImagePlus;
import ij.measure.Calibration;
import ij.process.FHT;
import ij.process.FloatProcessor;
import io.jhdf.HdfFile;
import io.jhdf.api.Dataset;

import java.io.File;
import java.util.Arrays;

public class Ex6_Helpers {
    public static ComplexImage loadKSpaceData() {
        File file = new File("src/kdata_brain.h5");
        try (HdfFile hdfFile = new HdfFile(file)) {
            Dataset realDataset = hdfFile.getDatasetByPath("kspace_realpart");
            Object realObject = realDataset.getData();
            Dataset imagDataset = hdfFile.getDatasetByPath("kspace_imagpart");
            Object imagObject = imagDataset.getData();
            return new ComplexImage((float[][]) realObject, (float[][]) imagObject);
        }
    }
    public static void showImage(float[] buffer, String title) {
        // Find min and max values of the buffer array
        float minValue = Float.POSITIVE_INFINITY;
        float maxValue = Float.NEGATIVE_INFINITY;
        for (float value : buffer) {
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        FloatProcessor processor = new FloatProcessor(256, 256, buffer);
        ImagePlus plus = new ImagePlus();
        plus.setProcessor(title, processor);
        processor.setMinAndMax(minValue, maxValue);

        plus.show();
        ij.IJ.run("Tile");
    }
    public static ComplexImage InverseFFT2D(ComplexImage input) {
        return complexFFT(input, true);
    }
    public static ComplexImage FFT2D(ComplexImage input) {
        return complexFFT(input, false);
    }

    private static ComplexImage complexFFT(ComplexImage input, boolean inverseTransform) {
        float[] tmpRe = new float[input.width * input.height];
        float[] tmpIm = new float[input.width * input.height];
        if (input.width != input.height) {
            throw new RuntimeException("Our implementation only works for image width equal to image height.");
        }
        var N = input.width;
        c2c2DFFT(flatten(input.realPart), flatten(input.imagPart), N, tmpRe, tmpIm);

        //That's some dirty forward transforming (reversing array order of inverse transform and adding some scaling)
        if (!inverseTransform) {
            var arrLength = tmpRe.length;
            for (int i = 0; i < arrLength / 2; i++) {
                float temp = tmpRe[i];
                tmpRe[i] = tmpRe[arrLength - i - 1] * (float) arrLength;
                tmpRe[arrLength - i - 1] = temp * (float) arrLength;
                temp = tmpIm[i];
                tmpIm[i] = tmpIm[arrLength - i - 1] * (float) arrLength;
                tmpIm[arrLength - i - 1] = temp * (float) arrLength;
            }
        }
        float[][] real = reshape(tmpRe, input.height, input.width);
        float[][] imag = reshape(tmpIm, input.height, input.width);
        return new ComplexImage(real, imag);
    }

    /**
     * Complex to Complex Inverse Fourier Transform
     * Author: Joachim Wesner
     * Source: ImageJ FFT plugin
     */
    private static void c2c2DFFT(float[] rein, float[] imin, int maxN, float[] reout, float[] imout) {
        FHT fht = new FHT(new FloatProcessor(maxN, maxN));
        float[] fhtpixels = (float[]) fht.getPixels();
        // Real part of inverse transform
        for (int iy = 0; iy < maxN; iy++)
            cplxFHT(iy, maxN, rein, imin, false, fhtpixels);
        fht.inverseTransform();
        // Save intermediate result, so we can do a "in-place" transform
        float[] hlp = new float[maxN * maxN];
        System.arraycopy(fhtpixels, 0, hlp, 0, maxN * maxN);
        // Imaginary part of inverse transform
        for (int iy = 0; iy < maxN; iy++)
            cplxFHT(iy, maxN, rein, imin, true, fhtpixels);
        fht.inverseTransform();
        System.arraycopy(hlp, 0, reout, 0, maxN * maxN);
        System.arraycopy(fhtpixels, 0, imout, 0, maxN * maxN);
    }

    /**
     * Build FHT input for equivalent inverse FFT
     * Author: Joachim Wesner
     * Source: ImageJ FFT plugin
     */
    private static void cplxFHT(int row, int maxN, float[] re, float[] im, boolean reim, float[] fht) {
        int base = row * maxN;
        int offs = ((maxN - row) % maxN) * maxN;
        if (!reim) {
            for (int c = 0; c < maxN; c++) {
                int l = offs + (maxN - c) % maxN;
                fht[base + c] = ((re[base + c] + re[l]) - (im[base + c] - im[l])) * 0.5f;
            }
        } else {
            for (int c = 0; c < maxN; c++) {
                int l = offs + (maxN - c) % maxN;
                fht[base + c] = ((im[base + c] + im[l]) + (re[base + c] - re[l])) * 0.5f;
            }
        }
    }
    public static float[][] reshape(float[] vector, int numRows, int numCols) {
        if (vector.length != numRows * numCols) {
            throw new IllegalArgumentException("Invalid dimensions for reshaping");
        }

        float[][] array2D = new float[numRows][numCols];
        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                array2D[i][j] = vector[index++];
            }
        }
        return array2D;
    }

    // Function to flatten a 2D float array into a 1D vector
    public static float[] flatten(float[][] array2D) {
        int numRows = array2D.length;
        int numCols = array2D[0].length;
        float[] vector1D = new float[numRows * numCols];
        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                vector1D[index++] = array2D[i][j];
            }
        }
        return vector1D;
    }
}
