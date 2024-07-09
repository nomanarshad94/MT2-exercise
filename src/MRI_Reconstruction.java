import ij.plugin.PlugIn;

public class MRI_Reconstruction implements PlugIn {
    @Override
    public void run(String arg0) {
        ComplexImage kSpace = Ex6_Helpers.loadKSpaceData();
        Ex6_Helpers.showImage(kSpace.getLogMagnitude(), "kspace");

        // Task1: Implement fftShift()  (3p)

        // Task2: Reconstruct the image from k-space (2p)

        // Task3: Convert the image back to k-space (1p)

        // Task4: Implement zeroFilling() and display the zero-filled k-space data (3p)

        // Task5: Do the same as Task1 and Task2 but with zero-filled k-space (1p)

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

        return output;
    }

    public ComplexImage zeroFilling(ComplexImage input){
        int boxSize = 96;
        
        float[][] tmpReal = input.realPart;
        float[][] tmpImag = input.imagPart;
        // Your codes here ...

        return new ComplexImage(tmpReal, tmpImag);
    }
}
