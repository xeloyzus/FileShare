/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;


import Custom_Listeners.Memory_Threshold_Listener;
import Custom_Listeners.Upload_Listener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author vian
 */
public class RecieveCommand {
    DialogConnectToServer dialogConnectToServer;
    JFrame_confirm_selection frame_confirm_selection;

    
    Socket socket;
    BufferedReader dataFromClient;
    DataOutputStream dataToClient;
    DataInputStream dataInputStream;
    JSONObject jsonObject;
    String command;
    String clientData;
    String incoming_Mac_Address;
    String incoming_Ip_Address;
    String incoming_Port;
    String success;
    
    SendCommand sendCommand;
    
    public static List<Upload_Listener> upload_Listeners = new ArrayList<>(); //listen to upload progress while socket sends file data requested
    public static List<Memory_Threshold_Listener> memory_Threshold_Listeners = new ArrayList<>(); //listen incase string may exceed predicted limit. This is to avoid out of memory error
  
        
    public RecieveCommand(Socket socket) {
        this.socket = socket;
//        System.out.println("server recieved");
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                ProcessSocketData();
            }
        };
        thread.start();
    }
    
    //constructor to use incase the dialog to connect to a server recieves an instruction. The intention is to be able to close the dialog and open 
    //another dialog that will show the progress but preserving information from the supplied socket
    public RecieveCommand(JFrame_confirm_selection frame_confirm_selection, DialogConnectToServer dialogConnectToServer, Socket socket){
        this.socket = socket;
        this.dialogConnectToServer = dialogConnectToServer;
        this.frame_confirm_selection = frame_confirm_selection;
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                ProcessSocketData();
            }
        };
        thread.start();
    }
    
    //uploading listeners------------
    //for custom listener. Windows should be able to recieve events concerning file upload
  public static void add_Upload_Listener(Upload_Listener toAdd) {
        upload_Listeners.add(toAdd);
    }
    
    //for custom listener. Windows should be removed from recieving events
  public static void remove_Upload_Listener(Upload_Listener toAdd) {
        upload_Listeners.remove(toAdd);
    }
  
    //uploading listeners------------
    //for custom listener. Windows should be able to recieve events if a string may exceed threshold. If unchecked will lead to out of  memory error
  public static void add_Memory_Threshold_Listener(Memory_Threshold_Listener toAdd) {
        memory_Threshold_Listeners.add(toAdd);
    }
    
    //for custom listener. Windows should be removed from recieving events
  public static void remove_Memory_Threshold_Listener(Memory_Threshold_Listener toAdd) {
        memory_Threshold_Listeners.remove(toAdd);
    }
    

    
     private void ProcessSocketData() {
        try {

            //get Input and Output streams
           // dataFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           dataInputStream = new DataInputStream(socket.getInputStream());
             dataFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         //   dataToClient = new DataOutputStream(socket.getOutputStream());
            //clientData = dataFromClient.readLine(); //string with JSON data, parse to JSON object
            clientData = dataFromClient.readLine(); //string with JSON data, parse to JSON object
           // InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(clientData.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8"));
       //    clientData = StringUtils.substring(clientData, 2); //skip the first two characters in the string if it is encoded with 'UTF-8'. dataOutStream.writeUTF() puts two bytes at the beginning of the stream. This solves character encoding issues

            if (clientData == null) {
                System.err.println("Client data is null");
                return;
            }

            //**parse string to JSON
            jsonObject = null;
            JSONParser parser = new JSONParser();
            Object object;

            try {
                object = parser.parse(clientData);
                jsonObject = (JSONObject) object;
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            //**end of 'parse string to jSon***

            if (jsonObject == null) {
                System.err.println("jsonObject null!");
                return;
            }
            //get the command and use a switch to act accordingly
            command = (String) jsonObject.get("command");
            System.out.println("command:" + command);
            success = (String)jsonObject.get("success");
            
            //get the mac address of the device that is sending the command
            incoming_Mac_Address = (String)jsonObject.get("mac_Address");
            //get the ip address of the device that is sending the command
            incoming_Ip_Address = (String)jsonObject.get("ip_Address");
            //get the mac address of the device that is sending the command
            incoming_Port = (String)jsonObject.get("port");
        
      
            
            switch(command){
             
                case "Initiate_Connection":
                    Initiate_Connection();
                    break;
             
                case "Connection_Initiation_Response":
                    Connection_Initiation_Response();
                    break;
                    
                case "Confirm_File":
                    Confirm_File();
                    break;
                    
                case "Confirmed_File_Results":
                    Confirmed_File_Results();
                    break;
                    
                case "Request_File":
                    Request_File();
                    break;
                    
                case "Send_File":
                    Send_File();
                    break;
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        }
        
        //when a device intends to initiate a session in order to get files
        public void Initiate_Connection(){
               
               //if incoming mac address is not the same as the mac address in communication the we send a reply that the server is still actively engaged
                //this can happen if already a device has connected and another connects after. We only deal with one device at a time
                if(!IsSameDevice()){
                    System.err.println("different device");
                    return;
                }
                
               
                //if currently busy i.e an ongoing file sending session then check if 'sending_Data' is true, return. Don't initiate multiple connections
                if(Is_Sending_Data()){
                    return;
                }
                
                String mac_Address = MyMethods.GetAddress("mac"); //send the mac address of local machine to requesting device. 
               
                //put information about files about to shared in a map
                HashMap<String, Object> info = new HashMap<>();
 
                //root selection info
                info.put("root_Selection_File_Paths", Dialog_Preparing_files_to_share.root_Selection_File_Paths);
                info.put("root_Selection_File_Names", Dialog_Preparing_files_to_share.root_Selection_File_Names);
                info.put("root_Selection_Icon_Types", Dialog_Preparing_files_to_share.root_Selection_Icon_Types);
                info.put("root_Selection_File_Sizes", Dialog_Preparing_files_to_share.root_Selection_File_Sizes);
                //everything info, useful when displaying contents in folders all at once i.e. expose all folder and sub folder contents in a single view
                info.put("all_File_Paths", Dialog_Preparing_files_to_share.all_File_Paths);
                info.put("all_File_Names", Dialog_Preparing_files_to_share.all_File_Names);
                info.put("all_Icon_Types", Dialog_Preparing_files_to_share.all_Icon_Types);
                info.put("all_File_Sizes", Dialog_Preparing_files_to_share.all_File_Sizes);
                
                info.put("folders_Info", Dialog_Preparing_files_to_share.folders_Info); //information specific to any folders that could be in the selection, information includes size, all files that reside within e.t.c
                info.put("content_Size_For_Everything", Dialog_Preparing_files_to_share.content_Size_For_Everything + "");
                info.put("total_Number_Of_Items", Dialog_Preparing_files_to_share.total_Number_Of_Items + "");
       
                //include the operating system of the server (local machine)
                info.put("os_Name", StaticValues.os_Name);
              //  System.out.println("size of root_Selection_File_Paths:" + Dialog_Preparing_files_to_share.root_Selection_File_Paths.size());
              
              boolean less_Memory = false; //will be true if we have little memory
              //use the string build to try and build the string. if the string builder is likely to cause out of memory error then it's likely we shall get out of memory error when converting to JSON string so we tell user to select fewer folders 
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Dialog_Preparing_files_to_share.root_Selection_File_Paths.toString());
               // System.out.println("Size 1:" + stringBuilder.length());
               if(has_Less_Heap_Memory())
                   less_Memory = true; 
              
               //only append if memory is good
               if(!less_Memory)
                stringBuilder.append(Dialog_Preparing_files_to_share.root_Selection_File_Names.toString());
              //  System.out.println("Size 2:" + stringBuilder.length());
                  if(has_Less_Heap_Memory())
                   less_Memory = true;
                  
                  //only append if memory is good
               if(!less_Memory) 
                stringBuilder.append(Dialog_Preparing_files_to_share.root_Selection_Icon_Types.toString());
                 //   System.out.println("Size 3:" + stringBuilder.length());
               if(has_Less_Heap_Memory())
                   less_Memory = true;
               
               //only append if memory is good
               if(!less_Memory)
                stringBuilder.append(Dialog_Preparing_files_to_share.root_Selection_File_Sizes.toString());
                 //   System.out.println("Size 4:" + stringBuilder.length());
              if(has_Less_Heap_Memory())
                   less_Memory = true;
                
               //only append if memory is good
               if(!less_Memory) 
                stringBuilder.append(Dialog_Preparing_files_to_share.all_File_Paths.toString());
                  //  System.out.println("Size 5:" + stringBuilder.length());
               if(has_Less_Heap_Memory())
                   less_Memory = true;
               
                 //only append if memory is good
               if(!less_Memory) 
                stringBuilder.append(Dialog_Preparing_files_to_share.all_File_Names.toString());
                  //  System.out.println("Size 6:" + stringBuilder.length());
               if(has_Less_Heap_Memory())
                   less_Memory = true;
               
                   //only append if memory is good
               if(!less_Memory) 
                stringBuilder.append(Dialog_Preparing_files_to_share.all_Icon_Types.toString());
                   // System.out.println("Size 7:" + stringBuilder.length());
               if(has_Less_Heap_Memory())
                   less_Memory = true;
               
                  //only append if memory is good
               if(!less_Memory) 
                stringBuilder.append(Dialog_Preparing_files_to_share.all_File_Sizes.toString());
            //        System.out.println("Size 8:" + stringBuilder.length());
                
                  if(has_Less_Heap_Memory())
                   less_Memory = true;
                  
                //check the string builder size and predict if the string won't create out of memory error when converted to String before being sent over the socket
              //  System.out.println("predicted string size:" + stringBuilder.length());
                
//send information about the files being shared
                command = "Connection_Initiation_Response";
                
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("command", command);
                hashMap.put("mac_Address", mac_Address);
           
            //    if(stringBuilder.length() > 25000000){ //not safe. possibly to much information. might cause out of memory error. heap memory issues, I think of too much directory nesting
                  if(less_Memory || stringBuilder.length() > 20000000){
                    hashMap.put("success", "no");
                    hashMap.put("message", "Selection may exceed memory limits of sender."); 
                    //notify the concerned window. Tell user that selection list cant be sent. May contain too much nesting. If not checked then conversion of the map before sending will cause out of memory error
                    for (Memory_Threshold_Listener  memory_Threshold_Listener : memory_Threshold_Listeners)
                              memory_Threshold_Listener.Threshold_Exceeded();
                }
                else{
                    //include the 'info'
                    hashMap.put("info", info);
                }
                
                System.out.println("ip:" + incoming_Ip_Address + " port:" + incoming_Port);
                //if either 'incoming_Ip_Address' is null or the 'incoming_Port' is null then the device tried re-initiation while server is still busy uploading
                //tell user server is still busy
                if(StringUtils.isEmpty(incoming_Port) || StringUtils.containsIgnoreCase(incoming_Port, "null")){
                        //change icon to show that server is still busy
//                    Dialog_Preparing_files_to_share.main_Icon_Label.setIcon(StaticValues.info_x_55);
//                    Dialog_Preparing_files_to_share.main_Icon_Label.setText("The device to connect to is still busy. Please wait."); 
                    
                    return;
                }

                //make the string buider null and try to induce garbage collector to collect it so as to free up some space
                stringBuilder.delete(0, (stringBuilder.length() - 1));
                stringBuilder = null;
                
                System.gc();
                
                sendCommand = new SendCommand(command, incoming_Ip_Address, Integer.parseInt(incoming_Port), hashMap);
                sendCommand.Excecute();
                
                //change icon to show that a connection is made
                Dialog_Preparing_files_to_share.main_Icon_Label.setIcon(StaticValues.list_x_55);
                Dialog_Preparing_files_to_share.main_Icon_Label.setText("Waiting for selection confirmation.");
           
        }
   
        public boolean has_Less_Heap_Memory(){
                 //if we have less than 5Mb of what the JVM (heap) is available then this window should close. We avoid out of memory error
            if(Runtime.getRuntime().freeMemory() <= 10000000)
                return true;
            else
                return false;
        }


        //reply recieved from server after attempting to initiate a connection
        public void Connection_Initiation_Response(){
            //stop loading animation, change flag
            DialogConnectToServer.connecting = false;
            
            //if server reports a problem, then success flag will be 'no'
            if(StringUtils.equalsIgnoreCase(success, "no")){
                String message = (String)jsonObject.get("message");
                JOptionPane.showMessageDialog(dialogConnectToServer, message, "Connection not successful", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String server_Mac_Address = (String)jsonObject.get("mac_Address");
            HashMap <String, Object> info = (HashMap)jsonObject.get("info");
            
            //keep the mac address of the server in a static variable
            DialogConnectToServer.server_Mac_Address = server_Mac_Address;
            
            DialogConnectToServer.connecting = true; //set this flag to true and later change to false so as frame not to close
            DialogConnectToServer.connected = true;
 
            frame_confirm_selection.dialogConnectToServer = dialogConnectToServer;
            frame_confirm_selection.info = info; //update the 'info' map of this object. Remember it is the current 
            frame_confirm_selection.Assign_Values(); //assign values of lists and variables using the 'info' map
            frame_confirm_selection.Populate_Table(); //populate the table
     
             DialogConnectToServer.connecting = false;
       
        }
        
        //if requested for file, confirm it exists and update file info such as file size
        public void Confirm_File(){
  
                           //if incoming mac address is not the same as the mac address in communication the we send a reply that the server is still actively engaged
                        //this can happen if already a device has connected and another connects after. We only deal with one device at a time
                if(!IsSameDevice()){
                            System.err.println("different device");
                            return;
                 }

                String mac_Address = MyMethods.GetAddress("mac"); //send the mac address of local machine to requesting device. 

                HashMap <String, Object> info = new HashMap<>(); //will have the confirmation information

                //get the info about the file to be requested
                String file_Path = (String)jsonObject.get("file_Path");
                /*
                String file_Size = (String)jsonObject.get("file_Size");
                String file_Type = (String)jsonObject.get("file_Type");
                */

                String file_size_reported = ""; //the file size of the file currently
                String file_exists = ""; //will be yes' if file exists or 'no' if it doesn't

                //unescape 'file_Path'
                file_Path = MyMethods.unEscapeJSON(file_Path);
                    //check that the file exists
                File file = new File(file_Path); 
                Path path = Paths.get(file_Path);
                    if(!file.exists()){
                        file_exists = "no";
                        info.put("success", "no");
                        info.put("message", "file does not exist");
                    }
                    else{
                        //even if the file exists confirm it's of the exact type
                        String type_file_reported = "";
                          //determine file type. Is it a folder, a link or a regular file
                        if(Files.isSymbolicLink(path)){
                            type_file_reported = "link";
                        }
                        else
                            if(file.isDirectory()){
                                type_file_reported = "folder";
                            }
                            else
                               if(file.isFile()){
                                   //optional, we can exclude hidden files
                                   //if(file.isFile())
                                type_file_reported = "file";
                                file_size_reported = file.length() + ""; 
                            }

                        //file exists
                        file_exists = "yes";

                        //include confirmation information
                        info.put("success", "yes");
                        info.put("type_of_file_reported", type_file_reported);
                        info.put("file_exists", file_exists);
                        info.put("file_size_reported", file_size_reported); //the current size of the file
//System.out.println("current file size:" + file_size_reported);
                        //send conrirmation back to requesting device
                        String command = "Confirmed_File_Results";

                        HashMap <String, Object> hashMap = new HashMap<>();
                        hashMap.put("command", command);
                        hashMap.put("mac_Address", mac_Address); //the mac address of the device(the server of the files)
                        hashMap.put("confirmed_File_Results", info); //put the confirmation information
                      

                        sendCommand = new SendCommand(command, incoming_Ip_Address, Integer.parseInt(incoming_Port), hashMap);
                        sendCommand.Excecute();
                               // System.out.println("sent:" + command2);
                           }
        }
        
        //recieved whenever server has sent confirmation about a file to be downloaded
        public void Confirmed_File_Results(){
            //send the confirmed file results to 'dialog_Download_Files' 
            HashMap<String, Object> confirmed_File_Results = (HashMap)jsonObject.get("confirmed_File_Results");
          try{
            JFrame_confirm_selection.dialog_Download_Files.confirmed_File_Results = confirmed_File_Results;
          }
          catch(Exception e){
              e.printStackTrace();
          }
        }
        
        public void Request_File(){
               //if incoming mac address is not the same as the mac address in communication the we send a reply that the server is still actively engaged
                //this can happen if already a device has connected and another connects after. We only deal with one device at a time
                if(!IsSameDevice()){
                    return;
                }
            
              //reference the map with the information we need. information such as the device to request from and the file paths
            HashMap <String, Object> info = (HashMap)jsonObject.get("info");
        }
        
             //check whether the mac address of the incoming instruction is the same as the one in current communication.
        //if it is not the same then probably a device wants to connect yet the local device is still busy with one already
        public boolean IsSameDevice(){
            //if mac address is empty then assign incoming mac address to 'mac address of connected device'. It means we didn't have one before therefore it's the first connection since creation of the class
            if(StringUtils.isEmpty(StaticValues.mac_address_of_connected_device)){
                StaticValues.mac_address_of_connected_device = incoming_Mac_Address;
                
                return true; //return true;
            }
            
            //if incoming mac address is not the same as that of the 'mac address of connected device' then we return false.
            if(!StringUtils.equalsIgnoreCase(StaticValues.mac_address_of_connected_device, incoming_Mac_Address)){
          
                HashMap <String, Object> hashMap = new HashMap<>();
                hashMap.put("command", command);
                hashMap.put("success", "no");
                hashMap.put("message", "Server is still busy.");
                
                sendCommand = new SendCommand(command, incoming_Ip_Address, Integer.parseInt(incoming_Port), hashMap);
                sendCommand.Excecute();
                
                System.err.println("Same device!");
                return false;
            }
            else{
               
                return true;
            }
        }
        
        //checks whether device is sending data. if it then we can't act on another request at the same time.
        public boolean Is_Sending_Data(){
         
            //if there is an ongoing session then send response and return true
            if(StaticValues.sending_Data){
          
                HashMap <String, Object> hashMap = new HashMap<>();
                hashMap.put("command", command);
                hashMap.put("success", "no");
                hashMap.put("message", "Server is still busy. On going session.");
                
                sendCommand = new SendCommand(command, incoming_Ip_Address, Integer.parseInt(incoming_Port), hashMap);
                sendCommand.Excecute();
                
                System.err.println("On going session");
                return true;
            }
            else{
               
                return false;
            }
        }
        

                //responds with file data when requested. It's after confirmation. If anything doesn't work out then we simply quit. The client expecting the file data is supposed to timeout if no data is sent after some time
        public void Send_File(){
        
                           //if incoming mac address is not the same as the mac address in communication the we send a reply that the server is still actively engaged
                        //this can happen if already a device has connected and another connects after. We only deal with one device at a time
                if(!IsSameDevice()){
                            System.err.println("different device");
                            return;
                 }

                String mac_Address = MyMethods.GetAddress("mac"); //send the mac address of local machine to requesting device. 
           
                String port_For_File_Transmission = (String)jsonObject.get("port_For_File_Transmission"); //the port to use to send file bytes
                String file_Path = (String)jsonObject.get("file_Path");
                //get the number of files and there size as expected by the recipient
                String size_Of_Items_Selected = (String)jsonObject.get("size_Of_Items_Selected");
                String number_Of_Items_Selected = (String)jsonObject.get("number_Of_Items_Selected");
                
                if(StringUtils.isEmpty(size_Of_Items_Selected) || StringUtils.containsIgnoreCase(size_Of_Items_Selected, "null")){
                    size_Of_Items_Selected = "0";
                }
                if(StringUtils.isEmpty(number_Of_Items_Selected) || StringUtils.containsIgnoreCase(number_Of_Items_Selected, "null")){
                    number_Of_Items_Selected = "0";
                }
                
                //update the static variables
                StaticValues.number_Of_Requested_Items = Long.parseLong(number_Of_Items_Selected);
                StaticValues.size_of_Requested_Items = Long.parseLong(size_Of_Items_Selected);
                //show to user
                Dialog_Preparing_files_to_share.Post_Number_Of_Items_Requested_And_Size();
            
            SocketAddress socketAddress = new InetSocketAddress(socket.getInetAddress(), Integer.parseInt(port_For_File_Transmission));
           
            StaticValues.socket2 = new Socket();
            
            boolean proceed = true;
            
                 // Notify everybody that may be interested. Sending data has started
            for (Upload_Listener custom_Listener : upload_Listeners)
                    custom_Listener.sending_Started();
        
            //connect socket2 to socket address
           try {
                StaticValues.socket2.connect(socketAddress, 9000);
            } catch (IOException ex) {
                    //notify whoever is interested that sending has failed and the reason
                 for (Upload_Listener custom_Listener : upload_Listeners)
                    custom_Listener.sending_Stopped(ex.getMessage());
                 
                //StaticValues.sending_Data = false; //change flag to false
                proceed = false; //flag to help us not proceed if we got this exception
                Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(!proceed)
                return;
            
            //check that the file exists
            file_Path = MyMethods.unEscapeJSON(file_Path); //unescape path
            File file = new File(file_Path);       
            Path path = Paths.get(file_Path);
          
                  //determine file type. Is it a folder, a link or a regular file and send accordingly. Care should be taken when sending links
                if(Files.isSymbolicLink(path)){
                   
                }
                else
                    if(file.isDirectory()){
                        
                    }
                    else
                       if(file.isFile()){
                           //optional, we can exclude hidden files
                           //if(file.isFile())
                              // Get the size of the file
                        long length = file.length();
                        byte[] bytes = new byte[512];
                        
                        InputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(file);
                        } catch (FileNotFoundException ex) {
                                   //notify whoever is interested that sending has failed and the reason
                            for (Upload_Listener custom_Listener : upload_Listeners)
                               custom_Listener.sending_Stopped(ex.getMessage());
                            
                               //StaticValues.sending_Data = false; //change flag to false
                               proceed = false; //flag to help us not proceed if we got this exception
                            Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if(!proceed)
                            return;
                        
                        OutputStream outputStream = null;
                        try {
                            outputStream = StaticValues.socket2.getOutputStream();
                        } catch (IOException ex) {
                                       //notify whoever is interested that sending has failed and the reason
                            for (Upload_Listener custom_Listener : upload_Listeners)
                               custom_Listener.sending_Stopped(ex.getMessage());
                            
                              // StaticValues.sending_Data = false; //change flag to false
                                proceed = false; //flag to help us not proceed if we got this exception
                            Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                  
                        }

                        if(!proceed)
                            return;
                        
                        int count;
                        try {
                            while ((count = inputStream.read(bytes)) > 0) {
                                outputStream.write(bytes, 0, count);
                                outputStream.flush();
                                
                               // StaticValues.sending_Data = true;
                                
                                //update number of bytes sent
                                StaticValues.bytes_Sent += count;
                                
                                 //if the size of the items being downloaded is zero then we put percentage to 100. This also helps avoid division by zero if file size is zero
                                if(StaticValues.size_of_Requested_Items == 0){
                                    StaticValues.percentage_Sent = 100;
                                }
                                else
                                    StaticValues.percentage_Sent = (int)( 100 * StaticValues.bytes_Sent / StaticValues.size_of_Requested_Items); //percentage of bytes sent so far 
                            
                                if(StaticValues.percentage_Sent  != StaticValues.counter2){
                                    StaticValues.counter2 = StaticValues.percentage_Sent;
                                     //System.out.println("file % " + MyValues.file_percentage);
                                    //update the progress bar in 'Dialog_Preparing_file_to_share'
                                     //Dialog_Preparing_files_to_share.Post_Progress(StaticValues.percentage_Sent);
                                           //notify whoever is interested in the percentage to update any progress bars
                                    for (Upload_Listener custom_Listener : upload_Listeners)
                                       custom_Listener.Post_Upload_Progress(StaticValues.percentage_Sent);
                                }
                               
                            }
                           // System.out.println("fin!!!!!!!");
                        }
                        catch(SocketException ex){ //usually if the socket recieving data closes
                                  //notify whoever is interested that sending has failed and the reason
                            for (Upload_Listener custom_Listener : upload_Listeners)
                               custom_Listener.sending_Stopped(ex.getMessage());
                            
                            ex.printStackTrace();
                            //StaticValues.sending_Data = false; //change flag to false
                            proceed = false; //flag to help us not proceed if we got this exception
                       
                        }
                        catch (IOException ex) {
                                  //notify whoever is interested that sending has failed and the reason
                            for (Upload_Listener custom_Listener : upload_Listeners)
                               custom_Listener.sending_Stopped(ex.getMessage());
                            
                              // StaticValues.sending_Data = false; //change flag to false
                               proceed = false; //flag to help us not proceed if we got this exception
                            Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                       
                        }

                        if(!proceed)
                            return;
                        
                        //close streams and sockets
                        try{
                            outputStream.close();
                            inputStream.close();
                            if(StaticValues.socket2 != null)
                                 StaticValues.socket2.close();
                            
                        } catch (IOException ex) {
                               StaticValues.sending_Data = false; //change flag to false
                
                    Logger.getLogger(RecieveCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
        }  
        }
        
}
