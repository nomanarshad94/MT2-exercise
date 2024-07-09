import ij.process.FHT;
import ij.process.FloatProcessor;

public class ComplexImage {
    protected float[][] realPart;
    protected float[][] imagPart;
    protected int height;
    protected int width;

    // Constructor
    public ComplexImage(float[][] realPart, float[][] imagPart) {
        this.realPart = realPart;
        this.imagPart = imagPart;
        this.height = realPart.length;
        this.width = realPart[0].length;
    }

    // Getters and setters
    public float[][] getRealPart() {
        return realPart;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public void setRealPart(float[][] realPart) {
        this.realPart = realPart;
    }

    public float[][] getImagPart() {
        return imagPart;
    }

    public void setImagPart(float[][] imagPart) {
        this.imagPart = imagPart;
    }

    public float[] getMagnitude(){
        return calculateMagnitude(false);
    }

    public float[] getLogMagnitude(){
        return calculateMagnitude(true);
    }
    private float[] calculateMagnitude(boolean logFlag) {
        float[] magnitude = new float[height * width];

        for (int x = 0; x < height; ++x) {
            for (int y = 0; y < width; ++y) {
                var tmp = Math.sqrt(Math.pow(this.getRealPart()[y][x], 2) + Math.pow(this.getImagPart()[y][x],2));
                if (tmp == 0) tmp = 1e-11;
                if (logFlag) tmp = Math.log10(tmp);
                magnitude[x*height + y] = (float) tmp;
            }
        }
        return magnitude;
    }
}
