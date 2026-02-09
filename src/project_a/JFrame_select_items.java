/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import Custom_Listeners.Exit_Listener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;


/**
 *
 * @author vian
 */
public class JFrame_select_items extends javax.swing.JFrame implements Exit_Listener {

    public ArrayList <String> fileNames = new ArrayList<>(); //keep the names of the files
    public ArrayList<String> filePaths = new ArrayList<>(); //keep file paths of the files selected
    public ArrayList<String> iconTypes = new ArrayList<>(); //will keep the names of icons of files. Will be used as a reference when populating the table
    
    //maps to keep files and folders and info (file size) for folders we need to keep the files with. file paths are the keys
    HashMap <String, Object> files_info = new HashMap<>();
    HashMap <String, Object> folders_info = new HashMap<>(); //contain the directories in the user's root selection and all files that belong to that specific folder recursively
    
    //for keeping info about files to be shared. (files outside any folders in root selection)
    ArrayList <String> file_names_list = new ArrayList<>();
    ArrayList <String> file_sizes_list = new ArrayList<>();
    ArrayList <String> file_paths_list = new ArrayList<>();
    ArrayList <String> file_types_list = new ArrayList<>(); //whether directory, link or regular file
    
    //for keeping information about recursed files
     ArrayList <String> file_names_list_recursive = new ArrayList<>();
    ArrayList <String> file_sizes_list_recursive = new ArrayList<>();
    ArrayList <String> file_paths_list_recursive = new ArrayList<>();
    ArrayList <String> file_types_list_recursive = new ArrayList<>(); //whether directory, link or regular file
    
    long content_size_for_folder = 0L; //size of n a specific folder to be shared
    long content_size_for_everything = 0L; //size of everything to be shared.
    long total_number_of_items; //the total items in all folders inclusive
    
      ArrayList<String> strings = new ArrayList<>();
      
      public boolean refreshTable = false; //when true table will be refreshed, true when ever window is closed. 
      
      public static boolean is_Disposed = false; //will be true if disposed
    /**
     * Creates new form SelectFiles
     */
    public JFrame_select_items() {
        
        initComponents();
        
        is_Disposed = false;
        
        setLocationRelativeTo(null); //postion this JFrame in center of screen
  
        init2();
        
          jLabel2.setIcon(StaticValues.drag_and_drop_x_32);
          
         // Create the drag and drop listener
         MyDragDropListener myDragDropListener = new MyDragDropListener();
         
            // Connect the label with a drag and drop listener
         new DropTarget(jPanel2, myDragDropListener);
         
         //add window listener
              this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent we) {
              //add listener
                MainInterface.add_Exit_Listener(JFrame_select_items.this);
            }

            @Override
            public void windowClosing(WindowEvent we) {
               //reset values
               resetValues();
              //set refresh flag to true so that table will be refreshed, loosing all selection if any
                refreshTable = true;
                
                //remove listener
                MainInterface.remove_Exit_Listener(JFrame_select_items.this);
            }

            @Override
            public void windowClosed(WindowEvent we) {
          
            }

            @Override
            public void windowIconified(WindowEvent we) {
            
            }

            @Override
            public void windowDeiconified(WindowEvent we) {
           
            }

            @Override
            public void windowActivated(WindowEvent we) {
                //when window shows re fresh table, remember the values are resetted whenever window is closed so table should be empty
                //check for refresh flag when ever window activates
                if(refreshTable){
                    populateTable();
                    refreshTable = false; //set refresh flag to false
                }
             
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
            
            }
        });
    }

    public void resetValues(){
            fileNames.clear(); //keep the names of the files
            filePaths.clear(); //keep file paths of the files selected
            iconTypes.clear();  //will keep the names of icons of files. Will be used as a reference when populating the table

            //maps to keep files and folders and info (file size) for folders we need to keep the files with. file paths are the keys
            files_info.clear();
            folders_info.clear(); //contain the directories in the user's root selection and all files that belong to that specific folder recursively

            //for keeping info about files to be shared. (files outside any folders in root selection)
            file_names_list.clear();
            file_sizes_list.clear();
            file_paths_list.clear();
            file_types_list.clear(); //whether directory, link or regular file

            //for keeping information about recursed files
            file_names_list_recursive.clear();
            file_sizes_list_recursive.clear(); 
            file_paths_list_recursive.clear(); 
            file_types_list_recursive.clear();  //whether directory, link or regular file

            content_size_for_folder = 0L; //size of n a specific folder to be shared
            content_size_for_everything = 0L; //size of everything to be shared.
            total_number_of_items = 0L; //the total items in all folders inclusive
    }
    
    public void init2(){
        //change background of jTable
          jScrollPane1.getViewport().setBackground(StaticValues.color_4);
          //initialise jTable
          intialiseTable();
          
          if(StaticValues.military_theme){
              //change jTable background
            jScrollPane1.getViewport().setBackground(StaticValues.gray_3);
            
           jLabel1.setBackground(StaticValues.gray_1);
           jLabel1.setBorder(StaticValues.border);
           jButton1.setBackground(Color.black);
           
           jLabel2.setIcon(StaticValues.drag_and_drop_x_32);
           jLabel2.setBackground(StaticValues.gray_2);
           jLabel2.setForeground(Color.black);
           
           jButton2.setBackground(Color.black);
           
        }
    }

    @Override
    public void dispose() {
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
        
        is_Disposed = true;
    }
    
        @Override
    public void exit_Listener() {
       JFrame_select_items.this.dispose();
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        remove_jMenuItem = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        paste_jMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        remove_jMenuItem.setText("Remove");
        remove_jMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remove_jMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(remove_jMenuItem);

        paste_jMenuItem.setText("Paste");
        paste_jMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paste_jMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(paste_jMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/list_x_64.png"))); // NOI18N
        jLabel1.setText("Select items");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setOpaque(true);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jPanel2.setOpaque(false);

        jLabel2.setBackground(StaticValues.color_5);
        jLabel2.setForeground(StaticValues.color_dark);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/drag_and_drop_x_32_material_blue.png"))); // NOI18N
        jLabel2.setText("Drag and drop items");
        jLabel2.setBorder(StaticValues.border);
        jLabel2.setOpaque(true);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });

        jButton1.setBackground(StaticValues.color_dark);
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_blue_light_x_32.png"))); // NOI18N
        jButton1.setText("Browse");
        jButton1.setBorder(StaticValues.border);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setOpaque(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton2.setBackground(StaticValues.color_dark);
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/forward_x_32.png"))); // NOI18N
        jButton2.setBorder(StaticValues.border2);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
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
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
               //show dialog for user to locate image
     
        JFileChooser chooser = new JFileChooser();
        
        chooser.setDialogTitle("Choose items");
        //chooser.setApproveButtonText("Ok");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        int option = chooser.showDialog(JFrame_select_items.this, "Ok");
        if (option == JFileChooser.APPROVE_OPTION) {
            
            File [] files = chooser.getSelectedFiles();
            Path path; 
            String file_path;
            String file_name;
            if(!ArrayUtils.isEmpty(files)){
                //loop through selection
                for(File file : files){
                   //only add a path if it doesn't exist
                    if(!filePaths.contains(file.getAbsolutePath())){  
                        file_path = file.getAbsolutePath();
                        file_name = FilenameUtils.getName(file.getAbsolutePath());
                        
                        //if the file is a link we continue. We don't copy links 
                        path = Paths.get(file_path);
                        
                        if(Files.isSymbolicLink(path)){
                            System.out.println("skipping link:" + file_path);
                               continue;
                         }
                        
                        filePaths.add(file_path);
                        fileNames.add(file_name);
                     
                        if(file.isDirectory())
                            iconTypes.add("folder");
                        else
                            iconTypes.add(file_name);
                        
                        //add row to table
                        addRowToTable(FilenameUtils.getName(file.getAbsolutePath()));
                      
                    }
                    
               
                 }
            }
            else
                System.out.println("empty");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
            //show pop up menu
                //get right click
        if(SwingUtilities.isRightMouseButton(evt)){
//            jPopupMenu1.show(this, evt.getX(), evt.getY());

         JTable source = (JTable)evt.getSource();
                    
        int row = source.rowAtPoint( evt.getPoint() );
        int column = source.columnAtPoint( evt.getPoint() );

        if (! source.isRowSelected(row))
           source.changeSelection(row, column, false, false);

               jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void remove_jMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remove_jMenuItemActionPerformed
            int selectedRows[] = jTable1.getSelectedRows(); //get the selected row indexes

           if(ArrayUtils.isEmpty(selectedRows)){ //if no rows are selected we return
                             return;
           }
           
           DefaultTableModel defaultTableModel =(DefaultTableModel)jTable1.getModel();
            
           //delete the indexes from the arraylists
           /*
           for(int index : selectedRows){
                System.out.println("before index..." + index + " : file name -  " + fileNames.get(index));
                System.out.println("before index..." + index + " : file path - " + fileNames.get(index));
                System.out.println("before index..." + index + " : icon type - " + fileNames.get(index));
               fileNames.remove(index);
               filePaths.remove(index);
               iconTypes.remove(index);
          
//               System.out.println("removing index..." + index + " : " + fileNames.get(index));
           }
           */
           
           //loop backwards from end of the loop to the start
           int index;
           for(int i = (selectedRows.length - 1); i >= 0 ; i--){
              
               index = selectedRows[i];
               
               fileNames.remove(index);
               filePaths.remove(index);
               iconTypes.remove(index);
               
                defaultTableModel.removeRow(index);
           }
           
              //System.out.println("size after remove:" + fileNames.size());
           //re-populate the table can work incase removing rows at a time is disturbing
          // populateTable();
    }//GEN-LAST:event_remove_jMenuItemActionPerformed

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
                 //show pop up menu
                //get right click
        if(SwingUtilities.isRightMouseButton(evt)){
//            jPopupMenu1.show(this, evt.getX(), evt.getY());

  

               jPopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jLabel2MouseReleased

    private void paste_jMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paste_jMenuItemActionPerformed
       
           //create clipboard object
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        Transferable transferable = clipboard.getContents(null);
             
                            // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {

            try {
             
                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    Path path; 
                    String file_path;
                    String file_name;
                    // Get all of the dropped files
                    List files = (List) transferable.getTransferData(flavor);

                    //add files if not present in the arraylists
                    if(!files.isEmpty()){
      
                        File file;
                        for(Object object : files){ 
                            file = (File)object; //cast the object into a file
                           //only add a path if it doesn't exist
                            if(!filePaths.contains(file.getAbsolutePath())){  
                                file_path = file.getAbsolutePath();
                                file_name = FilenameUtils.getName(file.getAbsolutePath());

                                //if the file is a link we continue. We don't copy links 
                                path = Paths.get(file_path);

                                if(Files.isSymbolicLink(path)){
                                    System.out.println("skipping link:" + file_path);
                                       continue;
                                 }

                                filePaths.add(file_path);
                                fileNames.add(file_name);

                                if(file.isDirectory())
                                    iconTypes.add("folder");
                                else
                                    iconTypes.add(file_name);

                                //add row to table
                                addRowToTable(FilenameUtils.getName(file.getAbsolutePath()));
                            }

                         }
                    }
                }
            } catch (Exception e) {

                // Print out the error stack
                e.printStackTrace();
            }      
        }
    }//GEN-LAST:event_paste_jMenuItemActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      //reset necessary values
        total_number_of_items = 0L; //should reset to zero
        content_size_for_everything = 0L; //should reset to zero

//loop through file paths available and populate maps to contain information for files to be finally shared
        File file;
        Path path;
        
        if(!filePaths.isEmpty()){
            /*
            for(String file_path : filePaths){
     
                try{
                    file = new File(file_path);
                    
                     path = Paths.get(file_path);   
         
              //if it's a link, avoid!
                   if(Files.isSymbolicLink(path)){
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
                 
                         //keep folder and file info
                         HashMap<String, Object> hashMap = new HashMap<>();
                         hashMap.put("file_names_list", file_names_list_recursive);
                         hashMap.put("file_sizes_list", file_sizes_list_recursive);
                         hashMap.put("file_paths_list", file_paths_list_recursive);
                         hashMap.put("file_types_list", file_types_list_recursive);
                         hashMap.put("content_size_for_folder", content_size_for_folder + content_size_for_folder);
                         
                         //keep the folder and it's file info
                         folders_info.put(file_path, hashMap);
                         //increment the size of total content. add the folder size
                         content_size_for_everything += content_size_for_folder;
                         
                         System.out.println("folder:" + file.getName() + " size:" + MyMethods.humanReadableByteCount(content_size_for_folder, true));
                    }
                    else{ //regular file
                           String file_type = "file";
                           String file_size = file.length() + ""; 
                           String file_name = file.getName(); //name of file
                           String path_of_file = file.getAbsolutePath(); //path of the file
         
                           file_names_list.add(file_name);
                           file_sizes_list.add(file_size);
                           file_paths_list.add(path_of_file);
                           file_types_list.add(file_type); //whether directory, link or regular file

                           //increment total number of items
                          total_number_of_items ++;
          
                         //increment the size of total content. add the folder size
                         content_size_for_everything += file.length();
                         
                         System.out.println("file:" + file.getName() + " size:" + MyMethods.humanReadableByteCount(content_size_for_folder, true));
                    }
                }
                catch(Exception e){ 
                    e.printStackTrace();
                  
                }
                
            }
            */
            
            //show prepare dialog, pass the file paths list and it will compute as necessary showing info to user
       
            new Dialog_Preparing_files_to_share(JFrame_select_items.this, filePaths).setVisible(true);
        }
        else{
            System.out.println("selection empty:");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    
    public void intialiseTable(){
        //Create a table model to supply to the table
         String col[] = {"", ""};
         
                //disable editing of table cells
          DefaultTableModel tableModel = new DefaultTableModel(){
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                  return false;
                }
              };
          
          //set table headers
          tableModel.setColumnIdentifiers(col);
          
                            //set table to have alternate row colors, or even row backgrounds
                      jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                            @Override
                            public Component getTableCellRendererComponent(JTable table, 
                                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                                /*
                                 Color alternateColor = StaticValues.color_6;
                                 
                                c.setBackground(row%2==0 ? StaticValues.color_5 : alternateColor); //alternate rows
                        
                                
                                if(isSelected){
                                     c.setBackground(StaticValues.color_1);
                                     c.setForeground(Color.white); 
                                }
                                else{
                                    c.setBackground(row%2==0 ? StaticValues.color_5 : alternateColor); //alternate rows
                                    c.setForeground(StaticValues.color_dark_2);
                                }

                                return c;
                                */
                                
                                //default for blue theme
                                 Color alternateColor1 = StaticValues.color_6;
                                 Color alternateColor2 = StaticValues.color_5;
                                 Color foreground_color_when_selected = Color.white;
                                 Color background_color_when_selected = StaticValues.color_1;
                                 Color foreground_color_when_not_selected = StaticValues.color_dark_2;
                                 
                                 if(StaticValues.military_theme){
                                           alternateColor1 = StaticValues.gray_4;
                                           alternateColor2 = StaticValues.gray_5;
                                           foreground_color_when_selected = Color.white;
                                           background_color_when_selected = Color.black;
                                           foreground_color_when_not_selected = Color.BLACK;
                                 }
                                 
                                c.setBackground(row%2==0 ?  alternateColor2 : alternateColor1); //alternate rows
                                
                                if(isSelected){
                                     c.setBackground(background_color_when_selected);
                                     c.setForeground(foreground_color_when_selected); 
                                }
                                else{
                                    c.setBackground(row%2==0 ? alternateColor2 : alternateColor1); //alternate rows
                                    c.setForeground(foreground_color_when_not_selected);
                                }
                            //set font
//                                c.setFont(MyValues.Heading3);
                                  //if has focus then foreground should be blue else black
                               /*  if(hasFocus)
                                      c.setForeground(Color.blue);
                                else
                                      c.setForeground(Color.black);
                                */
                                
                                return c;
                            };
                        });
                       //***end of alternating colors
                 
                       //set column header fonts
                    //  MyValues.clientAll_jTable.getTableHeader().setFont(InternalFont.getFont(InternalFont.Ubuntu_Mono_Bold, Font.PLAIN, 15));
        jTable1.setModel(tableModel);
        jTable1.setRowHeight(30); //we adjust the row height so that icons can fit properly
    
        jTable1.getColumnModel().getColumn(0).setMaxWidth(50); //set column width for first column
       // jTable1.getColumnModel().getColumn(1).setPreferredWidth(1000); 
        
       jTable1.getTableHeader().setReorderingAllowed(false); //disable column dragging   
    }
    
    public void addRowToTable( String fileName){
        Object rowData [] = new Object[3]; //an array of objects which will hold temp data objects with row data
            rowData[0] = "";
            rowData[1] = fileName;
          
           DefaultTableModel defaultTableModel =(DefaultTableModel)jTable1.getModel();
           defaultTableModel.addRow(rowData);
        
            jTable1.getColumnModel().getColumn(0).setCellRenderer(new Icons_JTable(iconTypes));
    }
    
    public void removeRowFromTable(int index){
            DefaultTableModel defaultTableModel =(DefaultTableModel)jTable1.getModel();
               defaultTableModel.removeRow(index);
    }
    
    //deleting rows at a time is a headache, why not just repopulate the table
    public void populateTable(){
          //Create a table model to supply to the table
         String col[] = {"", ""};
         
                //disable editing of table cells
          DefaultTableModel tableModel = new DefaultTableModel(){
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                  return false;
                }
              };
          
          //set table headers
          tableModel.setColumnIdentifiers(col);
          
     Object objectArray [] = new Object[2]; //an array of objects which will hold temp data objects with row data
      try{
        //loop through the networkList keyset and populate object [] arrayList with object arrays
        for (int i = 0; i < fileNames.size(); i++){
            String file_name = fileNames.get(i);
           
            objectArray[0] = "";
            objectArray[1] = file_name;

            tableModel.addRow(objectArray);
            }                                        
      }
      catch(Exception e){
          e.printStackTrace();
      }
          
                            //set table to have alternate row colors, or even row backgrounds
                      jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                            @Override
                            public Component getTableCellRendererComponent(JTable table, 
                                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                                 Color alternateColor = StaticValues.color_6;
                                c.setBackground(row%2==0 ? StaticValues.color_5 : alternateColor); //alternate rows
                        
                                
                                if(isSelected){
                                     c.setBackground(StaticValues.color_1);
                                     c.setForeground(Color.white); 
                                }
                                else{
                                    c.setBackground(row%2==0 ? StaticValues.color_5 : alternateColor); //alternate rows
                                    c.setForeground(StaticValues.color_dark_2);
                                }
                            //set font
//                                c.setFont(MyValues.Heading3);
                                  //if has focus then foreground should be blue else black
                               /*  if(hasFocus)
                                      c.setForeground(Color.blue);
                                else
                                      c.setForeground(Color.black);
                                */
                                
                                return c;
                            };
                        });
                       //***end of alternating colors
                 
                       //set column header fonts
//                       MyValues.clientAll_jTable.getTableHeader().setFont(InternalFont.getFont(InternalFont.Ubuntu_Mono_Bold, Font.PLAIN, 15));
        jTable1.setModel(tableModel);
        jTable1.setRowHeight(30); //we adjust the row height so that icons can fit properly
        
           //set renderers to show icons
        jTable1.getColumnModel().getColumn(0).setCellRenderer(new Icons_JTable(iconTypes));
    
        jTable1.getColumnModel().getColumn(0).setMaxWidth(50); //set column width for first column
       // jTable1.getColumnModel().getColumn(1).setPreferredWidth(1000); 
        
       jTable1.getTableHeader().setReorderingAllowed(false); //disable column dragging
    }
    
      //get the files in a folder recursively. Store infomation including file size in static arraylists
  public void filesInFolderRecursively(String file_path){

      File files = new File(file_path);
      //if the file is not a directory then we return
      if(!files.isDirectory()){
          return;
      }
      
      File [] containingFiles = files.listFiles();
      
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
       
        //increment number of items
        total_number_of_items ++;
         return;
      }
      
            //if a file is a symbolic link we don't calculate it's size
     for(File file : containingFiles){
   
         //if file doesn't exist we skip it and continue with the loop. This can happen if during the while the file is deleted, or user pulls out USB
         if(!file.exists())
             continue;
         
             //attributes we can add
         String file_name = file.getName(); //name of file
         String path_of_file = file.getAbsolutePath(); //path of the file
         String file_type = ""; //whether 'folder', 'link', or regular file
         String file_size = "0"; //applies to regular files only
         
         Path path;
         path = Paths.get(path_of_file);   
         
              //determine file type. Is it a folder, a link or a regular file. if a 'file' then we get it's size and keep it but we increment to get the folder size. 
              //for recursive effect, if a folder is detected then we call the method which gets files and sizes as the loop goes on.
         if(Files.isSymbolicLink(path)){
             file_type = "link";
         }
         else
             if(file.isDirectory()){
                 file_type = "folder";
                 //call the method again for recursive effect
                 filesInFolderRecursively(path_of_file); //supply the file path of the folder
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
                file_names_list.add(file_name);
                file_sizes_list.add(file_size);
                file_paths_list.add(path_of_file);
                file_types_list.add(file_type); //whether directory, link or regular file
                
                //increment total number of items
                total_number_of_items ++;
                
             //   System.out.println("file name:" + file_name + " -> " + file_path );
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
  }
 
    
    //for drag and drop
    class MyDragDropListener implements DropTargetListener {

    @Override
    public void drop(DropTargetDropEvent event) {

        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {

            try {
             
                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    Path path; 
                    String file_path;
                    String file_name;
                    // Get all of the dropped files
                    List files = (List) transferable.getTransferData(flavor);

                    //add files if not present in the arraylists
                    if(!files.isEmpty()){
      
                        File file;
                        for(Object object : files){ 
                            file = (File)object; //cast the object into a file
                           //only add a path if it doesn't exist
                            if(!filePaths.contains(file.getAbsolutePath())){  
                                file_path = file.getAbsolutePath();
                                file_name = FilenameUtils.getName(file.getAbsolutePath());

                                //if the file is a link we continue. We don't copy links 
                                path = Paths.get(file_path);

                                if(Files.isSymbolicLink(path)){
                                    System.out.println("skipping link:" + file_path);
                                       continue;
                                 }

                                filePaths.add(file_path);
                                fileNames.add(file_name);

                                if(file.isDirectory())
                                    iconTypes.add("folder");
                                else
                                    iconTypes.add(file_name);

                                //add row to table
                                addRowToTable(FilenameUtils.getName(file.getAbsolutePath()));
                            }

                         }
                    }
                }
            } catch (Exception e) {

                // Print out the error stack
                e.printStackTrace();
            }
        }

       
        // Inform that the drop is complete
        event.dropComplete(true);

          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               jLabel2.setBackground(StaticValues.color_5);
               
               if(StaticValues.military_theme){
                    jLabel2.setBackground(StaticValues.gray_2);
                    jLabel2.setForeground(Color.black);
                    jLabel2.setIcon(StaticValues.drag_and_drop_x_32);
               }
            }
        });  
    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
        System.out.println("Drag entered");
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                 jLabel2.setBackground(StaticValues.color_6);
                 
                 if(StaticValues.military_theme){
                    jLabel2.setBackground(StaticValues.gray_1);
                    jLabel2.setForeground(Color.white);
                    jLabel2.setIcon(StaticValues.drag_and_drop_white_x_32);
               }
            }
        });
       
    }

    @Override
    public void dragExit(DropTargetEvent event) {
        System.out.println("Drag exited");
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               jLabel2.setBackground(StaticValues.color_5);
               
                if(StaticValues.military_theme){
                    jLabel2.setBackground(StaticValues.gray_2);
                    jLabel2.setForeground(Color.black);
                    jLabel2.setIcon(StaticValues.drag_and_drop_x_32);
               }
            }
        });  
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
         System.out.println("Drag Over");
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
             //   jPanel4.setBackground(defaultDragPanelColor);
                
               jLabel2.setBackground(Color.BLUE);
               
               if(StaticValues.military_theme){
                    jLabel2.setBackground(StaticValues.gray_1);
                    jLabel2.setForeground(Color.white);
                    jLabel2.setIcon(StaticValues.drag_and_drop_white_x_32);
               }
            }
        });
            
    }

    
    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
       
    }

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
            java.util.logging.Logger.getLogger(JFrame_select_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame_select_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame_select_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame_select_items.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame_select_items dialog = new JFrame_select_items();
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JMenuItem paste_jMenuItem;
    private javax.swing.JMenuItem remove_jMenuItem;
    // End of variables declaration//GEN-END:variables
}
