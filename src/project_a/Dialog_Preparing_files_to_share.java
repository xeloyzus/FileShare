/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import Custom_Listeners.Exit_Listener;
import Custom_Listeners.Memory_Threshold_Listener;
import Custom_Listeners.Upload_Listener;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

/**
 *
 * @author vian
 */
public class Dialog_Preparing_files_to_share extends javax.swing.JDialog implements Upload_Listener, Memory_Threshold_Listener, Exit_Listener{
    RecieveCommand recieveCommand;
    //static vars
    public static JLabel network_connectivity_label;
    public static JLabel main_Icon_Label;
    public static JLabel items_Requested_Label; //will show the number of items requested
    public static JLabel size_Of_Requested_Items_Label; //will show the size of the items requested
    public static JProgressBar total_Progress_ProgressBar; //will show the percentage of the progress
    
    public ArrayList<String> filePaths = new ArrayList<>(); //keep file paths of the files selected
    
    //-- will contain the info ready for recipient to ask for, only files successfully located are considered, links are disregarded even if user has in selection
   public static  ArrayList <String> root_Selection_File_Names = new ArrayList<>(); //keep the names of the files
    public static ArrayList <String> root_Selection_File_Paths = new ArrayList<>();
   public static ArrayList<String> root_Selection_Icon_Types = new ArrayList<>(); //will keep the names of icons of files. Will be used as a reference when populating the table
    public static ArrayList <String> root_Selection_File_Sizes = new ArrayList<>(); //will keep the sizes of files or folders in selection
    //-----------------------------
    
      //maps to keep files and folders and info (file size) for folders we need to keep the files with. file paths are the keys
   // HashMap <String, Object> files_info = new HashMap<>();
    public static HashMap <String, Object> folders_Info = new HashMap<>(); //contain the directories in the user's root selection and all files that belong to that specific folder recursively
    
    //for keeping info about files to be shared. (files in folders and subfolders of root selection)
    public static ArrayList <String> all_File_Names = new ArrayList<>();
    public static ArrayList <String> all_File_Sizes = new ArrayList<>();
    public static ArrayList <String> all_File_Paths = new ArrayList<>();
    public static ArrayList <String> all_Icon_Types = new ArrayList<>(); //whether directory, link or regular file
    
    //for keeping information about recursed files
     ArrayList <String> file_names_list_recursive = new ArrayList<>();
    ArrayList <String> file_sizes_list_recursive = new ArrayList<>();
    ArrayList <String> file_paths_list_recursive = new ArrayList<>();
    ArrayList <String> file_types_list_recursive = new ArrayList<>(); //whether directory, link or regular file
    
    long content_size_for_folder = 0L; //size of n a specific folder to be shared
    public static long content_Size_For_Everything = 0L; //size of everything to be shared.
    public static long total_Number_Of_Items; //the total items in all folders inclusive
    
    Thread thread; //thread to compute file info. should close when dialog disposes
    Boolean threadRun = true; //false for thread for loops in thread to stop
    
    CheckConnectivity_TimerTask checkConnectivity_TimerTask;
    Timer timer; //timer to check for connectivity
    
    String ipAddress; //ip address of the device acting to serve files
    
    boolean waiting_for_client = false; //true when waiting for a client
    
    ServerSocket serverSocket;
    int listening_Port;
    Socket socket;
    private boolean listen_for_connection;
   
    String error_Message;
    
    boolean still_Preparing = true; //will be false after preparing files. While preparing sockets cant be open.
    Timer timer2;
    public Check_For_Stalled_Upload check_For_Stalled_Upload;
  
    Runtime runtime;
    
    /**
     * Creates new form Dialog_Preparing_files_to_share
     */
    public Dialog_Preparing_files_to_share(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        runtime = Runtime.getRuntime();
        
        if(StaticValues.military_theme){
            Set_Military_Theme();
        }
        
        //underline some labels
        MyMethods.JLabel_Underline(jLabel8);
       // MyMethods.JLabel_Underline(jLabel13);
        
        jLabel9.setText(" ");
        jLabel10.setText(" ");
        
         this.setLocationRelativeTo(parent);
    }
    
    public Dialog_Preparing_files_to_share(java.awt.Frame parent, ArrayList <String> filePaths){
        this(parent, true); //call constructor
     
        this.filePaths = filePaths;
      
        //reset static values
        root_Selection_File_Names.clear();
        root_Selection_File_Paths.clear();
        root_Selection_Icon_Types.clear(); //will keep the names of icons of files. Will be used as a reference when populating the table
        root_Selection_File_Sizes.clear(); //will keep the sizes of files or folders in selection
    //-----------------------------
    
      //maps to keep files and folders and info (file size) for folders we need to keep the files with. file paths are the keys
      folders_Info.clear(); //contain the directories in the user's root selection and all files that belong to that specific folder recursively
    
    //for keeping info about files to be shared. (files in folders and subfolders of root selection)
         all_File_Names.clear();
         all_File_Sizes.clear();
         all_File_Paths.clear();
        all_Icon_Types.clear(); //whether directory, link or regular file
    
        content_size_for_folder = 0L; //size of n a specific folder to be shared
        content_Size_For_Everything = 0L; //size of everything to be shared.
        total_Number_Of_Items = 0L; //the total items in all folders inclusive
        
        //reset values that keep count of the number of items expected and the total size
        StaticValues.number_Of_Requested_Items = 0L;//will have the number of items requested if requested for files. 
        StaticValues.size_of_Requested_Items = 0L; //will have the size of the items requested
        StaticValues.bytes_Sent = 0L;
        StaticValues.percentage_Sent = 0;
        StaticValues.counter2 = 0;
        StaticValues.sending_Data = false;
        
        jProgressBar1.setValue(0);
        
        PrepareFiles();
       
        loadStatics();
        
        
        //add a window listener. Register to recieve custom events when window is showing and register when it is not
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                //add to recieve custom events
                RecieveCommand.add_Upload_Listener(Dialog_Preparing_files_to_share.this); 
                //add to recieve custom events
                RecieveCommand.add_Memory_Threshold_Listener(Dialog_Preparing_files_to_share.this); 
                
                MainInterface.add_Exit_Listener(Dialog_Preparing_files_to_share.this);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //remove from recieving custom events
                RecieveCommand.remove_Upload_Listener(Dialog_Preparing_files_to_share.this); 
                //remove from recieving custom events
                RecieveCommand.remove_Memory_Threshold_Listener(Dialog_Preparing_files_to_share.this); 
                
                MainInterface.remove_Exit_Listener(Dialog_Preparing_files_to_share.this);
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

    
    //this class is notified when sending data starts
    @Override
    public void sending_Started() {
        System.out.println("Sending started...");
        //start checking if uploading has stalled using a timer. Only if we aren't already sending else multiple instances create other threads
        if(!StaticValues.sending_Data){
            
            check_For_Stalled_Upload = new Check_For_Stalled_Upload();
            timer2 = new Timer();

            timer2.schedule(check_For_Stalled_Upload, 30000, 30000); //wait for 'half a minute' intervals
            
            //set flag to true
            StaticValues.sending_Data = true;
        }
        
    }

    @Override
    public void sending_Stopped(String message) {
        System.err.println("error:" + message);
        
        //stop checking for stalled upload
        if(timer2 != null){
            timer2.cancel();
            timer2 = null;
        }
        
        if(check_For_Stalled_Upload != null){
            check_For_Stalled_Upload.cancel();
            check_For_Stalled_Upload = null;
        }
        
        //set flag to false
        StaticValues.sending_Data = false;
        }

    @Override
    public void Post_Upload_Progress(int percentage_Sent) {
        Post_Progress(percentage_Sent);
    }


    public void Set_Military_Theme(){
        jLabel1.setBackground(StaticValues.gray_1);
        jLabel1.setBorder(StaticValues.border);
      
        jPanel2.setBackground(StaticValues.gray_1);
        jPanel2.setBorder(StaticValues.border);
        
        jPanel3.setBackground(StaticValues.gray_1);
        
        jPanel4.setBackground(StaticValues.gray_1);
        
        jPanel5.setBackground(StaticValues.gray_1);
        
        //resize dialog coz some borders are more taller
        Dialog_Preparing_files_to_share.this.setSize(jPanel1.getWidth(), jPanel1.getHeight() + 80);
    }

    //listen for the possibility that the string may generate out of memory error if there is attempt to convert the map to string before writing over the socket
    @Override
    public void Threshold_Exceeded() {
        Thread thread = new Thread(){
           public void run(){
                       System.err.println("Possibility string may cause out memory error");
                //inform user
                jLabel1.setIcon(StaticValues.error_x_55);
                jLabel1.setText("Fatal error!");

                JOptionPane.showMessageDialog(Dialog_Preparing_files_to_share.this, "Try selecting fewer folders.", "Memory limitation!", JOptionPane.ERROR_MESSAGE);
                //quit the window
                Dialog_Preparing_files_to_share.this.dispose();
           } 
        };

        thread.start();
    }

    @Override
    public void exit_Listener() {
        Dialog_Preparing_files_to_share.this.dispose();
    }
    
    //check whether sending data has stalled
    public class Check_For_Stalled_Upload extends TimerTask{
            long previous_Number_Of_Bytes_Sent;
            
            @Override
            public void run(){
                       /*
                        System.out.println("timer start...");
                        System.out.println("prev:" + previous_Number_Of_Bytes_Sent);
                        System.out.println("curr:" + StaticValues.bytes_Sent);
                        System.out.println("___________________");
                        */
                        if(previous_Number_Of_Bytes_Sent < StaticValues.bytes_Sent){
                            //update 'previous_Number_Of_Bytes_Sent' so as to be comparable
                             previous_Number_Of_Bytes_Sent = StaticValues.bytes_Sent;
                        }
                        else
                         //if previous bytes sent count is same as the current count for bytes sent then we have a stalled downloade
                         if(previous_Number_Of_Bytes_Sent == StaticValues.bytes_Sent){
                             System.out.println("file sending has stalled!!");
                             
                                //close the sockets so that recipient doesn't reconnect
                                              //close server socket
                                CloseServerSocket();

                                            //stop sending any data
                                if(StaticValues.socket2 != null)
                                     try {
                                                //close socket if it is sending any files
                                                StaticValues.socket2.close();
                                                StaticValues.socket2 = null;

                                            } catch (IOException ex) {
                                                Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                          
                                        error_Message = "Sending stalled.";
                                        An_Error();
                                        JOptionPane.showMessageDialog(Dialog_Preparing_files_to_share.this, "Sending didn't complete. Please try again.", "Sending failed", JOptionPane.ERROR_MESSAGE);
                                        Refresh();
                         }
                      
                            
                            System.out.println("timer end...");
               
       
            }
        
    }
    
    public void loadStatics(){
        network_connectivity_label = jLabel7;
        main_Icon_Label = jLabel1;
        items_Requested_Label = jLabel17;
        size_Of_Requested_Items_Label = jLabel18;
        total_Progress_ProgressBar = jProgressBar1;
    }
    
    //count items and prepare them for final sharing
    public void PrepareFiles(){

        thread = new Thread(){

            @Override
            public void run() {
                super.run(); 
                           //reset necessary values
                total_Number_Of_Items = 0L; //should reset to zero
                content_Size_For_Everything = 0L; //should reset to zero

        //loop through file paths available and populate maps to contain information for files to be finally shared
                File file;
                Path path;
        
                if(!filePaths.isEmpty()){
                    for(String file_path : filePaths){

                          //if the flag is false we should break from loop so as to stop thread
                        if(!threadRun){
                            break;
                        }
                        
                        try{
                            file = new File(file_path);

                             path = Paths.get(file_path);   

                      //if it's a link, avoid!
                           if(Files.isSymbolicLink(path)){
                                System.out.println("link!!");
                                 continue;
                                }

                            //if it is a directory then we loop recursively and get to all the content
                            if(file.isDirectory()){
                                //reset list that will have all recursed file information of this folder selected in the root selection
                                 file_names_list_recursive.clear();
                                 file_sizes_list_recursive.clear();
                                 file_paths_list_recursive.clear();
                                 file_types_list_recursive.clear(); //whether directory, link or regular file

                                 content_size_for_folder = 0L;

                                 filesInFolderRecursively(file_path);

                                 //-- keep the recursive info in new lists (objects) so as not to be changed
                                 ArrayList <String> file_names_list = new ArrayList<>();
                                 ArrayList <String> file_sizes_list = new ArrayList<>();
                                 ArrayList <String> file_paths_list = new ArrayList<>();
                                 ArrayList <String> file_types_list = new ArrayList<>();
                                 
                                file_names_list.addAll(file_names_list_recursive);
                                file_sizes_list.addAll(file_sizes_list_recursive);
                                file_paths_list.addAll(file_paths_list_recursive);
                                file_types_list.addAll(file_types_list_recursive);
                                
                                 //keep folder and file info
                                 HashMap<String, Object> hashMap = new HashMap<>();
                                 hashMap.put("file_names_list", file_names_list);
                                 hashMap.put("file_sizes_list", file_sizes_list);
                                 hashMap.put("file_paths_list", file_paths_list);
                                 hashMap.put("file_types_list", file_types_list);
                                 hashMap.put("content_size_for_folder", content_size_for_folder + content_size_for_folder);

                                 //keep the folder and it's file info
                                 folders_Info.put(file_path, hashMap);
                       
                                 
                                 /*
                                 System.out.println("root folder:" + file_path);
                                 System.out.println("list size:" + file_names_list_recursive.size());
                                 */
                                 
                                 //keep in info about everything in root selection
                                 root_Selection_File_Paths.add(file_path);
                                 root_Selection_File_Names.add(file.getName());
                                 root_Selection_Icon_Types.add("folder"); //will indicate that it a folder
                                 root_Selection_File_Sizes.add("" + content_size_for_folder);
                                 
                                 
                                 /*
                                 //************optional, incase interested in adding folder size to total content size. However the alternative is add file sizes of the folder while recursing
                                 
                                 //increment the size of total content. add the folder size
                                 content_size_for_everything += content_size_for_folder;

                                 //System.out.println("folder:" + file.getName() + " size:" + MyMethods.humanReadableByteCount(content_size_for_folder, true));
                                        //show size of items
                                java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                        jLabel5.setText(MyMethods.humanReadableByteCount(content_size_for_everything, true));
                                    }
                                });
                                 //**********************
                                 */
                            }
                            else{ //regular file
                                   String file_type = "file";
                                   String file_size = file.length() + ""; 
                                   String file_name = file.getName(); //name of file
                                   String path_of_file = file.getAbsolutePath(); //path of the file

                                   all_File_Names.add(file_name);
                                   all_File_Sizes.add(file_size);
                                   all_File_Paths.add(path_of_file);
                                   all_Icon_Types.add(file_type); //whether directory, link or regular file

                                   //increment total number of items
                                  total_Number_Of_Items ++;

                                      //show number of items
                                java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                        jLabel4.setText(total_Number_Of_Items + "");
                                    }
                                });

                                 //increment the size of total content. add the folder size
                                 content_Size_For_Everything += file.length();

                               //  System.out.println("file:" + file.getName() + " size:" + MyMethods.humanReadableByteCount(content_size_for_folder, true));
                                     //show size of items
                                java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                        jLabel5.setText(MyMethods.humanReadableByteCount(content_Size_For_Everything, true));
                                    }
                                });
                                
                                    //keep in info about everything in root selection
                                 root_Selection_File_Paths.add(path_of_file);
                                 root_Selection_File_Names.add(file_name);
                                 root_Selection_Icon_Types.add(file_type); //will indicate that it a folder
                                 root_Selection_File_Sizes.add("" + file_size);

                            }
                        }
                        catch(Exception e){ 
                            e.printStackTrace();
                        }
                       
                        
                             //prediction of too much folder nesting. This counter increments whenever a folder is recursed. Without a check we may get out of memory error
                       /*
                             if(some_Counter > 30000){
                           
                            JOptionPane.showMessageDialog(Dialog_Preparing_files_to_share.this, "Try selecting fewer folders not within other folders.\n", "Too much folder nesting!", HEIGHT);
                            Dialog_Preparing_files_to_share.this.dispose();
                            
                            break;
                         }
                        */
                       
                    }
                }
                else{
                    System.out.println("selection empty:");
                }
                
   
                     //set flag to false
               still_Preparing = false; //means sending socket is free to operate and it's ok to even refresh
                //loop has finished. Check for network connectivity
               Refresh();
             
               /*
          for(String key : folders_Info.keySet()){
            System.out.println("key:" + key);
            HashMap <String, Object> folder_Information = (HashMap)folders_Info.get(key);
              //get the arraylists and other information about this folder contained in the map
              ArrayList <String> file_paths_list = (ArrayList)folder_Information.get("file_paths_list");
              for(String string : file_paths_list)
              System.out.println("path:" + string);
        }
          */
          
            }
        };
        thread.start();
    }
    
    //creates a new timer task and schedules timer, Close server socket if not null and creates a new one
    public void Refresh(){
               //reset the 'mac address of the connected device' if empty it means that we can deal with another device to copy files
               StaticValues.mac_address_of_connected_device = "";
               
               //do not proceed if file preparation and calculations have not been finished
               if(still_Preparing)
                   return;
               
              //close server socket, new one will be created thus providing new credentials
              CloseServerSocket();
              
               if(timer != null)
                    timer.cancel();
               
                if(checkConnectivity_TimerTask != null)
                    checkConnectivity_TimerTask.cancel();
        
                CheckForNetworkConnectivity(); //display icons 
                       
                checkConnectivity_TimerTask = new CheckConnectivity_TimerTask();
                 timer = new Timer();
                 
                //if ip address is null then user isn't on a network
                ipAddress = MyMethods.GetAddress("ip");
                
                if(StringUtils.isEmpty(ipAddress)){
                 
                     timer.schedule(checkConnectivity_TimerTask, 3000, 2000); //will keep running. Cancel when window is closed
                }
                else{
                    //wait for recipient...
                      WaitForRecipient();
                      
                       timer.schedule(checkConnectivity_TimerTask, 3000, 2000); //will keep running. Cancel when window is closed
                }
                
                  //reset values that keep count of the number of items expected and the total size
            StaticValues.number_Of_Requested_Items = 0L;//will have the number of items requested if requested for files. 
            StaticValues.size_of_Requested_Items = 0L; //will have the size of the items requested
            StaticValues.bytes_Sent = 0L;
            StaticValues.percentage_Sent = 0;
            StaticValues.counter2 = 0;
            
            jProgressBar1.setValue(0);
            
             Post_Number_Of_Items_Requested_And_Size();//post the number of items and there size
        
        if(StaticValues.socket2 != null)
        try {
            //close socket if it is sending any files
            StaticValues.socket2.close();
            StaticValues.socket2 = null;
            
        } catch (IOException ex) {
            Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            //stop checking for stalled upload----
                             //stop checking for stalled upload
                        if(timer2 != null){
                            timer2.cancel();
                            timer2 = null;
                        }

                        if(check_For_Stalled_Upload != null){
                            check_For_Stalled_Upload.cancel();
                            check_For_Stalled_Upload = null;
                        }

                                
                          //set flag to false
                          StaticValues.sending_Data = false;
                          //--------
    }
    
    
    //cancels timer task and timer
    public void Stop_Timer(){
        
        if(checkConnectivity_TimerTask != null)
            checkConnectivity_TimerTask.cancel();

        if(timer != null)
            timer.cancel();
       
        //
    }
    
    //post the number of items and there size
    public static void Post_Number_Of_Items_Requested_And_Size(){
        Dialog_Preparing_files_to_share.items_Requested_Label.setText(StaticValues.number_Of_Requested_Items + "");
        Dialog_Preparing_files_to_share.size_Of_Requested_Items_Label.setText(MyMethods.humanReadableByteCount(StaticValues.size_of_Requested_Items, true));
    }
    
    //post progress to progress bar
    public  void Post_Progress(int percentage){
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                main_Icon_Label.setText("Sending...");
                main_Icon_Label.setIcon(StaticValues.double_ring_x_55);
                
               Dialog_Preparing_files_to_share.total_Progress_ProgressBar.setValue(percentage);
               
               if(percentage == 100){
                          //reset values for next session
                  
                   System.out.println("finished sending....");
            
                   main_Icon_Label.setText("Finished.");
                  main_Icon_Label.setIcon(StaticValues.success_x_55);
                
                      //close the sockets so that recipient doesn't reconnect
                       //close server socket
                   CloseServerSocket();

                                            //stop sending any data
                        if(StaticValues.socket2 != null)
                           try {
                                 //close socket if it is sending any files
                                  StaticValues.socket2.close();
                                  StaticValues.socket2 = null;

                                  } catch (IOException ex) {
                                                Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(Level.SEVERE, null, ex);
                                  }
                            
                        //stop checking for stalled upload----
                             //stop checking for stalled upload
                        if(timer2 != null){
                            timer2.cancel();
                            timer2 = null;
                        }

                        if(check_For_Stalled_Upload != null){
                            check_For_Stalled_Upload.cancel();
                            check_For_Stalled_Upload = null;
                        }

                                
                          //set flag to false
                          StaticValues.sending_Data = false;
                          //--------
                             
                         JOptionPane.showMessageDialog(Dialog_Preparing_files_to_share.this, "Finished sending.", "Finished", JOptionPane.INFORMATION_MESSAGE);
                         Refresh();
                
               }
            }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(StaticValues.color_dark);

        jPanel1.setBackground(StaticValues.color_dark);
        if(StaticValues.military_theme){
            try {
                Image image = ImageIO.read(getClass().getResource(File.separator + "images" + File.separator + "Woodland-Camo_x_500.jpg"));
                jPanel1 = new ImagePanel(image, true);
            } catch (IOException ex) {
                Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        jLabel1.setBackground(StaticValues.color_dark);
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loader_4.gif"))); // NOI18N
        jLabel1.setText("Preparing items");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setOpaque(true);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jPanel2.setBackground(StaticValues.color_dark);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Total items:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Size:");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText(" ");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText(" ");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Network connectivity:");

        jLabel7.setText(" ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)))
        );

        jPanel3.setBackground(StaticValues.color_dark);
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBackground(StaticValues.color_dark);

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Credentials (Required by recipient):");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Port:");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("iP Address:");

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText(" ");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText(" ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 69, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)))
                        .addContainerGap(38, Short.MAX_VALUE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh_x_55.png"))); // NOI18N
        jLabel19.setToolTipText("Close session and create new one");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(28, 28, 28))
        );

        jPanel4.setBackground(StaticValues.color_dark);
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Progress");

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Items requested:");

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Size:");

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText(" ");

        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText(" ");

        jProgressBar1.setValue(50);
        jProgressBar1.setStringPainted(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
      
        Refresh();
    }//GEN-LAST:event_jLabel19MouseClicked

    int some_Counter;
     Path path;
     File [] containingFiles;
         //get the files in a folder recursively. Store infomation including file size in static arraylists
  public void filesInFolderRecursively(String file_path){

      File files = new File(file_path);
      //if the file is not a directory then we return
      if(!files.isDirectory()){
          return;
      }

      containingFiles = files.listFiles();
      
        //if we don't have files at all then we return (just an empty folder)
      if(containingFiles == null || containingFiles.length == 0){ //for empty folder j return. This means the folder itself is reported but just empty
          
              //attributes we can add
         String file_name = FilenameUtils.getName(file_path); //name of file
         String path_of_file = file_path; //path of the file
         String file_type = ""; //whether 'folder', 'link', or regular file
         String file_size = "0"; //applies to regular files only
 
         file_type = "folder";
         
         
        file_names_list_recursive.add(file_name);
        file_sizes_list_recursive.add(file_size);
        file_paths_list_recursive.add(path_of_file);
        file_types_list_recursive.add(file_type); //whether directory, link or regular file
        
        //include empty folder in all files
            all_File_Names.add(file_name);
            all_File_Sizes.add(file_size);
            all_File_Paths.add(path_of_file);
             all_Icon_Types.add(file_type); //whether directory, link or regular file
       
     //   System.out.println("empty folder");
        //increment total number of items
        total_Number_Of_Items ++;
        //increment number of items
        
        
            //show number of items
        java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                        jLabel4.setText(total_Number_Of_Items + "");
                    }
         });
        
         return;
      }
      
            //if a file is a symbolic link we don't calculate it's size
     for(File file : containingFiles){
         //if the flag is false we should break from loop so as to stop thread
         if(!threadRun){
             break;
         }
              
         //if file doesn't exist we skip it and continue with the loop. This can happen if during the while the file is deleted, or user pulls out USB
         if(!file.exists())
             continue;
         
             //attributes we can add
         String file_name = file.getName(); //name of file
         String path_of_file = file.getAbsolutePath(); //path of the file
         String file_type = ""; //whether 'folder', 'link', or regular file
         String file_size = "0"; //applies to regular files only
         
        
         path = Paths.get(path_of_file);   
         
              //determine file type. Is it a folder, a link or a regular file. if a 'file' then we get it's size and keep it but we increment to get the folder size. 
              //for recursive effect, if a folder is detected then we call the method which gets files and sizes as the loop goes on.
         if(Files.isSymbolicLink(path)){
             file_type = "link";
             System.err.println("link!");
         }
         else
             if(file.isDirectory()){
                 file_type = "folder";
                 //call the method again for recursive effect
                 filesInFolderRecursively(path_of_file); //supply the file path of the folder
                  some_Counter ++;
             }
             else
                if(file.isFile()){
                    //optional, we can exclude hidden files
                    //if(file.isFile())
                 file_type = "file";
                 file_size = file.length() + ""; 
                 //increment the folderSize to get the total folder size
                 content_size_for_folder += file.length();
                 
                 //will be kept in relation to the folder they are contained
                file_names_list_recursive.add(file_name);
                file_sizes_list_recursive.add(file_size);
                file_paths_list_recursive.add(path_of_file);
                file_types_list_recursive.add(file_type); //whether directory, link or regular file

                //will be kept with all files. So as to easily maintain references to all items. i.e to check if a requested file is ok to be shared, we check for prescences of it's path
                all_File_Names.add(file_name);
                all_File_Sizes.add(file_size);
                all_File_Paths.add(path_of_file);
                all_Icon_Types.add(file_type); //whether directory, link or regular file
                
                //increment total number of items
                total_Number_Of_Items ++;
               
  
                //show number of items
                java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                        jLabel4.setText(total_Number_Of_Items + "");
                    }
                });
                
                //**** optionally folder sizes can be added to total content size after recursing a folder, however below files sizes are added one at a time
              //increment the size of total content. add the folder size
                content_Size_For_Everything += file.length();
                
                //show size of items
                java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                    jLabel5.setText(MyMethods.humanReadableByteCount(content_Size_For_Everything, true));
                  }
                 });
                //****************
                
             //   System.out.println("file name:" + file_name + " -> " + file_path );
          // System.out.println("some counter:" + some_Counter);
             }
         
         /*
         //if folder don't add. only empty folder paths are regarded and are dealt with with a files in a folder are '0' as seen above
         if(!StringUtils.equals(file_type, "folder")){
//                System.out.println("file type:" + fileType);
//                System.out.println("file path:" + filePath);
                //keep the attributes in there respective arraylists
                  
                file_names.add(file_name);
                file_sizes.add(file_size);
                file_paths.add(path_of_file);
                file_types.add(file_type); //whether directory, link or regular file
         }
         */
      
     }
 
     
     //if we have less than 5Mb of what the JVM (heap) is available then this window should close. We avoid out of memory error
     if( runtime.freeMemory() <= 5000000){
             System.err.println("Free:" +MyMethods.humanReadableByteCount(Runtime.getRuntime().freeMemory(), true));
            JOptionPane.showMessageDialog(Dialog_Preparing_files_to_share.this, "Try selecting fewer folders not within other folders.\n", "Too much folder nesting!", HEIGHT);
             Dialog_Preparing_files_to_share.this.dispose();
             
             //clear the static arraylists coz they will keep holding memory
           filePaths = null; 
    
    //-- will contain the info ready for recipient to ask for, only files successfully located are considered, links are disregarded even if user has in selection
  root_Selection_File_Names .clear(); //keep the names of the files
    root_Selection_File_Paths .clear();
   root_Selection_Icon_Types .clear(); //will keep the names of icons of files. Will be used as a reference when populating the table
    root_Selection_File_Sizes .clear(); //will keep the sizes of files or folders in selection
    //-----------------------------
    
      //maps to keep files and folders and info (file size) for folders we need to keep the files with. file paths are the keys
   // HashMap <String, Object> files_info = new HashMap<>();
    folders_Info .clear(); //contain the directories in the user's root selection and all files that belong to that specific folder recursively
    
    //for keeping info about files to be shared. (files in folders and subfolders of root selection)
    all_File_Names .clear();
    all_File_Sizes .clear();
    all_File_Paths .clear();
    all_Icon_Types .clear(); //whether directory, link or regular file
    
    //for keeping information about recursed files
     file_names_list_recursive .clear();
    file_sizes_list_recursive .clear();
    file_paths_list_recursive .clear();
    file_types_list_recursive.clear(); //whether directory, link or regular file
            System.gc();
     }
     
     
        }

    @Override
    public void dispose() {
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
        threadRun = false; //flag for all loops in thread not to continue
        listen_for_connection = false;
        StaticValues.mac_address_of_connected_device = "";
        //close server socket
        CloseServerSocket();
        
        //stop sending any data
      if(StaticValues.socket2 != null)
        try {
            //close socket if it is sending any files
            StaticValues.socket2.close();
            StaticValues.socket2 = null;
            
        } catch (IOException ex) {
            Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(Level.SEVERE, null, ex);
        }
      
     filePaths = null; 
    
    //-- will contain the info ready for recipient to ask for, only files successfully located are considered, links are disregarded even if user has in selection
  root_Selection_File_Names .clear(); //keep the names of the files
    root_Selection_File_Paths .clear();
   root_Selection_Icon_Types .clear(); //will keep the names of icons of files. Will be used as a reference when populating the table
    root_Selection_File_Sizes .clear(); //will keep the sizes of files or folders in selection
    //-----------------------------
    
      //maps to keep files and folders and info (file size) for folders we need to keep the files with. file paths are the keys
   // HashMap <String, Object> files_info = new HashMap<>();
    folders_Info .clear(); //contain the directories in the user's root selection and all files that belong to that specific folder recursively
    
    //for keeping info about files to be shared. (files in folders and subfolders of root selection)
    all_File_Names .clear();
    all_File_Sizes .clear();
    all_File_Paths .clear();
    all_Icon_Types .clear(); //whether directory, link or regular file
    
    //for keeping information about recursed files
     file_names_list_recursive .clear();
    file_sizes_list_recursive .clear();
    file_paths_list_recursive .clear();
    file_types_list_recursive.clear(); //whether directory, link or regular file
    }
    
           //display icons 
    public void A_Warning(String message){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
          
              jLabel1.setText(message);
                
                jLabel1.setIcon(StaticValues.warning_x_55);
            }
        });
    }
    
           //display icons 
    public void An_Error(){
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
           if(StringUtils.isEmpty(error_Message))
                  jLabel1.setText("There was an error!");
                else
                     jLabel1.setText(error_Message);
                
                jLabel1.setIcon(StaticValues.error_x_55);
            }
        });
    }
    
    public class CheckConnectivity_TimerTask extends TimerTask{

        @Override
        public void run() {
            //stop the timer task if flag is false
           if(!threadRun){
               this.cancel();
           }
           
                 //if ip address is null then user isn't on a network
           ipAddress = MyMethods.GetAddress("ip");
   
           if(!StringUtils.isEmpty(ipAddress)){ //if ip address is not empty then we have some connnectivity. Cancel the timer task
                   //wait for recipient...
               if(!waiting_for_client){
                    WaitForRecipient();
               }
         
           }
           else{
               if(waiting_for_client)
                    CheckForNetworkConnectivity();
           }
      
        }
    }
    
    //make changes to necessary icons when checking for connectivity
    public void CheckForNetworkConnectivity(){
        waiting_for_client = false; //set flag to false
        
        jLabel9.setText(" ");
        jLabel10.setText(" ");
        jLabel11.setText(" ");
        jLabel12.setText(" ");
        
        jLabel14.setIcon(null); //don't show qr code
        
         jLabel1.setText("Checking for network connectivity...");
         jLabel1.setIcon(StaticValues.internet_x_55);
         jLabel7.setIcon(StaticValues.loading_gif_3_16_x_8);
    }
    
    //make necessary changes to icons when waiting for recipient
    public void WaitForRecipient(){
        waiting_for_client = true; //set flag to true;
        
        jLabel9.setText("iP Address:");
        jLabel10.setText("Port:");
        
  
        jLabel1.setText("Waiting for recipient...");
    
        
        jLabel1.setIcon(StaticValues.double_ring_x_55);
        jLabel7.setIcon(StaticValues.green_rectangle_16_x_8);
        
        //open the server socket to expect incoming instructions
        if(!listen_for_connection){ //if listening thread is not alive it means we server socket may not be open
            OpenServerSocket(); //open server socket and listen for incoming commands
        }
        
        //expose ip address and port number to user
        jLabel11.setText(ipAddress);
        jLabel12.setText(listening_Port + "");
        
        //create json object containing the ip address and port. this will be the information used to create the qr code which can be scanned by the recipient
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ipAddress", ipAddress);
        jsonObject.put("listening_Port", listening_Port);
        
        BufferedImage qrCode_BufferedImage = QrCodeImage(jsonObject);
        //convert buffered image to image icon so as to display in JLable
        jLabel14.setIcon(MyMethods.imageIcon_From_BufferedImage(qrCode_BufferedImage));
        
        //if we are still having a connection then we don't show animation
        if(!StringUtils.isEmpty(StaticValues.mac_address_of_connected_device)){
         
            jLabel1.setText("Waiting for selection confirmation.");
          // jLabel1.setIcon(StaticValues.user_x_55);
           jLabel1.setIcon(StaticValues.list_x_55);
            
        }
    }
    
    
           //returns true if server socket is open or already open. When server socket is open, the listening thread for incoming connections is also active
     public  boolean OpenServerSocket(){
       boolean isOpen = false;
        // Try to open a server socket on port 'serverSocketPort'. Note that we can't choose a port less than 1023 if we are not privileged users (root)
         if(serverSocket == null){
            try {
                //try and open a new socket, if we fail we print an error message
                serverSocket  = new ServerSocket(0); //we get a random port
                listening_Port = serverSocket.getLocalPort(); //save the listening port number to a static variable
               
                //start listening thread
                listen_for_connection = true; //flag for whether thread listening for connection should do so
                ListenForConnection();
                isOpen = true;
            } catch (IOException ex) {
                isOpen = false;
                ex.getStackTrace();          
            
            }
        }
         else{
             isOpen = true;
             System.out.println("Server is already open");
                             
         }
         
         return isOpen;
    }
       
        //terminate socket which will terminate server connection with clients
    public void CloseServerSocket(){
        listen_for_connection = false; //set flag to false so that thread listening for incoming commands should stop
        
        if(serverSocket != null){
            try {
                serverSocket.close();  
                serverSocket = null; 
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
                  while(listen_for_connection){
           //    System.out.println("Thread running");
                      try {
                          /*
                          //if server socket is closed, we break from the loop
                          if(serverSocket.isClosed()){
                              System.err.println("server socket closed.");
                              break;
                          }
                          //if socket is null or is closed, we still break from the loop
                          if(socket == null || socket.isClosed()){
                              System.err.println("socket is null or closed.");
                              break;
                          }
                          */
                          socket = serverSocket.accept();
                         
                             recieveCommand = new RecieveCommand(socket);
                
//                           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                          DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
//                           String clientSentence = inFromClient.readLine();
//                           System.out.println("Received: " + clientSentence);
//                           String  capitalizedSentence = clientSentence.toUpperCase() + '\n';
//                           outToClient.writeBytes(capitalizedSentence); 
//                          
//                          System.out.println("connected..." + this.getId());
//                          JOptionPane.showMessageDialog(null, "connected");                       
                      } 
                      catch(SocketException ex){ //socket closed
                          ex.printStackTrace(); 
                      }
                      catch (IOException ex) {
                       //   JOptionPane.showMessageDialog(null, ex.getMessage().toString() + " \n" + ex.toString());
                          ex.printStackTrace();
                      }
                  }
              } 
           };
           thread.start();     
    }
         
         //return a buffered image containing data in json format
         public BufferedImage QrCodeImage(JSONObject jsonObject){
                String json_Escaped_String = MyMethods.escapeJSON(jsonObject.toJSONString()); //escape the json string incase of troublesome characters like slash characters
            
                 // Generate QR code to output stream.
                ByteArrayOutputStream output = new ByteArrayOutputStream();
	QRCode.from(json_Escaped_String).to(ImageType.PNG).withColor(0x0064FF00, 0xFFFFFFAA).withSize(80, 80).writeTo(output);
                                                                                                                        
                BufferedImage bufferedImage = null;
                
	// Create BufferedImage from input stream.
	byte[] data = output.toByteArray();
	ByteArrayInputStream input = new ByteArrayInputStream(data);
	
	try {
                    bufferedImage = ImageIO.read(input);
	} catch (IOException e) {
	e.printStackTrace();
	}
             
                return bufferedImage;
         }
         
         //digests instructions from devices
         public void RecieveCommand(Socket socket){
             
         }
         
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
            java.util.logging.Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dialog_Preparing_files_to_share.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dialog_Preparing_files_to_share dialog = new Dialog_Preparing_files_to_share(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
