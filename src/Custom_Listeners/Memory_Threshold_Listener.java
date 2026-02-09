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
public interface Memory_Threshold_Listener {

    public void Threshold_Exceeded(); //threshold may be exceed if string is converted to JSON string before writing it over a socket
    
}
