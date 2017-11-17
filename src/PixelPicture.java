import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PixelPicture 
{
	private BufferedImage picture;
	private int averageR, averageG, averageB;
	public PixelPicture(File file)
	{
		int totalRed = 0, totalBlue = 0, totalGreen = 0;
        try
        {
        	picture = ImageIO.read(file);
        	for(int x = 0; x < picture.getWidth(); x++)
        	{
        		for(int y = 0; y < picture.getHeight(); y++)
        		{
        			int rgb = picture.getRGB(x, y);
        			//int alpha = (rgb >> 24) & 0xFF;
        			int red =   (rgb >> 16) & 0xFF;
        			int green = (rgb >>  8) & 0xFF;
        			int blue =  (rgb      ) & 0xFF;
        			totalRed+=red;
        			totalBlue+=blue;
        			totalGreen+=green;
        		}
        	}
			System.out.print("Finished Loading " + file.getAbsolutePath());
			int totalPixels = picture.getWidth() * picture.getHeight();
			averageR = totalRed / totalPixels;
			averageG = totalBlue / totalPixels;
			averageB = totalGreen / totalPixels;
			System.out.println("\tAverage RGB " + averageR +","+averageG+","+averageB);
        } 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
	}
	public PixelPicture(File file, int x, int y)
	{
		this(file);
		BufferedImage scaledImage = new BufferedImage(x, y, picture.getType());
		Graphics2D scaledImageGraphics = scaledImage.createGraphics();
		AffineTransform affineTransform = AffineTransform.getScaleInstance((double)x/picture.getWidth(), (double)y/picture.getHeight());
		scaledImageGraphics.drawRenderedImage(picture, affineTransform);
		this.picture = scaledImage;
	}
	public BufferedImage getPicture()
	{
		return picture;
	}
	public int getAverageRed()
	{
		return averageR;
	}
	public int getAverageGreen()
	{
		return averageG;
	}
	public int getAverageBlue()
	{
		return averageB;
	}
}
