/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import Custom_Listeners.Exit_Listener;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author vian
 */
public class Dialog_Download_Files extends javax.swing.JDialog implements Exit_Listener {

    /**
     * Creates new form Dialog_Download_Files
     */
    public static String error_Message; //a static var to get messages when operating the sockets
    String ip_Address; 
    Timer timer;
    HashMap<String, Object> info;
    
    ArrayList <String> file_Paths = new ArrayList<>();
    ArrayList <String> file_Names = new ArrayList<>();
    ArrayList <String> file_Sizes = new ArrayList<>();
    ArrayList <String> icons_To_Show = new ArrayList<>();
         
    HashMap<String, Object> folders_Info;
    
    String selections_And_Size_Count; //will have the number of items selected and there size
    Long number_Of_Items_Selected;
    Long size_Of_Items_Selected;
    
    String save_Directory; //path of where user will be saving the files
    
    RecieveCommand recieveCommand;
    SendCommand sendCommand;
    
    Socket socket;
    Thread thread;
    boolean listen_For_Connection; //true if we want to listen for incoming instructions
    boolean thread_Run = true; //false for thread for loops in thread to stop
    
    //for downloading process
    long bytes_Downloaded; //total bytes downloaded since session started
    long bytes_Downloaded_Of_Current_Item; //how much of the current item has been downloaded
    int percentage_Downloaded;
    int percentage_Downloaded_Of_Current_Item;
    int counter_3;
    
    long number_Of_Failed_Downloads; //number of files unsuccessfully downloaded
    long number_Of_Complete_Downloads; //completed downloads 
    String name_Of_Current_Item; //current item being downloaded
    long size_Of_Current_Item;//size of the item being downloaded
    
    HashMap <String, Object> folders_In_Root_Selection; //folders in root selection
    
    String what_To_Do_If_File_Exists; //skip, replace or keep both files if a file exists
    
    String os_Name_For_Server; //operating system of the server which has files to be requested
    
    HashMap <String, Object> confirmed_File_Results = new HashMap<>(); //will have the confirmed results from the server
    
    //server socket that will listen for incoming file transfer after requesting for a file
    public static ServerSocket server_Socket_To_Listen_For_File_Bytes; //listen for incoming bytes for requested files
    public static int server_Socket_Port_For_Listening_For_File_Bytes;//port number to use when expecting file bytes
    
    //for listening for download
    Socket socket2 = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;
    
    Icons icons = new Icons(); //will get the icons with reflection for us
    
    Check_If_Downloading_Has_Stalled check_If_Downloading_Has_Stalled; //globally accesible 
    Timer timer2;
    
    public Dialog_Download_Files(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        if(StaticValues.military_theme){
            Set_Military_Theme();
        }
        
        this.setLocationRelativeTo(parent); //position in center
    }

    public Dialog_Download_Files(JFrame jframe, HashMap <String, Object> info){
        this(jframe, true); //call the other constructor
        this.info = info;
  
        //load arraylists
        file_Paths = (ArrayList)info.get("file_Paths");
        file_Names = (ArrayList)info.get("file_Names");
        file_Sizes = (ArrayList)info.get("file_Sizes");
        icons_To_Show = (ArrayList)info.get("icons_To_Show");
        
        folders_Info = (HashMap)info.get("folders_Info");
        
        selections_And_Size_Count = (String)info.get("selections_And_Size_Count");
        number_Of_Items_Selected = Long.parseLong((String)info.get("number_Of_Items_Selected")); //number of selection in previous table
        size_Of_Items_Selected = Long.parseLong((String)info.get("size_Of_Items_Selected"));
        
        save_Directory = (String)info.get("save_Directory");
        os_Name_For_Server = (String)info.get("os_Name_For_Server");
        
        what_To_Do_If_File_Exists = (String)info.get("what_To_Do_If_File_Exists");
        
        folders_In_Root_Selection = (HashMap)info.get("folders_In_Root_Selection"); //folders that are present in root selection. check key set for paths of folders and folder names as values
        
        //show how many items are expected and the size of the download
        jLabel17.setText(file_Paths.size() + "");
        jLabel18.setText(MyMethods.humanReadableByteCount(size_Of_Items_Selected, true));
 
        jLabel42.setIcon(new ImageIcon(StaticValues.document_blank_Buffered_Image_with_Reflection));
        
        //set jLabel32 and jLabel35 foreground to white
        jLabel32.setForeground(Color.white);
        jLabel35.setForeground(Color.white);
      
        //open socket to listen for instructions
        OpenServerSocket();
        
                  //if ip address is null then user isn't on a network
        ip_Address = MyMethods.GetAddress("ip");
           
           if(!StringUtils.isEmpty(ip_Address) ){ //if ip address is not empty then we have some connnectivity. display green icon
                jLabel21.setIcon(StaticValues.green_rectangle_16_x_8);
           }
           else{
               ip_Address = "";
               jLabel21.setIcon(StaticValues.loading_gif_3_16_x_8); 
               
                 error_Message = "Connectivity so unstable.";
                  //display error icon
                 An_Error();
                 JOptionPane.showMessageDialog(Dialog_Download_Files.this, "The connection completely broke at some point."
                         + "\nSession will now close!", "Error!", JOptionPane.ERROR_MESSAGE);
                 
                 Dialog_Download_Files.this.dispose(); //close the window
           }
           
           //start timer to periodically check if local device is on network. The ip address cannot be empty
        CheckConnectivity_TimerTask checkConnectivity_TimerTask = new CheckConnectivity_TimerTask();
         timer = new Timer();
         timer.schedule(checkConnectivity_TimerTask, 3000, 2000); //will keep running. Cancel when window is closed
        //down load files in a separate thread
        /*
        new Thread(){
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                 Download_Files();
            }
        }.start();
       */
        
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
               //add listener
               MainInterface.add_Exit_Listener(Dialog_Download_Files.this);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //remove listener
                MainInterface.remove_Exit_Listener(Dialog_Download_Files.this);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                
            }

            @Override
            public void windowIconified(WindowEvent e) {
                
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                
            }

            @Override
            public void windowActivated(WindowEvent e) {
               
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
               
            }
        });
    }
    
            @Override
    public void dispose() {
       error_Message = "";
       
        thread_Run = false; //flag for all loops in thread not to continue
        listen_For_Connection = false;
        
        try{
            if(timer2 != null){
                timer2.cancel();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
//        connected = false;
        
        CloseServerSocket(); //stop listening for instructions
        MyMethods.CloseSocketForIncomingFileData(); //stop downloading if currently downloading. closes the socket responsible for recieving files
        
        if(socket2 != null)
            try {
                socket2.close();
       } catch (IOException ex) {
           Logger.getLogger(Dialog_Download_Files.class.getName()).log(Level.SEVERE, null, ex);
       }
        
        super.dispose(); 
//        
//           if(!DialogConnectToServer.connecting){
//            frame_confirm_selection.dispose();
//        }
        //close server socket
        //CloseServerSocket();
        //** instead of closing the socket which could be in use else we set the listening flag to false so that this Dialog can stop listening
           
    }
    
        @Override
    public void exit_Listener() {
        //close window
        Dialog_Download_Files.this.dispose();
    }
    
    
     public void Set_Military_Theme(){
        jLabel13.setBackground(StaticValues.gray_1);
        jLabel13.setBorder(StaticValues.border);
      
        jPanel6.setBackground(StaticValues.gray_1);
        jPanel6.setBorder(StaticValues.border);
        
        jPanel9.setBackground(StaticValues.gray_1);
        
        jPanel10.setBackground(StaticValues.gray_1);
        
        jPanel11.setBackground(StaticValues.gray_1);
        
                //resize dialog coz some borders are more taller
        Dialog_Download_Files.this.setSize(this.getWidth(), this.getHeight() + 60);
    }
    
    
    public void Download_Files(){
        //start timer to check if download has stalled. If we don't recieve bytes over a period of time then the download has stalled
        check_If_Downloading_Has_Stalled = new Check_If_Downloading_Has_Stalled();
        timer2 = new Timer();
        timer2.schedule(check_If_Downloading_Has_Stalled, 5000, 5000); 
        
        System.out.println("Starting download...");
        ip_Address = MyMethods.GetAddress("ip"); //send the ip address of local machine to requesting device. 
        String mac_Address = MyMethods.GetAddress("mac"); //send the mac address of local machine to requesting device. 
        int port = DialogConnectToServer.serverSocket.getLocalPort(); //get the listening port
        
        String new_file_name; //will be checked if empty. If not empty then the intention is not to over-write but keep both files
          
        HashMap <String, Object> hashMap = new HashMap<>();
        
        for(int i = 0; i < file_Paths.size(); i++){
            hashMap.clear(); //clear map for re-use 
                   //clear 'new_file_name'
            new_file_name = "";
            bytes_Downloaded_Of_Current_Item = 0L;
          
            
            String file_Name = file_Names.get(i);
            String file_Path = file_Paths.get(i);
            String file_Size = file_Sizes.get(i);
            String file_Type = icons_To_Show.get(i); //know whether it is a folder or not
           // System.out.println("file_path:" + file_Path);
             //ask the device with the file for confirmation
             String command = "Confirm_File";
             
             hashMap.put("command", command);
             hashMap.put("ip_Address", ip_Address);
             hashMap.put("mac_Address", mac_Address);
             hashMap.put("port", port + "");
             //include file information
             hashMap.put("file_Path", file_Path);
             hashMap.put("file_Size", file_Size);
             hashMap.put("file_Type", file_Type);
             
             sendCommand = new SendCommand(command, DialogConnectToServer.server_Ip_Address, Integer.parseInt(DialogConnectToServer.server_Port), hashMap);
             sendCommand.Excecute();
             
                     //wait for some time...
                       //if confirmation arrives break from the while loop. The loop is to wait for the confirmation. When the confirmation is not empty then we can proceed
              //when confirmation arrives the information is stored in 'confirmed_File_Results' map. This is done in the recieve command class .Remember to clear this map to be empty for re-use
             int counter = 0;
             while(confirmed_File_Results.isEmpty()){
                        if(!thread_Run)
                            break;
                        
                        if(!StringUtils.isEmpty(error_Message))
                            System.out.println("error message:" + error_Message);
                          //change to animation icon
                          Downloading_Icons();
                          
                          //System.out.println("counter:" + counter);
                           if(counter >= 100){
                               break;
                               }
                           
                           try {
                             
                                  //wait for intervals
                                  Thread.sleep(300);
                                  
                                  counter ++;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                            }
                       }
            
                  if(!thread_Run)
                            break;
                  
                //if we still don't have confirmed results there is a problem. Server should have been able to get back a response.  we don't proceed
             if(confirmed_File_Results.isEmpty()){
                 //display error icon
                 An_Error();
                 JOptionPane.showMessageDialog(Dialog_Download_Files.this, "There was a problem getting file confirmation."
                         + "\nSession will now close!", "Error!", JOptionPane.ERROR_MESSAGE);
                 
                 Dialog_Download_Files.this.dispose(); //close the window
                 break;
             }
             
            //we have confirmed results. Check for success flag
             String success = (String)confirmed_File_Results.get("success");
             
             //if success is no, we add to failed downloads and continue with the loop
             if(StringUtils.equalsIgnoreCase(success, "no")){
                 System.err.println("failed to download:" + file_Path);
                 System.err.println("reason:" + (String)confirmed_File_Results.get("message")); //show the reason why the file failed as reported by server
                 //increment failed download counter
                 number_Of_Failed_Downloads ++;
                 
                 //show number of failed downloads in jLabel. Set foreground to red
                 Show_Number_Of_Downloads();
                 
                 continue; //continue loop from top
             }
             
             //success is 'yes'
             //update any information that may have changed for example if a different file size is reported
             String file_Size_Reported = (String)confirmed_File_Results.get("file_size_reported"); //size of file reported by the server
             //if the server reports a different file size then we adjust accordingly
             if(!StringUtils.equals(file_Size_Reported, file_Size)){
                 
                if(StringUtils.isEmpty(file_Size_Reported))
                            file_Size_Reported = "0";
                
                if(StringUtils.isEmpty(file_Size))
                            file_Size = "0";
                        
                 //turn the file sizes from strings to numbers and do some calculations
                 long file_Size_Reported_Long = Long.parseLong(file_Size_Reported);
                 long file_Size_Long = Long.parseLong(file_Size);
                 Long difference;
                     /*   
                if(file_Size_Long > file_Size_Reported_Long){
                            difference = (file_Size_Reported_Long - file_Size_Long); //i.e 2 - 4 = -2. -2 is added to the total content size
                            System.out.println("File:" + file_Path + "\nFile size reported from source is smaller.\nsize reported to source is:" + file_Size_Reported_Long + "\nsize reported from source is:" + file_Size_Long + "\ndifference:" + difference+ "\n--------------------");
                        }
                        else{
                            difference = (file_Size_Long - file_Size_Reported_Long); //i.e 4 - 2 = 2. 2 is added to the total content size
                             System.out.println("File:" + file_Path + "\nFile size reported from source is bigger.\nsize reported to source is:" + file_Size_Long + "\nsize reported from source is:" + file_Size_Reported_Long + "\ndifference:" + difference + "\n--------------------");
                        }
                        
                //add difference to total content size
                size_Of_Items_Selected += difference;
                */
                //show the new size
                jLabel18.setText(MyMethods.humanReadableByteCount(size_Of_Items_Selected, true));
                
                 file_Size = file_Size_Reported; //file size should be the most recent as reported.
             }
             
             //open server socket to listen for incoming bytes of requested file
             MyMethods.OpenSocketForIncomingFileData(); //start expecting some file data to be recieved.
             
             String file_Path_To_Write = ""; //path to write to after any conversion of path
             file_Path_To_Write = save_Directory + File.separator + file_Name;
             //if the file is or is contained in one of the folders in the root selection then we have to convert the path, remember the directory structure will be created.
             //if the file is not contained in a folder in the root selection then simply consider the name and append it to the 'save_directory'
             //the folders in the root selection can be located in 'folders_In_Root_Selection' map. The key is path and value is folder name. Loop through the key set and if the file path starts with any key then that file belongs to that folder
             for(String folder_Path : folders_In_Root_Selection.keySet()){
                 //if the file path starts with the folder path then the file is contained some where in that folder i.e. file path = D:\My Folder\file.txt, Folder path = D:\My Folder
                 if(StringUtils.startsWith(file_Path, folder_Path)){
                     String folder_Name = (String)folders_In_Root_Selection.get(folder_Path); //get the folder name
                     //convert the path
                     /*
                     System.out.println("folder path:" + folder_Path);
                     System.out.println("file path:" + file_Path);
                     System.out.println("file name:" + file_Name);
                     System.out.println("folder name:" + folder_Name);
                     */
                 
                     file_Path_To_Write = MyMethods.Path_Converter2(os_Name_For_Server, file_Path, folder_Name, folder_Name, save_Directory);
                 }
                 
             }
             
            //System.out.println("file_Path_To_Write:" + file_Path_To_Write + "\n");
                   //try and create the directory structure
                   try {
                            //if file is folder then it's an empty folder, simply create it and continue with the loop
                       if(StringUtils.equals(file_Type, "folder")){
                           System.out.println("created empty folder");
                             FileUtils.forceMkdir(new File(file_Path_To_Write));
                            //update counter 
                             number_Of_Complete_Downloads ++;
                             
                             Show_Number_Of_Downloads(); //show the download count in the labels
                             
                           continue;
                       }
                       else
                            FileUtils.forceMkdirParent(new File(file_Path_To_Write));
                       
                        } catch (IOException ex) {
                            Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                            System.err.println("failed to create directory structure for 'converted_path'");
                              //update counter for failed downloads
                            number_Of_Failed_Downloads ++;
                            
                            Show_Number_Of_Downloads(); //show the download count in the labels
                            continue;
                        }
                   
                                      
                   File file_ = new File(file_Path_To_Write);
                   
                   try{
                   //check paste flags and act accordingly, by default files will replace so need to implement 'replace'
                   //SKIP
                   if(StringUtils.containsIgnoreCase(what_To_Do_If_File_Exists, "skip")){
                       //if a file (not folder!) already exists we don't download it, rather we skip it and proceed to the next, we continue the loop
                       if(file_.exists() && !file_.isDirectory()){
                           System.out.println("skipped... " + file_Path_To_Write );
                               //increment the dowloaded files
                           number_Of_Complete_Downloads ++;
                           
                           Show_Number_Of_Downloads(); //show the download count in the labels
                           continue; //continue the loop
                       }
                   }
                   
                   //KEEP BOTH
               
                   if(StringUtils.containsIgnoreCase(what_To_Do_If_File_Exists, "keep")){
                       //if a file (not folder!) already exists we don't download it, rather we create a new name 
                       if(file_.exists() && !file_.isDirectory()){
                          //
                           String baseName = FilenameUtils.getBaseName(file_Path_To_Write); 
                           String file_extension = FilenameUtils.getExtension(file_Path_To_Write);
                           String path_without_file_name = FilenameUtils.getFullPath(file_Path_To_Write);
                           
                           baseName = baseName + "_" + new Date().getTime();
                           
                           //append '.' to an extension if extension is available
                           if(!StringUtils.isEmpty(file_extension))
                               file_extension = "." + file_extension;
                               
                           new_file_name = path_without_file_name + baseName + file_extension;
                           
                                //try and create the directory structure
                   try {
                            
                            FileUtils.forceMkdirParent(new File(new_file_name));
                       
                        } catch (IOException ex) {
                            Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                            System.err.println("failed to create directory structure for 'new_file_name'");
                              //update counter for failed downloads
                            number_Of_Failed_Downloads ++;
                            
                            Show_Number_Of_Downloads(); //show the download count in the labels
                            continue;
                        }
               
                           System.out.println("new file to keep... " + new_file_name );
                         
                       }
                   }
                   
                   
                   }
                   catch(Exception e){
                       e.printStackTrace();
                   }
                    
                   
                       //request for file. tell device with the file to start sending file to local machine
                       //ask the device with the file for confirmation
                        String command_2 = "Send_File";
                        
                        hashMap.clear(); //clear hashmap for re-use
                        hashMap.put("command", command_2);
                        hashMap.put("mac_Address", mac_Address); //the mac address of the device that wants the files
                        hashMap.put("ip_Address", ip_Address); //ip address of the device that wants the files
                        hashMap.put("port", port + "");
                        hashMap.put("port_For_File_Transmission", Dialog_Download_Files.server_Socket_Port_For_Listening_For_File_Bytes + ""); //port on which file transfer will happen
                        hashMap.put("file_Path", file_Path);
                        //include total file size expected and the number of items selected
                        hashMap.put("size_Of_Items_Selected", size_Of_Items_Selected + "");
                        hashMap.put("number_Of_Items_Selected", number_Of_Items_Selected + "");
                        
                        SendCommand sendCommand = new SendCommand(command_2, DialogConnectToServer.server_Ip_Address, Integer.parseInt(DialogConnectToServer.server_Port), hashMap);
                        sendCommand.Excecute();
       
                        //incomging file data should be dealt with. 
                       try {
                          
                            socket2 = Dialog_Download_Files.server_Socket_To_Listen_For_File_Bytes.accept();
                        
                        } catch (IOException ex) {
                            System.err.println("Can't accept client connection.");
                            
                             number_Of_Failed_Downloads ++;
                             Show_Number_Of_Downloads();
                             continue;
                        }

                        try {
                      
                            inputStream = socket2.getInputStream();
                        } catch (IOException ex) {
                            System.err.println("Can't get socket input stream. ");
                            
                             number_Of_Failed_Downloads ++;
                             Show_Number_Of_Downloads();
                             continue;
                        }

                        try {
                            //if we don't intend to keep both files if same then we use 'converted_path' else we use the new file name. We check if the new file name is not empty before deciding
                            if(!StringUtils.isEmpty(new_file_name))
                               outputStream = new FileOutputStream(new_file_name); 
                            else
                                outputStream = new FileOutputStream(file_Path_To_Write);
                            
                        } catch (FileNotFoundException ex) {
                            System.out.println("File not found. ");
                            
                             number_Of_Failed_Downloads ++;
                             Show_Number_Of_Downloads();
                             continue;
                        }
                   
             //clear this map for every iteration 
             confirmed_File_Results.clear();
             
                       
             byte[] bytes = new byte[512];
           //   MyValues.file_size = Long.parseLong(fileSizeOfSource); //size of the file currently being downloaded

                    int count;
                    int counter2 = 0;
                    bytes_Downloaded_Of_Current_Item = 0L;
                    long file_Size_long = Long.parseLong(file_Size); //size of the current item, so as to parse every time to long
                    //change the required file icon and name
                    Show_Icon_And_Name_And_File_Size(file_Name, file_Size_long);
                    
                        try {
                            while ((count = inputStream.read(bytes)) > 0) {
                                outputStream.write(bytes, 0, count);
                                
                                bytes_Downloaded_Of_Current_Item += count;
                                bytes_Downloaded += count;
                                
                                //---calculate percentage for current item
                                //if the current size of the item being downloaded is zero then we put percentage to 100. This also helps avoid division by zero if file size is zero
                                if(file_Size_long == 0){
                                    percentage_Downloaded_Of_Current_Item = 100;
                                }
                                else
                                    percentage_Downloaded_Of_Current_Item = (int)( 100 * bytes_Downloaded_Of_Current_Item / file_Size_long); //percentage of bytes written so far on current file
                            
                                if(percentage_Downloaded_Of_Current_Item != counter2){
                                    counter2 = percentage_Downloaded_Of_Current_Item;
                                     //System.out.println("file % " + MyValues.file_percentage);
                                    
                                     Post_Progress(percentage_Downloaded_Of_Current_Item);
                                }
                        //-----
                                //----------calculate percentage for all thing i.e. entire percentage
                                if(size_Of_Items_Selected == 0){
                                    percentage_Downloaded = 100;
                                }
                                else
                                    percentage_Downloaded = (int)( 100 * bytes_Downloaded / size_Of_Items_Selected); //percentage of bytes written so far
                            
                                if(percentage_Downloaded != counter_3){
                                    counter_3 = percentage_Downloaded;
                                     //System.out.println("file % " + MyValues.file_percentage);
                                    
                                     Post_Progress_Total(percentage_Downloaded);
                                }
                                //---------
                                
                                //update progress on labels that will show continuos progress as data arrives
                                Update_Progress_On_Labels(bytes_Downloaded_Of_Current_Item, bytes_Downloaded);
                                
                            }   
                        } catch (IOException ex) {
                            Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                            
                             number_Of_Failed_Downloads ++;
                             Show_Number_Of_Downloads();
                             continue;
                        }

                    //increment the dowloaded files
                    number_Of_Complete_Downloads++;
                    Show_Number_Of_Downloads();
                    
        } //end for loop
        
        Show_Finished_Downloading();
        MyMethods.CloseSocketForIncomingFileData(); //stop listening for incoming files
    }
    
    //show progress on progress bar
    public void Post_Progress(int percentage){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               jProgressBar3.setValue(percentage);
               
               if(percentage == 100){
                 //  System.out.println("finished");
               }
            }
        });
    }
    
    
    //show progress on progress bar
    public void Post_Progress_Total(int percentage){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               jProgressBar4.setValue(percentage);
               
               if(percentage == 100){
                 //  System.out.println("finished");
                   //close the timer that checks for stalled downloads
                   if(timer2 != null){
                        timer2.cancel();
                        timer2 = null;
                   }
               }
            }
        });
    }
    
    public void Update_Progress_On_Labels(long bytes_Downloaded_Of_Current_Item, long bytes_Downloaded){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
             jLabel34.setText(MyMethods.humanReadableByteCount(bytes_Downloaded_Of_Current_Item, true));
             jLabel41.setText(MyMethods.humanReadableByteCount(bytes_Downloaded, true));
            }
        });
    }
    
    
    //shows the nujmber of complete and failed downloads in the labels to the user
    public void Show_Number_Of_Downloads(){
         java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        //show failed downloads
                        if(number_Of_Failed_Downloads > 0){
                            jLabel32.setForeground(Color.red);
                            jLabel35.setForeground(Color.red);
                            jLabel35.setText(number_Of_Failed_Downloads + "");
                            }
                        
                        //show successful downloads
                        if(number_Of_Complete_Downloads > 0){
                            jLabel40.setText(number_Of_Complete_Downloads + "");
                        }
                    }
                });
    }
    
    //shows the icon and name of the item being downloaded and also it's size in the label
    public void Show_Icon_And_Name_And_File_Size(String file_Name, long file_Size){
         java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                     //show icon of file
                     jLabel42.setIcon(new ImageIcon(icons.Get_Buffered_Image(file_Name, true)));
                     jLabel43.setText(file_Name);
                     //show file size
                     jLabel33.setText(MyMethods.humanReadableByteCount(file_Size, true));
                     jLabel34.setText(MyMethods.humanReadableByteCount(0, true)); //file size should start at zero
                    }
                });
    }
    
    //display icon to show downloading is finished
        public void Show_Finished_Downloading(){
         java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        
                        //if everything is downloaded
                      if(percentage_Downloaded == 100){
                        jLabel13.setText("Finished.");
                        jLabel13.setIcon(StaticValues.success_x_55);
                        }
                      else{
                         jLabel13.setText("Stalling detected!");
                        jLabel13.setIcon(StaticValues.error_x_55);
                      }
                      
                      jProgressBar3.setValue(0);
                      jProgressBar3.setStringPainted(false);
                      jProgressBar3.setEnabled(false);
                      
                      jLabel33.setText(" ");
                      jLabel34.setText(" ");
                      jLabel35.setText(" ");
                      
                      jLabel43.setText(" ");
                      
                      //remove reflection icon
                      jLabel42.setText(" ");
                      jLabel42.setIcon(null);
                      
                      //if download is not 100%
                      /*
                      if(percentage_Downloaded != 100){
                       MyMethods.CloseSocketForIncomingFileData(); //stop listening for incoming files
                            
                        error_Message = "Unable to get everything.";
                        //display error icon
                       An_Error();
                       JOptionPane.showMessageDialog(Dialog_Download_Files.this, "Sorry, the download may have stalled."
                               + "\nSession will now close!", "Error!", JOptionPane.ERROR_MESSAGE);

                       Dialog_Download_Files.this.dispose(); //close the window
                      }
                      */
                    }
                });
                
    }
        
            //returns true if server socket is open or already open. When server socket is open, the listening thread for incoming connections is also active
     public  boolean OpenServerSocket(){
       boolean isOpen = false;
        // Try to open a server socket on port 'serverSocketPort'. Note that we can't choose a port less than 1023 if we are not privileged users (root)
         if(DialogConnectToServer.serverSocket == null){
            try {
                //try and open a new socket, if we fail we print an error message
                DialogConnectToServer.serverSocket  = new ServerSocket(0); //we get a random port
                DialogConnectToServer.listening_Port = DialogConnectToServer.serverSocket.getLocalPort(); //save the listening port number to a static variable
                
                //start listening thread
                listen_For_Connection = true; //flag for whether thread listening for connection should do so
                ListenForConnection();
                isOpen = true;
            } catch (IOException ex) {
                isOpen = false;
                ex.getStackTrace();          
            
            }
        }
         else{
             isOpen = true;
             //start listening thread
                listen_For_Connection = true; //flag for whether thread listening for connection should do so
                ListenForConnection();
             System.out.println("Server is already open");
                             
         }
         
         return isOpen;
    }
       
        //terminate socket which will terminate server connection with clients
    public void CloseServerSocket(){
        listen_For_Connection = false; //set flag to false so that thread listening for incoming commands should stop
        
        if(DialogConnectToServer.serverSocket != null){
            try {
                DialogConnectToServer.serverSocket.close();  
                DialogConnectToServer.serverSocket = null; 
               // JOptionPane.showMessageDialog(null, "Server closed"); 
            } catch (IOException ex) {
                    ex.printStackTrace();
                   //  JOptionPane.showMessageDialog(null, ex.getMessage().toString() + " \n" + ex.toString());           
            }
        }
        else{
             //  JOptionPane.showMessageDialog(null, "Server null");
             System.err.println("tried to close null server");
        }
    }
    
       public  void ListenForConnection(){
//       if(thread == null){
           thread = new Thread(){
              @Override
              public void run(){
                  while(listen_For_Connection){
           //    System.out.println("Thread running");
                      try {
                          socket = DialogConnectToServer.serverSocket.accept();
                     
                             recieveCommand = new RecieveCommand( socket);
      
//                           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                          DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
//                           String clientSentence = inFromClient.readLine();
//                           System.out.println("Received: " + clientSentence);
//                           String  capitalizedSentence = clientSentence.toUpperCase() + '\n';
//                           outToClient.writeBytes(capitalizedSentence); 
//                          
//                          System.out.println("connected..." + this.getId());
//                          JOptionPane.showMessageDialog(null, "connected");                       
                      } catch (IOException ex) {
                       //   JOptionPane.showMessageDialog(null, ex.getMessage().toString() + " \n" + ex.toString());
                          ex.printStackTrace();
                      }
                  }
              } 
           };
           thread.start();     
    }
       

    
       //display icons 
    public void An_Error(){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                if(StringUtils.isEmpty(error_Message))
                  jLabel13.setText("There was an error!");
                else
                     jLabel13.setText(error_Message);
                
                jLabel13.setIcon(StaticValues.error_x_55);
            }
        });
    }
    
       //display icons 
    public void A_Warning(String message){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
          
              jLabel13.setText(message);
                
                jLabel13.setIcon(StaticValues.warning_x_55);
            }
        });
    }
    
       //display icons for downloading
    public void Downloading_Icons(){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                jLabel13.setText("Downloading...");
                jLabel13.setIcon(StaticValues.double_ring_x_55);
            }
        });
    }

        public class CheckConnectivity_TimerTask extends TimerTask{

        @Override
        public void run() {
            //stop the timer task if flag is false
           if(!thread_Run){
               this.cancel();
           }
        
                 //if ip address is null then user isn't on a network
           ip_Address = MyMethods.GetAddress("ip");
           
           if(!StringUtils.isEmpty(ip_Address) ){ //if ip address is not empty then we have some connnectivity. display green icon
                jLabel21.setIcon(StaticValues.green_rectangle_16_x_8);
           }
           else{
               ip_Address = "";
               jLabel21.setIcon(StaticValues.loading_gif_3_16_x_8); 
               /*
               error_Message = "Connectivity so unstable.";
                  //display error icon
                 An_Error();
                 JOptionPane.showMessageDialog(Dialog_Download_Files.this, "The connection completely broke at some point."
                         + "\nSession will now close!", "Error!", JOptionPane.ERROR_MESSAGE);
                 
                 Dialog_Download_Files.this.dispose(); //close the window
                 */
           }
           
        }
    }
        
        //timer to alway check if device is downloading. If files are being requested, we check if the bytes downloaded have changed. If they haven't then download stalled
        public class Check_If_Downloading_Has_Stalled extends TimerTask{
            long bytes_Previously_Downloaded; //will keep the previous count on the number of bytes downloaded. It will be used to compare with var 'bytes_Downloaded' that keeps track of total downloads. If same  then download stalled
            int counter; 
            
            @Override
                public void run() {
                    
                              //stop the timer task if flag is false
                   if(!thread_Run){
                       this.cancel();
                   }
                   
                   //if we have an error we return 
                   if(!StringUtils.isEmpty(error_Message))
                       return;
                   
                   //no need to proceed if percenctage sent is 100
                   if(percentage_Downloaded == 100)
                       return;
                   
                    //if the number of stalls exceeds a certain threshold then prompt the user whether to continue waiting
                 if(counter  == 5){
                //Ask user for confirmation
                        int n = JOptionPane.showConfirmDialog(Dialog_Download_Files.this, "Sometimes waiting abit may help."
                                + "\nDo you like to wait a while incase it reconnects?", "Download stalled.", JOptionPane.YES_NO_OPTION);

                            if(n != JOptionPane.YES_OPTION){ //if user doesn't yes reset counter
                                   //close dialog and quit
                                Dialog_Download_Files.this.dispose(); //close the window
                            }
                            else{
                                counter = 0;
                            }
                    }
                   
                 

                    //when downloading is on going 'bytes_Downloaded' will change 
                    if(bytes_Previously_Downloaded != bytes_Downloaded){ //this means ok. Actually downloading is going on
                        bytes_Previously_Downloaded = bytes_Downloaded; //keep the number of bytes downloaded so far for the next time when comparing.
                    
                              //change to animation icon
                          Downloading_Icons();
                    }
                    else{ //if 'bytes_Previously_Downloaded' is equal to 'bytes_Downloaded' then it means we haven't any download progress so download has probably stalled
                        System.err.println("download stalled!");
                        A_Warning("Download seems to have stalled!");
                        
                         counter ++; //increment threshold counter
                    }
                    
               
        }
            
        }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jProgressBar3 = new javax.swing.JProgressBar();
        jPanel11 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jProgressBar4 = new javax.swing.JProgressBar();
        jLabel32 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBackground(StaticValues.color_dark);
        if(StaticValues.military_theme){
            try {
                Image image = ImageIO.read(getClass().getResource(File.separator + "images" + File.separator + "Woodland-Camo_x_500.jpg"));
                jPanel4 = new ImagePanel(image, true);
            } catch (IOException ex) {
                Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        jLabel13.setBackground(StaticValues.color_dark);
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/download-2_x_55.png"))); // NOI18N
        jLabel13.setText("Downloading");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel13.setOpaque(true);
        jLabel13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jPanel6.setBackground(StaticValues.color_dark);

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Expected items:");

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Size:");

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText(" ");

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText(" ");

        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Network connectivity:");

        jLabel21.setText(" ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addContainerGap(460, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)))
        );

        jPanel9.setBackground(StaticValues.color_dark);
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jProgressBar3.setStringPainted(true);

        jPanel11.setBackground(StaticValues.color_dark);

        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("0");

        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("0");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel33))
                .addGap(3, 3, 3))
        );

        jLabel42.setBackground(new java.awt.Color(255, 255, 204));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("  ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel10.setBackground(StaticValues.color_dark);
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Total Progress");

        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Items recieved:");

        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Size:");

        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText(" ");

        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText(" ");

        jProgressBar4.setStringPainted(true);

        jLabel32.setText("Failed:");

        jLabel35.setText(" ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel40))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel35))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dialog_Download_Files.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dialog_Download_Files.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dialog_Download_Files.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dialog_Download_Files.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dialog_Download_Files dialog = new Dialog_Download_Files(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JProgressBar jProgressBar4;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
