import ij.*;
import ij.io.*;
import ij.plugin.PlugIn;
import ij.process.*;

/*
Name: Noman Arshad
Group: Noman Arshad & Taimoor Ajmal
*/

public class First_Plugin implements PlugIn {
	/*
	Convert from ColorProcessor to ByteProcessor
	*/
	private ByteProcessor convertToGrayscale(ImageProcessor cp) {
		TypeConverter tc = new TypeConverter(cp, false);
		return (ByteProcessor)tc.convertToByte();
	}

	/*
	Replace the current image
	*/
	private void showImage(ByteProcessor bp, String title) {
		ImagePlus f = new ImagePlus(title, bp);
		f.show();
	}

	public void run(String arg) {

		// Your code here
		ImagePlus plus = IJ.openImage();
		ImageProcessor processor = plus.getProcessor();
		ByteProcessor bp = convertToGrayscale(processor);
		showImage(bp, "First Plugin");
		IJ.showMessage("My First Plugin MSG!!");

	}

}
