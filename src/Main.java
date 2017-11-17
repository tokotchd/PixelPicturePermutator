import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Main 
{
	public final static int PIXEL_PICTURE_WIDTH = 69/3;
	public final static int PIXEL_PICTURE_HEIGHT = 69/3;
	public final static int MAIN_PICTURE_WIDTH = 830/2;
	public final static int MAIN_PICTURE_HEIGHT = 710/2;
	
	public static void main(String[] args)
	{
		String mainPicturePath = args[0];
		String pixelPictureFolder = args[1];
		
		BufferedImage mainPicture;
		BufferedImage scaledMainPicture;
		BufferedImage finalPicture;
		ArrayList<PixelPicture> pixelPictures = new ArrayList<PixelPicture>();
		try 
		{
			mainPicture = ImageIO.read(new File(mainPicturePath));
			File folder = new File(pixelPictureFolder);
			File[] listOfFiles = folder.listFiles();
			for(File file: listOfFiles)
			{
				if(file.isFile())
				{
					//PixelPicture pp = new PixelPicture(file);
					PixelPicture pp = new PixelPicture(file, PIXEL_PICTURE_WIDTH, PIXEL_PICTURE_HEIGHT);
					pixelPictures.add(pp);
				}
			}
			//once we have all of the pixelPictures... we have to scale the mainPicture
			scaledMainPicture = new BufferedImage(MAIN_PICTURE_WIDTH, MAIN_PICTURE_HEIGHT, mainPicture.getType());
			Graphics2D scaledImageGraphics = scaledMainPicture.createGraphics();
			AffineTransform affineTransform = AffineTransform.getScaleInstance((double)MAIN_PICTURE_WIDTH/mainPicture.getWidth(), (double)MAIN_PICTURE_HEIGHT/mainPicture.getHeight());
			scaledImageGraphics.drawRenderedImage(mainPicture, affineTransform);
			
			//now we do replacement
			finalPicture = new BufferedImage(scaledMainPicture.getWidth() * PIXEL_PICTURE_WIDTH, scaledMainPicture.getHeight() * PIXEL_PICTURE_HEIGHT, scaledMainPicture.getType());
			Graphics2D finalGraphics = finalPicture.createGraphics();
			for(int x = 0; x < scaledMainPicture.getWidth(); x++)
			{
				for(int y = 0; y < scaledMainPicture.getHeight(); y++)
				{
					int rgb = scaledMainPicture.getRGB(x, y);
        			//int alpha = (rgb >> 24) & 0xFF;
        			int red =   (rgb >> 16) & 0xFF;
        			int green = (rgb >>  8) & 0xFF;
        			int blue =  (rgb      ) & 0xFF;
        			PixelPicture closestPicture = pixelPictures.get(0);
    				double closestColorDistance = Math.sqrt(Math.pow(red - closestPicture.getAverageRed(), 2) + Math.pow(green - closestPicture.getAverageGreen(), 2) + Math.pow(blue - closestPicture.getAverageBlue(), 2));
        			for(int counter = 1; counter < pixelPictures.size(); counter++)
        			{
        				PixelPicture thisPicture = pixelPictures.get(counter);
        				double thisColorDistance = Math.sqrt(Math.pow(red - thisPicture.getAverageRed(), 2) + Math.pow(green - thisPicture.getAverageGreen(), 2) + Math.pow(blue - thisPicture.getAverageBlue(), 2));
        				if(thisColorDistance < closestColorDistance)
        				{
        					closestPicture = thisPicture;
        					closestColorDistance = thisColorDistance;
        				}
        			}
        			//once we have the closest pixelPicture, we just paste it in.
        			finalGraphics.drawImage(closestPicture.getPicture(), x * PIXEL_PICTURE_WIDTH, y * PIXEL_PICTURE_HEIGHT, null);
				}
			}
			File outputFile = new File(mainPicturePath + "-final.png");
			ImageIO.write(finalPicture, "png", outputFile);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
