/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author vian
 */
public class Project_A {

    /**
     * @param args the command line arguments
     */
    static OutputStream outputStream;
    static BufferedImage image = null;
    
     static int x = 9;
    public static void main(String[] args) {
        /*
        String text = "text";

        // Generate QR code to output stream.
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	QRCode.from(text).to(ImageType.PNG).writeTo(output);
        
        
	// Create BufferedImage from input stream.
	byte[] data = output.toByteArray();
	ByteArrayInputStream input = new ByteArrayInputStream(data);
	
	try {
		image = ImageIO.read(input);
	} catch (IOException e) {
	e.printStackTrace();
	}
	
        */


      /*
      if(x == 0)
          System.out.println("x is 0");
      else
          if(x == 1)
              System.out.println("x is 1");
      else
              if(x == 2)
                  System.out.println("x is 2");
      else
                  if(x == 3)
                      System.out.println("x == 3");
      
      */
     
      
        System.out.println(RandomStringUtils.randomAlphanumeric(100));
      //  System.out.println(Double.MAX_VALUE);
    }

}
