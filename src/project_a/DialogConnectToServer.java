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
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author vian
 */
public class DialogConnectToServer extends javax.swing.JDialog implements Exit_Listener {

    /**
     * Creates new form NewJDialog
     */
   
    public static boolean connecting = false; //will be true if connecting
    public static boolean connected = false; //will be true if server responds
    
    public static String server_Mac_Address; //will be aquired after server responds when initiating a connection
    public static String server_Ip_Address;
    public static String server_Port; 
    
    RecieveCommand recieveCommand;
    
    public static ServerSocket serverSocket;
    Socket socket;
    
    String mac_Address; //for local machine
    String ip_Address; //for local machine
    public static int listening_Port; //for local machine
    
    boolean listen_For_Connection;
    Thread thread; //thread to listen for incoming connection
 
    Boolean thread_Run = true; //false for thread for loops in thread to stop
    
    Timer timer; //timer to check for connectivity
    
    JFrame_confirm_selection frame_confirm_selection;
    
    public DialogConnectToServer(java.awt.Frame parent, boolean modal) {
        
        super(parent, modal);
    
       this.frame_confirm_selection = (JFrame_confirm_selection)parent; //Assuming only 'JFrame_confirm_selection' is calling this dialog, this will be ok
        initComponents();
        
        if(StaticValues.military_theme){
            Set_Military_Theme();
        }
        
        setLocationRelativeTo(null); //postion this  in center
   
        //if we had something typed in already we display it in the textfield
        if(!StringUtils.isEmpty(server_Ip_Address)){
            jTextField1.setText(server_Ip_Address);
        }
  
        //if we had something typed in already we display it in the textfield
        if(!StringUtils.isEmpty(server_Port)){
            jTextField2.setText(server_Port);
        }
        
        //add document listener
//        AddDocumentListener();
        
        //open server socket, to recieve initiation response
        OpenServerSocket();
        
        //start timer to periodically check if local device is on network. The ip address cannot be empty
          CheckConnectivity_TimerTask checkConnectivity_TimerTask = new CheckConnectivity_TimerTask();
          timer = new Timer();
          
           //if ip address is null then user isn't on a network
           ip_Address = MyMethods.GetAddress("ip");
                
           if(StringUtils.isEmpty(ip_Address)){
                 CheckForNetworkConnectivity(); //display icons
                timer.schedule(checkConnectivity_TimerTask, 3000, 2000); //will keep running. Cancel when window is closed
                }
                else{
                    Enter_Credentials();
                    timer.schedule(checkConnectivity_TimerTask, 3000, 2000); //will keep running. Cancel when window is closed
                }
           
           this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                    if(StaticValues.military_theme){

                        //resize dialog coz some borders are more taller
                  DialogConnectToServer.this.setSize(jPanel1.getWidth(), jPanel1.getHeight() + 40);
                }
                    
                    //add listener
                    MainInterface.add_Exit_Listener(DialogConnectToServer.this);
            }

            @Override
            public void windowClosing(WindowEvent e) {

                //remove listener
                MainInterface.remove_Exit_Listener(DialogConnectToServer.this);
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
       
        thread_Run = false; //flag for all loops in thread not to continue
        listen_For_Connection = false;
        connected = false;
        
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
        //close the dialog
        DialogConnectToServer.this.dispose();
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
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/internet_x_55.png"))); // NOI18N
        jLabel1.setText("Checking for network activity...");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setOpaque(true);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jPanel2.setBackground(StaticValues.color_dark);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("iP Address:");

        jTextField1.setBackground(StaticValues.color_6);
        jTextField1.setForeground(StaticValues.color_dark);
        jTextField1.setText(" ");
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Port:");

        jTextField2.setBackground(StaticValues.color_6);
        jTextField2.setForeground(StaticValues.color_dark);
        jTextField2.setText(" ");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
        });

        jButton2.setBackground(StaticValues.color_dark);
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/forward_x_32.png"))); // NOI18N
        jButton2.setBorder(StaticValues.border2);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Network connectivity:");

        jLabel7.setText(" ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(jTextField2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton2))
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
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //check if we have an ip address. If it's empty the local device isn't on a network. We don't proceed
        if(StringUtils.isEmpty(ip_Address)){
            //tell user to connect to a network
            jLabel1.setText("Please connect to a network!");
            return;
        }
            
        //check if all credentials are available, if not change colour of missing fields
        server_Ip_Address = jTextField1.getText();
        server_Port = jTextField2.getText();
        
        //safe trim
        if(!StringUtils.isEmpty(server_Ip_Address))
            server_Ip_Address = server_Ip_Address.trim();
        
        if(!StringUtils.isEmpty(server_Port))
            server_Port = server_Port.trim();
        
        //re-check strings if empty after trimming
        if(StringUtils.isEmpty(server_Ip_Address)){
            jTextField1.setText("Enter ip address.");
            jTextField1.setBackground(StaticValues.red);
            jTextField1.setForeground(Color.white);
            return;
        }
        
        if(StringUtils.isEmpty(server_Port)){
            jTextField2.setText("Enter port number.");
            jTextField2.setBackground(StaticValues.red);
            jTextField2.setForeground(Color.white);
            return;
        }
        
        //if ip address is not valid, tell user
        if(!MyMethods.isValidIP(server_Ip_Address)){
            jTextField1.setText("Improperly typed.");
            jTextField1.setBackground(StaticValues.red);
            jTextField1.setForeground(Color.white);
            return;
        }
        
        //port number can only contain numerical figures
        if(!StringUtils.isNumeric(server_Port)){
            jTextField2.setText("Improperly typed.");
            jTextField2.setBackground(StaticValues.red);
            jTextField2.setForeground(Color.white);
            return;
        }
        
            connecting = true; //when false loading animation won't show
            Connecting();
        
//send command to server to try and initiate a connection
        String command = "Initiate_Connection";
        
        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("command", command);
        hashMap.put("ip_Address", ip_Address);
        hashMap.put("mac_Address", mac_Address);
        hashMap.put("port", listening_Port + "");
        
        
        SendCommand sendCommand = new SendCommand(command, server_Ip_Address, Integer.parseInt(server_Port), hashMap);
        new Thread(){
           @Override
           public void run(){
                sendCommand.Excecute();
           } 
        }.start();
   
    }//GEN-LAST:event_jButton2ActionPerformed

    public void Set_Military_Theme(){
        jLabel1.setBackground(StaticValues.gray_1);
        jLabel1.setBorder(StaticValues.border);
        jButton2.setBackground(Color.black);
        
        jPanel2.setBackground(StaticValues.gray_1);
        
        jTextField1.setForeground(Color.black);
        jTextField2.setForeground(Color.black);
        
              //resize dialog coz some borders are more taller
        //DialogConnectToServer.this.setSize(jPanel1.getWidth(), jPanel1.getHeight() + 30);
    }
    
    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.setBackground(StaticValues.color_6);
        jTextField1.setForeground(StaticValues.color_dark);
        
        if(StaticValues.military_theme){
            jTextField1.setForeground(Color.black);
        }
        
        jTextField1.setText(""); //remove text
        
        //if we had something typed in already we display it in the textfield
        if(!StringUtils.isEmpty(server_Ip_Address)){
            jTextField1.setText(server_Ip_Address);
        }
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        jTextField2.setBackground(StaticValues.color_6);
        jTextField2.setForeground(StaticValues.color_dark);
        
       if(StaticValues.military_theme){
            jTextField2.setForeground(Color.black);
        }
        
        jTextField2.setText(""); //remove text
        
        //if we had something typed in already we display it in the textfield
        if(!StringUtils.isEmpty(server_Port)){
            jTextField2.setText(server_Port);
        }
    }//GEN-LAST:event_jTextField2FocusGained

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
                  while(listen_For_Connection){
           //    System.out.println("Thread running");
                      try {
                          socket = serverSocket.accept();
                         
                             recieveCommand = new RecieveCommand(frame_confirm_selection, DialogConnectToServer.this, socket);
                
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
         
             //make changes to necessary icons when checking for connectivity
    public void CheckForNetworkConnectivity(){
          if(connected){
            DialogConnectToServer.this.dispose();
            System.out.println("connected in enter Check for network");
            return;
                }
             //if connecting then show animation
        if(connecting){
            Connecting();
            return;
        }
        
         jLabel1.setText("Checking for network connectivity...");
         jLabel1.setIcon(StaticValues.internet_x_55);
         jLabel7.setIcon(StaticValues.loading_gif_3_16_x_8);
    }
             //make changes to necessary icons when connectivity is confirmed. Display icon to prompt user to enter credentials
    public void Enter_Credentials(){
                if(connected){
                DialogConnectToServer.this.dispose();
                return;
                }
                
        //if connecting then show animation
        if(connecting){
            Connecting();
            return;
        }
        
         jLabel1.setText("Please enter credentials");
         jLabel1.setIcon(StaticValues.password_x_55);
         jLabel7.setIcon(StaticValues.green_rectangle_16_x_8);
         
         //update mac address
        //update the mac address and port number of local device
        mac_Address = MyMethods.GetAddress("mac");
    }
    
    public void Connecting(){
       //show loading animation
        jLabel1.setIcon(StaticValues.double_ring_x_55);
        jLabel1.setText("Connecting...");    
    }

    public class CheckConnectivity_TimerTask extends TimerTask{

        @Override
        public void run() {
            //stop the timer task if flag is false
           if(!thread_Run){
               this.cancel();
           }
           /*
           if(connected)
               System.out.println("connected");
           else
               System.out.println("not connected");
           */
                 //if ip address is null then user isn't on a network
           ip_Address = MyMethods.GetAddress("ip");
           
           if(!StringUtils.isEmpty(ip_Address) ){ //if ip address is not empty then we have some connnectivity. Cancel the timer task
              Enter_Credentials();
           }
           else{
               ip_Address = "";
               CheckForNetworkConnectivity();
           }
           
        }
    }
    
 /* 
public void AddDocumentListener(){
      DocumentListener documentListener = new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent documentEvent) {
        printIt(documentEvent);
      }
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        printIt(documentEvent);
      }
      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        printIt(documentEvent);
      }
      
      private void printIt(DocumentEvent documentEvent) {
          DocumentEvent.EventType type = documentEvent.getType();
          
            if (type.equals(DocumentEvent.EventType.CHANGE)) {
                if (documentEvent.getDocument()== jTextField1.getDocument()){
                    jTextField1.setBackground(StaticValues.color_6);
                    jTextField1.setForeground(StaticValues.color_dark);
                }

                if (documentEvent.getDocument()== jTextField2.getDocument()){
                    jTextField2.setBackground(StaticValues.color_6);
                    jTextField2.setForeground(StaticValues.color_dark);
                }
            }
            else
                if (type.equals(DocumentEvent.EventType.INSERT)) {
                    if (documentEvent.getDocument()== jTextField1.getDocument()){
                        jTextField1.setBackground(StaticValues.color_6);
                        jTextField1.setForeground(StaticValues.color_dark);
                    }

                    if (documentEvent.getDocument()== jTextField2.getDocument()){
                        jTextField2.setBackground(StaticValues.color_6);
                        jTextField2.setForeground(StaticValues.color_dark);
                    }
                }
            else
                if (type.equals(DocumentEvent.EventType.REMOVE)) {
                    if (documentEvent.getDocument()== jTextField1.getDocument()){
                        jTextField1.setBackground(StaticValues.color_6);
                        jTextField1.setForeground(StaticValues.color_dark);
                    }

                    if (documentEvent.getDocument()== jTextField2.getDocument()){
                        jTextField2.setBackground(StaticValues.color_6);
                        jTextField2.setForeground(StaticValues.color_dark);
                    }
                }
      }
    };
 
          jTextField1.getDocument().addDocumentListener(documentListener);
          jTextField2.getDocument().addDocumentListener(documentListener);
          
     }
*/

//-------------------------
    
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
            java.util.logging.Logger.getLogger(DialogConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogConnectToServer dialog = new DialogConnectToServer(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
