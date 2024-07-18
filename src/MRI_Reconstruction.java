import ij.plugin.PlugIn;

public class MRI_Reconstruction implements PlugIn {
    @Override
    public void run(String arg0) {
        ComplexImage kSpace = Ex6_Helpers.loadKSpaceData();
        Ex6_Helpers.showImage(kSpace.getLogMagnitude(), "kspace");

        // Task1: Implement fftShift()  (3p)
        ComplexImage result = fftShift(kSpace);
        // Showing the kspace shifted image in the log scale to compare results with provided image in exercise pdf
        Ex6_Helpers.showImage(result.getLogMagnitude(), "kSpacefftshift");

        // Task2: Reconstruct the image from k-space (2p)
        ComplexImage image = Ex6_Helpers.InverseFFT2D(result);
        image = fftShift(image);
        Ex6_Helpers.showImage(image.getMagnitude(), "image");
        // Task3: Convert the image back to k-space (1p)
        ComplexImage kSpace2 = Ex6_Helpers.FFT2D(image);
        kSpace2 = fftShift(kSpace2);
        Ex6_Helpers.showImage(kSpace2.getLogMagnitude(), "kspace2");
        // Task4: Implement zeroFilling() and display the zero-filled k-space data (3p)
        ComplexImage zeroFilledKSpace = zeroFilling(kSpace);
        Ex6_Helpers.showImage(zeroFilledKSpace.getLogMagnitude(), "zeroFilledKSpace");
        // Task5: Do the same as Task1 and Task2 but with zero-filled k-space (1p)
        ComplexImage result2 = fftShift(zeroFilledKSpace);
        // Showing the zero filled kspace shifted image in the log scale to compare results with provided image in exercise pdf
        Ex6_Helpers.showImage(result2.getLogMagnitude(), "kSpacefftshift2");
        ComplexImage image2 = Ex6_Helpers.InverseFFT2D(result2);
        image2 = fftShift(image2);
        Ex6_Helpers.showImage(image2.getMagnitude(), "image2");

    }
    public ComplexImage fftShift(ComplexImage input) {
        ComplexImage output = new ComplexImage(new float[input.height][input.width], new float[input.height][input.width]);
        output.realPart =  swapQuadrants(input.realPart);
        output.imagPart = swapQuadrants(input.imagPart);

        return output;
    }
    private float[][] swapQuadrants(float[][] input) {
        float[][] output = new float[input.length][input[0].length];
        // Your codes here ...
        int halfHeight = input.length / 2;
        int halfWidth = input[0].length / 2;
        for (int i = 0; i < halfHeight; i++) {
            for (int j = 0; j < halfWidth; j++) {
                // swap the first quadrant with the third
                output[i][j] = input[i + halfHeight][j + halfWidth];
                output[i + halfHeight][j + halfWidth] = input[i][j];
                // swap the second quadrant with the fourth
                output[i][j + halfWidth] = input[i + halfHeight][j];
                output[i + halfHeight][j] = input[i][j + halfWidth];
            }
        }

        return output;
    }


    public ComplexImage zeroFilling(ComplexImage input){
        int boxSize = 96;
        
        float[][] tmpReal = input.realPart;
        float[][] tmpImag = input.imagPart;
        // Your codes here ...
        for (int i = 0; i < input.height; i++) {
            for (int j = 0; j < input.width; j++) {
                // set the values outside the boxSize to zero in all quadrants
                if ((i < ((input.height / 2) - (boxSize / 2))) || (i >= ((input.height / 2) + (boxSize / 2))) ||
                        (j < ((input.width / 2) - (boxSize / 2))) || (j >= ((input.width / 2) + (boxSize / 2)))) {
                    tmpReal[i][j] = 0;
                    tmpImag[i][j] = 0;
                }
            }
        }

        return new ComplexImage(tmpReal, tmpImag);
    }
}
