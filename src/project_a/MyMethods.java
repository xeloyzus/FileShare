/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.NoRouteToHostException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.rmi.ConnectIOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

/**
 *
 * @author vian
 */
public class MyMethods {
    
          //display file size in a readable format
    public  static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    
        //returns true is ip address is valid. Only ipV4
  public static boolean isValidIP(String ipAddr){   
        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }
  
     public static String GetAddress(String addressType){
       String address = "";
       InetAddress lanIp = null;
        try {
            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();
            while(net.hasMoreElements()){
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address){
                        if (ip.isSiteLocalAddress()){
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }
                    }
                }
            }

            if(lanIp == null) return null;

            if(addressType.equalsIgnoreCase("ip")){
                address = lanIp.toString().replaceAll("^/+", "");
            }else if(addressType.equalsIgnoreCase("mac")){
                address = GetMacAddress(lanIp);
            }else{
                throw new Exception("Specify \"ip\" or \"mac\"");
            }
        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        } catch (Exception e){

            e.printStackTrace();

        }

       return address;

   }
       
       
   private static String GetMacAddress(InetAddress ip){
       String address = null;
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
            }
            address = sb.toString();

        } catch (SocketException e) {

            e.printStackTrace();

        }

       return address;
   }
   
   
     //custom method to escape string so can be parsed by json
  public static String escapeJSON(String string){
       string = StringUtils.replace(string, "/", "//");  
       string = StringUtils.replace(string, "\\", "\\\\");
       
       return string;
  }
  
  //remove the extra characters that protect JSON parsing from crashing so that we get the original string path. 
  public static String unEscapeJSON(String path){
        path = StringUtils.replace(path, "//", "/");
         
        path = StringUtils.replace(path, "\\\\", "\\");
        
        return path;
  }
   
  //return an image icon using buffered image size
   public static ImageIcon imageIcon_From_BufferedImage(BufferedImage bufferedImage){
       return new ImageIcon(bufferedImage);
   }
   
   public static void SendResponse(String ipAddress,  int port, HashMap<String, Object> hashMap) {
          String command = (String)hashMap.get("command");
          
        try {

            InetAddress inetAddress = InetAddress.getByName(ipAddress); //get inetAddress from ipAddress
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
            Socket socket = new Socket();

            socket.connect(socketAddress, 10000);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());    
   
              /* two ways to ensure we send UTF-8 content to server */
            // 1. create and array of bytes of a string encoded as below
            byte[] buf = new JSONObject(hashMap).toJSONString().getBytes("UTF-8");
     
            dataOutputStream.write(buf, 0, buf.length);
            //2. User dataOutputStream.writeUTF to encode to UTF-8
           // dataOutputStream.writeUTF(new JSONObject(hashMap).toJSONString());  //use dataOutputStream.writeUTF to solve encoding issues for example French characters such as é. Remember not to read the first two bytes of this output
          
            dataOutputStream.flush();
            //close socket after sending data
            socket.close();

        }catch(UnknownHostException ex){ 
               ex.printStackTrace();   
               //show error to user
             //  showMessage(side, "Unknown host exception", "error");
               switch(command){
                   case "Initiate_Connection":
                        DialogConnectToServer.connecting = false; //set flag to false so as not to show animation
                       JOptionPane.showMessageDialog(null, 
                       "Unable to connect."
                       + "\nThe device to connect may have closed the session or may not be on the same network.", "Error connecting!", JOptionPane.WARNING_MESSAGE);
                       break;
                       
                   case "Connection_Initiation_Response":
                       JOptionPane.showMessageDialog(null, 
                       "Unable to connect to recipient."
                       + "\nThe recipient may have closed the session or may not be on the same network.",  "Error connecting!", JOptionPane.WARNING_MESSAGE);
                       break;
                       
                   case "Confirm_File":
                      Dialog_Download_Files.error_Message = ex.getMessage();
                    break;
               }
        }
        catch(NoRouteToHostException ex){  //possibly wrong ip, un reachable ip address
               ex.printStackTrace();   
               //show error to user
             //  showMessage(side, "Unknown host exception", "error");
               switch(command){
                   case "Initiate_Connection":
                       DialogConnectToServer.connecting = false; //set flag to false so as not to show animation
                       JOptionPane.showMessageDialog(null, 
                       "Unable to connect."
                       + "\nThe device to connect may have closed the session or may not be on the same network.", "Error connecting!", JOptionPane.WARNING_MESSAGE);
                       break;
                       
                   case "Connection_Initiation_Response":
                       JOptionPane.showMessageDialog(null, 
                       "Unable to connect to recipient."
                       + "\nThe recipient may have closed the session or may not be on the same network.",  "Error connecting!", JOptionPane.WARNING_MESSAGE);
                       break;
                       
                                            
                   case "Confirm_File":
                        Dialog_Download_Files.error_Message = ex.getMessage();
                        break;
               }
        }
        catch(ConnectException ex){  //probally connectint to wrong port number
               ex.printStackTrace();   
               //show error to user
             //  showMessage(side, "Unknown host exception", "error");
               switch(command){
                   case "Initiate_Connection":
                       DialogConnectToServer.connecting = false; //set flag to false so as not to show animation
                       JOptionPane.showMessageDialog(null, 
                       "The device to connect may have closed the session or may not be on the same network."
                               + "\nVerify credentials and try again.", "Error connecting!", JOptionPane.WARNING_MESSAGE);
                       break;
                       
                   case "Connection_Initiation_Response":
                       JOptionPane.showMessageDialog(null, 
                       "Unable to connect to recipient."
                       + "\nThe recipient may have closed the session or may not be on the same network.",  "Error connecting!", JOptionPane.WARNING_MESSAGE);
                       break;
                       
                  case "Confirm_File":
                        Dialog_Download_Files.error_Message = ex.getMessage();
                    break;
               }
        }
        catch(SocketTimeoutException e){
            e.printStackTrace();
            
            switch(command){
                case "Initiate_Connection":
                    DialogConnectToServer.connecting = false; //set flag to false so as not to show animation
                              //timeout exception. Show orange colour for network status 
                    if(Dialog_Preparing_files_to_share.network_connectivity_label != null)
                        Dialog_Preparing_files_to_share.network_connectivity_label.setIcon(StaticValues.orange_rectangle_16_x_8);
                    
                    JOptionPane.showMessageDialog(null, "The attempt to connect has taken longer than expected."
                            //+ "\nThe network quality at your end or the device to connect to may vary from time to time."
                            + "\nVerify credentials and try again.", "Timeout problem!", JOptionPane.ERROR_MESSAGE);
                    break;
                    
                case "Connection_Initiation_Response":
                              //timeout exception. Show orange colour for network status 
                      if(Dialog_Preparing_files_to_share.network_connectivity_label != null)
                        Dialog_Preparing_files_to_share.network_connectivity_label.setIcon(StaticValues.orange_rectangle_16_x_8);
                    JOptionPane.showMessageDialog(null, "The attempt to connect to recipient has taken longer than expected."
                          //  + "\nThe network quality at your end or the recipients end may vary from time to time."
                            + "\nPlease  try again.", "Timeout problem!", JOptionPane.ERROR_MESSAGE);
                    break;
                    
                 case "Confirm_File":
                      Dialog_Download_Files.error_Message = e.getMessage();
                    break;
            }
  

        }
        catch (IOException ex) {
               ex.printStackTrace();
               
               switch(command){
                   case "Initiate_Connection":
                       DialogConnectToServer.connecting = false; //set flag to false so as not to show animation
                      if(Dialog_Preparing_files_to_share.network_connectivity_label != null)
                        Dialog_Preparing_files_to_share.network_connectivity_label.setIcon(StaticValues.red_rectangle_16_x_8);
                      
                        JOptionPane.showMessageDialog(null,
                               "Unable to connect to device."
                               + "\n" + ex.getMessage(),  "Error connecting!", JOptionPane.ERROR_MESSAGE);
                       break;
                       
                   case "Connection_Initiation_Response":
                         if(Dialog_Preparing_files_to_share.network_connectivity_label != null)
                            Dialog_Preparing_files_to_share.network_connectivity_label.setIcon(StaticValues.red_rectangle_16_x_8);
                         
                        JOptionPane.showMessageDialog(null,  
                               "Unable to connect to recipient."
                               + "\n" + ex.getMessage(), "Error connecting!", JOptionPane.ERROR_MESSAGE);
                       break;
                       
                 case "Confirm_File":
                      Dialog_Download_Files.error_Message = ex.getMessage();
                    break;
               } 
        }
        catch (IllegalArgumentException ex) { //port number out of range
               ex.printStackTrace();
               
               switch(command){
                   case "Initiate_Connection":
                       DialogConnectToServer.connecting = false; //set flag to false so as not to show animation
                         if(Dialog_Preparing_files_to_share.network_connectivity_label != null)
                            Dialog_Preparing_files_to_share.network_connectivity_label.setIcon(StaticValues.red_rectangle_16_x_8);
                         
                        JOptionPane.showMessageDialog(null, 
                               "Unable to connect to device."
                               + "\nEnsure you entered the correct port number", "Port number!", JOptionPane.ERROR_MESSAGE);
                       break;

               } 
        }
            catch (Exception ex) {
               ex.printStackTrace();
               
               switch(command){
                   case "Initiate_Connection":
                       DialogConnectToServer.connecting = false; //set flag to false so as not to show animation
                         if(Dialog_Preparing_files_to_share.network_connectivity_label != null)
                            Dialog_Preparing_files_to_share.network_connectivity_label.setIcon(StaticValues.red_rectangle_16_x_8);
                         
                        JOptionPane.showMessageDialog(null,
                               "Unable to connect to device."
                               + "\n" + ex.getMessage(),  "Error connecting!", JOptionPane.ERROR_MESSAGE);
                       break;
                       
                   case "Connection_Initiation_Response":
                         if(Dialog_Preparing_files_to_share.network_connectivity_label != null)
                            Dialog_Preparing_files_to_share.network_connectivity_label.setIcon(StaticValues.red_rectangle_16_x_8);
                        JOptionPane.showMessageDialog(null, 
                               "Unable to connect to recipient."
                               + "\n" + ex.getMessage(), "Error connecting!", JOptionPane.ERROR_MESSAGE);
                       break;
                       
                 case "Confirm_File":
                      Dialog_Download_Files.error_Message = ex.getMessage();
                    break;
               } 
        }            

        finally{
            switch(command){
                case "Initiate_Connection":
                    DialogConnectToServer.connecting = false;
                    break;
            }
        }
    }
     
            //returns true if server socket is open or already open. When server socket is open, the listening thread for incoming connections is also active
  public  static boolean OpenSocketForIncomingFileData(){
       boolean isOpen = false;
        // Try to open a server socket on port 'serverSocketPort'. Note that we can't choose a port less than 1023 if we are not privileged users (root)
         if(Dialog_Download_Files.server_Socket_To_Listen_For_File_Bytes == null){
            try {
                //try and open a new socket, if we fail we print an error message
                Dialog_Download_Files.server_Socket_To_Listen_For_File_Bytes   = new ServerSocket(0); //we get a random port
                Dialog_Download_Files.server_Socket_Port_For_Listening_For_File_Bytes = Dialog_Download_Files.server_Socket_To_Listen_For_File_Bytes.getLocalPort(); //save the listening port number to a static variable

                isOpen = true;
            } catch (IOException ex) {
                isOpen = false;
                ex.getStackTrace();          
           
            }
        }
         else{
             isOpen = true;
         //    System.out.println("Server is already open");
         }
         return isOpen;
    }
       
        //terminate socket 
    public static void CloseSocketForIncomingFileData(){
       
        if(Dialog_Download_Files.server_Socket_To_Listen_For_File_Bytes != null){
            try {
                Dialog_Download_Files.server_Socket_To_Listen_For_File_Bytes.close();  
                Dialog_Download_Files.server_Socket_To_Listen_For_File_Bytes = null; 
                System.out.println("Server closed. Incoming file transfer");
            } catch (IOException ex) {
                    ex.printStackTrace();
                        
            }
        }
        else{
             //  JOptionPane.showMessageDialog(null, "Server null");
             System.err.println("tried to close null server");
        }
    }
    
      //underlines a jLabel
   public static void JLabel_Underline(JLabel jLabel){
        Font font = jLabel.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        jLabel.setFont(font.deriveFont(attributes));
      }
    
   //create a buffered image with a reflection
  public static BufferedImage createReflection(BufferedImage image) {
        int height = image.getHeight();
        //BufferedImage result = new BufferedImage(image.getWidth(),height * 2, BufferedImage.TYPE_INT_ARGB);
        BufferedImage result = new BufferedImage(image.getWidth(), (height  + (height / 2)), BufferedImage.TYPE_INT_ARGB); // reduced the height of the reflection
        Graphics2D g2 = result.createGraphics();
        // Paints original image
        g2.drawImage(image, 0, 0, null);
        // Paints mirrored image
        g2.scale(1.0, -1.0);
        g2.drawImage(image, 0, -height - height, null);
        g2.scale(1.0, -1.0);
        // Move to the origin of the clone
        g2.translate(0, height);
        // Creates the alpha mask
        GradientPaint mask;
        mask = new GradientPaint(0, 0, new Color(1.0f, 1.0f,1.0f, 0.5f),  0, height / 2, new Color(1.0f, 1.0f, 1.0f, 0.0f));
        g2.setPaint(mask);
        // Sets the alpha composite
        g2.setComposite(AlphaComposite.DstIn);
        // Paints the mask
        g2.fillRect(0, 0, image.getWidth(), height );

        g2.dispose();
        return result;
        }
  
     //converts a path supplied, to a path the operating system of the device can actually use to write a file. Forexample if a path is from a windows machine to a unix system then 
    // ocurrences of '\' are converted to '/'. Supply the name of the operating system from which the path is originating
    //arguments supplied: operating system of the device with the files,  file path(path as understood by device with files), directory where the file resides (as on device with the files) this will be used below to substring and provide a relevant directory structure, folder where downloaded files will be kept 
    public static String Path_Converter(String operating_system_to_convert_from, String path_to_convert, String parent_directory_of_source_content, String path_of_folder_to_paste_in){

        //if the operating system of the local machine is windows and operating system of the other device is not windows then we convert unix path to windows
        if(StringUtils.containsIgnoreCase(StaticValues.os_Name, "windows")){ //operating system of local machine is windows
            //if the other device is not windows, then convert 'unix' path to windows path
            if(!StringUtils.containsIgnoreCase(operating_system_to_convert_from, "windows")){
          
                
                //replace '//' with '\\'
                //path_to_convert = StringUtils.replace(path_to_convert, "//", "\\\\");
              //  parent_directory_of_source_content = StringUtils.replace(parent_directory_of_source_content, "//", "\\\\");
                path_of_folder_to_paste_in = unEscapeJSON( path_of_folder_to_paste_in);
                path_to_convert = unEscapeJSON(path_to_convert);
                parent_directory_of_source_content = unEscapeJSON(parent_directory_of_source_content);
                
                path_to_convert = StringUtils.substringAfter(path_to_convert, parent_directory_of_source_content);
            }
            else{ //if both operating systems are windows
                //unescape json
                path_of_folder_to_paste_in = unEscapeJSON( path_of_folder_to_paste_in);
                path_to_convert = unEscapeJSON(path_to_convert);
                parent_directory_of_source_content = unEscapeJSON(parent_directory_of_source_content);
                
                 path_to_convert = StringUtils.substringAfter(path_to_convert, parent_directory_of_source_content);
            }
        }
        else{//the operating system of the local machine is unix 
            //if the other device is not unix, then convert 'windows' path to 'unix' path
             if(StringUtils.containsIgnoreCase(operating_system_to_convert_from, "windows")){
                 
                //replace '//' with '\\'
               // path_to_convert = StringUtils.replace(path_to_convert, "\\\\", "//");
                //parent_directory_of_source_content = StringUtils.replace(parent_directory_of_source_content, "\\\\", "//");
                
                path_to_convert = unEscapeJSON(path_to_convert);
                parent_directory_of_source_content = unEscapeJSON(parent_directory_of_source_content);

                 path_of_folder_to_paste_in = unEscapeJSON(path_of_folder_to_paste_in);
        
                //substring 'parent_directory_of_source_content' and 'path_to_convert' to get a relevant directory structure to use when writing downloaded files
                path_to_convert = StringUtils.substringAfter(path_to_convert, parent_directory_of_source_content);
                
                //turn to unix path
             //   path_to_convert = StringUtils.replace(path_to_convert, "\\", "/");
            }
            else{ //if both operating systems are unix
                //unescape json
                path_to_convert = unEscapeJSON(path_to_convert);
                parent_directory_of_source_content = unEscapeJSON(parent_directory_of_source_content);
                
                path_of_folder_to_paste_in = unEscapeJSON(path_of_folder_to_paste_in);
        
                //substring 'parent_directory_of_source_content' and 'path_to_convert' to get a relevant directory structure to use when writing downloaded files
                path_to_convert = StringUtils.substringAfter(path_to_convert, parent_directory_of_source_content);
           
            }
        }

        
        //concantenate 'path_of_folder_to_paste_in' to 'path_to_convert' to get the a string that represents a file path to write to on the local machine
        return ( FilenameUtils.separatorsToSystem(path_of_folder_to_paste_in + path_to_convert ));
        
    }
    
    //file_Path is the path of the file, folder_Path is like the parent folder i.e file path = D:\My Folder\file.txt, Folder path = D:\My Folder
    public static String Path_Converter2(String operating_system_to_convert_from, String file_Path, String folder_Name, String folder_Path, String path_of_folder_to_paste_in){

        //if the operating system of the local machine is windows and operating system of the other device is not windows then we convert unix path to windows
        if(StringUtils.containsIgnoreCase(StaticValues.os_Name, "windows")){ //operating system of local machine is windows
            //if the other device is not windows, then convert 'unix' path to windows path
            if(!StringUtils.containsIgnoreCase(operating_system_to_convert_from, "windows")){
          
                
                //replace '//' with '\\'
                //path_to_convert = StringUtils.replace(path_to_convert, "//", "\\\\");
              //  parent_directory_of_source_content = StringUtils.replace(parent_directory_of_source_content, "//", "\\\\");
                path_of_folder_to_paste_in = unEscapeJSON( path_of_folder_to_paste_in);
                file_Path = unEscapeJSON(file_Path);
                folder_Path = unEscapeJSON(folder_Path);
                
                file_Path = StringUtils.substringAfter(file_Path, folder_Path);
            }
            else{ //if both operating systems are windows
                //unescape json
                path_of_folder_to_paste_in = unEscapeJSON( path_of_folder_to_paste_in);
                file_Path = unEscapeJSON(file_Path);
                folder_Path = unEscapeJSON(folder_Path);
                
                file_Path = StringUtils.substringAfter(file_Path, folder_Path);
            }
        }
        else{//the operating system of the local machine is unix 
            //if the other device is not unix, then convert 'windows' path to 'unix' path
             if(StringUtils.containsIgnoreCase(operating_system_to_convert_from, "windows")){
                 
                //replace '//' with '\\'
               // path_to_convert = StringUtils.replace(path_to_convert, "\\\\", "//");
                //parent_directory_of_source_content = StringUtils.replace(parent_directory_of_source_content, "\\\\", "//");
                
                file_Path = unEscapeJSON(file_Path);
                folder_Path = unEscapeJSON(folder_Path);

                 path_of_folder_to_paste_in = unEscapeJSON(path_of_folder_to_paste_in);
        
                //substring 'parent_directory_of_source_content' and 'path_to_convert' to get a relevant directory structure to use when writing downloaded files
                file_Path = StringUtils.substringAfter(file_Path, folder_Path);
                
                //turn to unix path
             //   path_to_convert = StringUtils.replace(path_to_convert, "\\", "/");
            }
            else{ //if both operating systems are unix
                 /*
                    System.out.println("---------------------------------------------------------------");
                    System.out.println("path_of_folder_to_paste_in:" + path_of_folder_to_paste_in);
                    System.out.println("folder_Name:" + folder_Name);
                    System.out.println("file_Path:" + file_Path);
                    */
                    
                //unescape json
                file_Path = unEscapeJSON(file_Path);
                folder_Path = unEscapeJSON(folder_Path);
                
                path_of_folder_to_paste_in = unEscapeJSON(path_of_folder_to_paste_in);
        
                //substring 'parent_directory_of_source_content' and 'path_to_convert' to get a relevant directory structure to use when writing downloaded files
                file_Path = StringUtils.substringAfter(file_Path, folder_Path);
           
            }
        }

   
        //concantenate 'path_of_folder_to_paste_in' + 'folder name +  'file_Path' to get the a string that represents a file path to write to on the local machine
        return ( FilenameUtils.separatorsToSystem(path_of_folder_to_paste_in + File.separator + folder_Name +  file_Path ));
        
    }
    
    //return the path where the jar is executed. This helps to locate resource folders that are bundled next to the jar.
    public static String Path_Of_Running_Jar(Class cl) throws UnsupportedEncodingException{
        String path = cl.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        //strip off the the jar name
       decodedPath = StringUtils.substringBeforeLast(decodedPath, File.separator);
        //the decoded path contains unix separators. If a windows system is using the the app then convert the separators to windows separtor
        if(StringUtils.containsIgnoreCase(StaticValues.os_Name, "windows")){
            decodedPath = FilenameUtils.separatorsToWindows(decodedPath);
        }
        
        //strip off the jar name
        decodedPath = StringUtils.substringBeforeLast(decodedPath, File.separator);
        return decodedPath;
        
    }
    
        //get an image from a package and return a buffered image
    public static BufferedImage BufferedImageFromResourcePackage(String path) throws IOException{
         Class<?> clas = MyMethods.class;
          InputStream input = clas.getResourceAsStream(path);
        BufferedImage bufferedImage = ImageIO.read(input);
        return bufferedImage;
    }
}
