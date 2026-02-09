/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import java.util.HashMap;

/**
 *
 * @author vian
 */
public class SendCommand {
    int port;
    String ip_Address_To_Send_To;
    HashMap<String, Object> hashMap;
    String command;
    
    public SendCommand(String command, String ip_Address_To_Send_To, int port, HashMap <String, Object> hashMap){
        this.ip_Address_To_Send_To = ip_Address_To_Send_To;
        this.port = port;
        this.hashMap = hashMap;
        this.command = command;
        
    }
    
    public void Excecute(){
        switch(command){
            case "Request_File":
                MyMethods.SendResponse(ip_Address_To_Send_To, port, hashMap);
                break;
                
                //try and initiate a connection with a server
            case "Initiate_Connection":
                 MyMethods.SendResponse(ip_Address_To_Send_To, port, hashMap);
                
                break;
                
                //send response to device trying to connect
            case "Connection_Initiation_Response":
                 MyMethods.SendResponse(ip_Address_To_Send_To, port, hashMap);
                break;
                
                //ask server to confirm about file before we download it
            case "Confirm_File":
                 MyMethods.SendResponse(ip_Address_To_Send_To, port, hashMap);
                break;
                
                //server responds with confirmed results about a file to be  downloaded
            case "Confirmed_File_Results":
                 MyMethods.SendResponse(ip_Address_To_Send_To, port, hashMap);
                break;
                
                //ask server to send a file
            case "Send_File":
                 MyMethods.SendResponse(ip_Address_To_Send_To, port, hashMap);
                break;
        }
    }
}
