/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Custom_Listeners;

/**
 *
 * @author vian
 */
public interface Upload_Listener {

    public void sending_Started();

    //if sending is stopped because of an error/exception
    public void sending_Stopped(String error_Message);

    public void Post_Upload_Progress(int percentage_Sent);
    
}
