/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import Custom_Listeners.Exit_Listener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author vian
 */
public class JFrame_confirm_selection extends javax.swing.JFrame implements Exit_Listener {

    /**
     * Creates new form JFrame_confirm_selection
     */
    DialogConnectToServer dialogConnectToServer;
    public static Dialog_Download_Files dialog_Download_Files;
    
    HashMap <String, Object> info; //info from server containing all files and folders information
    
    ArrayList <String> root_Selection_File_Paths;
    ArrayList <String> root_Selection_File_Names;
    ArrayList <String> root_Selection_Icon_Types;
    ArrayList <String> root_Selection_File_Sizes;
                //everything info, useful when displaying contents in folders all at once i.e. expose all folder and sub folder contents in a single view
    ArrayList <String> all_File_Paths;
    ArrayList <String> all_File_Names;
    ArrayList <String> all_Icon_Types;
    ArrayList <String> all_File_Sizes;
    //if user choses any media then we keep the file sizes in the lists below
    ArrayList <String> all_File_Paths_Media = new ArrayList<>();
    ArrayList <String> all_File_Names_Media = new ArrayList<>();
    ArrayList <String> all_Icon_Types_Media = new ArrayList<>();
    ArrayList <String> all_File_Sizes_Media = new ArrayList<>();
    
    HashMap <String, Object> folders_Info; 
    long content_Size_For_Everything; //as received from server
    long total_Number_Of_Items; //as recieved from server
    
    boolean show_Root_Selection = true; //flag will be true if we intend to show only root selection
    
    public boolean refreshTable = false; //when true table will be refreshed, true when ever window is closed. 
    
    int row_Clicked; //current row clicked in table
    String selections_And_Size_Count; //will have the number of selections and there size
    Long number_Of_Items_Selected;
    Long size_Of_Items_Selected;
    
    String save_Directory; //path of where user will be saving the files
    
    String os_Name_For_Server; //operating system of the server. (the device where the files are to be requested) this will be the operating system
    
    public static boolean is_Disposed = false; //will be true if disposed
    
    public JFrame_confirm_selection() {
        initComponents();
        
           Init2();
           
        jPanel5.setVisible(false);
        jPanel5.setEnabled(false);
        
        if(StaticValues.military_theme){
            Set_Military_Theme();
        }
        
        is_Disposed = false;
        
        setLocationRelativeTo(null); //postion this JFrame in center of screen
        
        //underline some labels
        MyMethods.JLabel_Underline(jLabel2);
        MyMethods.JLabel_Underline(jLabel5);
        MyMethods.JLabel_Underline(jLabel7);
        
     

           //add window listener
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent we) {
                  //show dialog to input credentials. This dialog will pass the 'JFrame_confirm_selection' object which will be manipulated when a response is recieved.
                 
               dialogConnectToServer = new  DialogConnectToServer(JFrame_confirm_selection.this, true);
               dialogConnectToServer.setVisible(true);
         
               //add listener
               MainInterface.add_Exit_Listener(JFrame_confirm_selection.this);
            }

            @Override
            public void windowClosing(WindowEvent we) {
                        //reset values

               Reset_Values();
              //set refresh flag to true so that table will be refreshed, loosing all selection if any
                refreshTable = true;
             
                //remove listener
                MainInterface.remove_Exit_Listener(JFrame_confirm_selection.this);
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
                   if(info != null && !info.isEmpty())//only refresh if we have information
                        Populate_Table();
                    
                                 
                    //show dialog to input credentials. This dialog will pass the 'JFrame_confirm_selection' object which will be manipulated when a response is recieved.
                    dialogConnectToServer = new  DialogConnectToServer(JFrame_confirm_selection.this, true);
                    try{
                     dialogConnectToServer.setVisible(true);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    
                    refreshTable = false; //set refresh flag to false
                }
             
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
            
            }
        });
    }

        public void Set_Military_Theme(){
        jLabel1.setBackground(StaticValues.gray_1);
        jLabel1.setBorder(StaticValues.border);
      
                 //change jTable background
        jScrollPane1.getViewport().setBackground(StaticValues.gray_3);
            
        jPanel2.setOpaque(false);
        
        jPanel3.setBackground(StaticValues.gray_1);
      // jPanel3.setBorder(StaticValues.border);
//        
       jPanel4.setBackground(StaticValues.gray_1);
//        
        jPanel5.setBackground(StaticValues.gray_1);

//        
        jPanel6.setBackground(StaticValues.gray_1);
        
        jButton2.setBackground(Color.black);
        
        //resize dialog coz some borders are more taller
       // Dialog_Preparing_files_to_share.this.setSize(jPanel1.getWidth(), jPanel1.getHeight() + 80);
    }
    
    
    public JFrame_confirm_selection(HashMap<String, Object> info){
        this(); //call default constructor
        this.info = info;
        
        this.root_Selection_File_Paths = (ArrayList)info.get("root_Selection_File_Paths");
        this.root_Selection_File_Names = (ArrayList) info.get("root_Selection_File_Names");
        this.root_Selection_Icon_Types = (ArrayList)info.get("root_Selection_Icon_Types");
        this.root_Selection_File_Sizes = (ArrayList)info.get("root_Selection_File_Sizes");
                //everything info, useful when displaying contents in folders all at once i.e. expose all folder and sub folder contents in a single view
        this.all_File_Paths = (ArrayList)info.get("all_File_Paths");
        this.all_File_Names = (ArrayList)info.get("all_File_Names");
        this.all_Icon_Types = (ArrayList)info.get("all_Icon_Types");
        this.all_File_Sizes = (ArrayList)info.get("all_File_Sizes");
        
        folders_Info = (HashMap)info.get("folders_Info");
        content_Size_For_Everything = Long.parseLong((String)info.get("content_Size_For_Everything"));
        total_Number_Of_Items = Long.parseLong((String)info.get("total_Number_Of_Items"));
      
        //populate table
        Populate_Table();
        

              
    }
    
        @Override
    public void exit_Listener() {
        System.out.println("**** called exit listener");
       JFrame_confirm_selection.this.dispose();
    }
    
           @Override
    public void dispose() {
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
        
        is_Disposed = true;
    }
    
    //called to re-load lists(lists and other variables are re-assigned using the 'info' map)
    public void Assign_Values(){
        
        root_Selection_File_Paths = (ArrayList)info.get("root_Selection_File_Paths");
        root_Selection_File_Names = (ArrayList) info.get("root_Selection_File_Names");
        root_Selection_Icon_Types = (ArrayList)info.get("root_Selection_Icon_Types");
        root_Selection_File_Sizes = (ArrayList)info.get("root_Selection_File_Sizes");
                //everything info, useful when displaying contents in folders all at once i.e. expose all folder and sub folder contents in a single view
        all_File_Paths = (ArrayList)info.get("all_File_Paths");
        all_File_Names = (ArrayList)info.get("all_File_Names");
        all_Icon_Types = (ArrayList)info.get("all_Icon_Types");
        all_File_Sizes = (ArrayList)info.get("all_File_Sizes");
        
        folders_Info = (HashMap)info.get("folders_Info");
        content_Size_For_Everything = Long.parseLong((String)info.get("content_Size_For_Everything"));
        total_Number_Of_Items = Long.parseLong((String)info.get("total_Number_Of_Items"));
        
        selections_And_Size_Count = " ";
                                //update the vars for number of items and size
        number_Of_Items_Selected = 0L;
        size_Of_Items_Selected = 0L;
                                
                                //update the lable to show the number of items and the size of the content
        jLabel4.setText(selections_And_Size_Count);
        
      
        /*
         for(String key : folders_Info.keySet()){
            System.out.println("key:" + key);
            HashMap <String, Object> folder_Information = (HashMap)folders_Info.get(key);
              //get the arraylists and other information about this folder contained in the map
              ArrayList <String> file_names_list = (ArrayList)folder_Information.get("file_names_list");
              System.out.println("size in loop:" + file_names_list.size());
        }
     */
        
        os_Name_For_Server = (String)info.get("os_Name"); //operating system of the server that has the files

             show_Root_Selection = true; //change flag to true to show root selection 
            //uncheck some check boxes
            jCheckBox1.setSelected(false);
            jCheckBox2.setSelected(false);
            jCheckBox3.setSelected(false);
            jCheckBox4.setSelected(false);
            jCheckBox5.setSelected(false);

            //check the desired button in the button group
            buttonGroup1.clearSelection();
            jRadioButton1.setSelected(true);
        
    }
    
    //reset values so that even table is blank the next time the window opens
    public void Reset_Values(){
       if(info != null){
        root_Selection_File_Paths.clear();
        root_Selection_File_Names.clear();
        root_Selection_Icon_Types.clear();
        root_Selection_File_Sizes.clear();
                //everything info, useful when displaying contents in folders all at once i.e. expose all folder and sub folder contents in a single view
        all_File_Paths.clear();
        all_File_Names.clear();
        all_Icon_Types.clear();
        all_File_Sizes.clear();
        
        folders_Info.clear();
        }
       
        
        content_Size_For_Everything = 0L;
        total_Number_Of_Items = 0L;
        
        //show root selection
        show_Root_Selection = true;
        
        //clear selected check fields
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        
        jCheckBox3.setSelected(false);
        jCheckBox4.setSelected(false);
        jCheckBox5.setSelected(false);
        
        //reset the lable that shows the total items selected and there size
        selections_And_Size_Count = " ";
        jLabel4.setText(selections_And_Size_Count);
        
        //reset the size and number of items selected
        number_Of_Items_Selected = 0L;
        size_Of_Items_Selected = 0L;

        buttonGroup1.clearSelection();
        jRadioButton1.setSelected(true);
      
        save_Directory = "";
        
        os_Name_For_Server = "";
    }
    
    public void Init2(){
        //change background of jTable
          jScrollPane1.getViewport().setBackground(StaticValues.color_4);
             //initialise jTable
          intialiseTable();
    }
    
        public void intialiseTable(){
        //Create a table model to supply to the table
         String col[] = {"", "",  ""};
         
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
//                       MyValues.clientAll_jTable.getTableHeader().setFont(InternalFont.getFont(InternalFont.Ubuntu_Mono_Bold, Font.PLAIN, 15));
        jTable1.setModel(tableModel);
        jTable1.setRowHeight(30); //we adjust the row height so that icons can fit properly
    
        jTable1.getColumnModel().getColumn(0).setMaxWidth(50); //set column width for first column
        jTable1.getColumnModel().getColumn(2).setMaxWidth(50); //set column width for 3rd column
       // jTable1.getColumnModel().getColumn(1).setPreferredWidth(1000); 
        
       jTable1.getTableHeader().setReorderingAllowed(false); //disable column dragging   
    }
    
    public void Populate_Table( ){
          //Create a table model to supply to the table
         String col[] = {"", "", ""};
         
                //disable editing of table cells
          DefaultTableModel tableModel = new DefaultTableModel(){
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                  return false;
                }
              };
          
          //set table headers
          tableModel.setColumnIdentifiers(col);
          
         ArrayList <String> file_Paths ;
         ArrayList <String> file_Names;
         ArrayList <String> icon_Types;
         ArrayList <String> file_Sizes;
          
         ArrayList <String> icons_To_Show = new ArrayList<>(); //populate this list as the table populates. use file name if file is not a directory
          
         //clear list that will keep tabs when media options are selected
         all_File_Paths_Media.clear();
         all_File_Names_Media.clear();
         all_Icon_Types_Media.clear();
         all_File_Sizes_Media.clear();

         
          //check flag and consider which information to use
         if(show_Root_Selection){
             file_Paths = root_Selection_File_Paths;
             file_Names = root_Selection_File_Names;
             icon_Types = root_Selection_Icon_Types;
             file_Sizes = root_Selection_File_Sizes;
         }
         else{
             file_Paths = all_File_Paths;
             file_Names = all_File_Names;
             icon_Types = all_Icon_Types;
             file_Sizes = all_File_Sizes;
         }
         
        if(file_Paths == null)
         return;
     Object objectArray [] = new Object[3]; //an array of objects which will hold temp data objects with row data
     
      try{
        //loop through  and populate object [] arrayList with object arrays
        for (int i = 0; i < file_Paths.size(); i++){
            boolean extension_Found = false; //will be true if a file matches any of the expected media extensions. if true then the other checks can be ignored
            
            String file_Path = file_Paths.get(i);
            String file_Name = file_Names.get(i);
            String icon_Type = icon_Types.get(i);
            String file_Size = file_Sizes.get(i);
            //if 'file_Size' as a string is empty then default it zero so as to avoid an exception when parsing a Long
            if(StringUtils.isEmpty(file_Size)){
                file_Size = "0";
            }
            
            //if the icon type is not folder then we keep the file name. This is the list that will provided when drawing icons in the first column
            if(StringUtils.equalsIgnoreCase(icon_Type, "folder")){
                icons_To_Show.add("folder");
            }
            else{ //keep file name
                icons_To_Show.add(file_Name);
            }
            
            //System.out.println("name:"  + file_Name + " icon_Type:" + icon_Type + " file_Size:" + file_Size);
            String file_Extenstion;
            //incase user has some media files of interest we check if the file is among the expected extensions.
           if(jCheckBox3.isSelected() || jCheckBox4.isSelected() || jCheckBox5.isSelected()){
               //if the file is a folder then we simply go to the start of th loop 'continue'
                if(StringUtils.equalsIgnoreCase(icon_Type, "folder"))
                    continue;
                
                 //check if user selected audio files
                if(jCheckBox3.isSelected()){
             
                       file_Extenstion = FilenameUtils.getExtension(file_Name); //extension of the file
                       
                       //if the file has no extension we continue
                       if(StringUtils.isEmpty(file_Extenstion))
                           continue;
                       
                       //change extension to lower case
                       file_Extenstion = StringUtils.lowerCase(file_Extenstion);
                       
                       //check if extension is among the extension in the list
                       if(StaticValues.audio_File_Extensions.contains(file_Extenstion)){
                           extension_Found = true; //set to true so that other checks don't excute. Remember this flag is resetted to false when the loop starts
                           
                            all_File_Paths_Media.add(file_Path);
                            all_File_Names_Media.add(file_Name);
                            all_Icon_Types_Media.add(file_Name);
                            all_File_Sizes_Media.add(file_Size);
                            
                            objectArray[0] = "";
                            objectArray[1] = file_Name;
                            objectArray[2] = MyMethods.humanReadableByteCount(Long.parseLong(file_Size), true);

                            tableModel.addRow(objectArray);
                       }
                }
                
                if(extension_Found) //if extension is found already by a previous check then go to start of loop. no need for further execution
                    continue;
                
                 //check if user selected video files
                if(jCheckBox4.isSelected()){
             
                       file_Extenstion = FilenameUtils.getExtension(file_Name); //extension of the file
                       
                       //if the file has no extension we continue
                       if(StringUtils.isEmpty(file_Extenstion))
                           continue;
                       
                       //change extension to lower case
                       file_Extenstion = StringUtils.lowerCase(file_Extenstion);
                       
                       //check if extension is among the extension in the list
                       if(StaticValues.video_File_Extensions.contains(file_Extenstion)){
                           
                            all_File_Paths_Media.add(file_Path);
                            all_File_Names_Media.add(file_Name);
                            all_Icon_Types_Media.add(file_Name);
                            all_File_Sizes_Media.add(file_Size);
                             
                            objectArray[0] = "";
                            objectArray[1] = file_Name;
                            objectArray[2] = MyMethods.humanReadableByteCount(Long.parseLong(file_Size), true);

                            tableModel.addRow(objectArray);
                       }
                }
                
                        
                if(extension_Found) //if extension is found already by a previous check then go to start of loop. no need for further execution
                    continue;
                
                 //check if user selected image files
                if(jCheckBox5.isSelected()){
             
                       file_Extenstion = FilenameUtils.getExtension(file_Name); //extension of the file
                       
                       //if the file has no extension we continue
                       if(StringUtils.isEmpty(file_Extenstion))
                           continue;
                       
                       //change extension to lower case
                       file_Extenstion = StringUtils.lowerCase(file_Extenstion);
                       
                       //check if extension is among the extension in the list
                       if(StaticValues.image_File_Extensions.contains(file_Extenstion)){
                           
                            all_File_Paths_Media.add(file_Path);
                            all_File_Names_Media.add(file_Name);
                            all_Icon_Types_Media.add(file_Name);
                            all_File_Sizes_Media.add(file_Size);
                             
                            objectArray[0] = "";
                            objectArray[1] = file_Name;
                            objectArray[2] = MyMethods.humanReadableByteCount(Long.parseLong(file_Size), true);

                            tableModel.addRow(objectArray);
                       }
                }
                
           }
           else{
                    objectArray[0] = "";
                    objectArray[1] = file_Name;
                    objectArray[2] = MyMethods.humanReadableByteCount(Long.parseLong(file_Size), true);

                    tableModel.addRow(objectArray);
                }
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
                                
                                if(column == 2){
                                   this.setHorizontalAlignment(SwingConstants.RIGHT);
                                }
                              else{
                                      this.setHorizontalAlignment(SwingConstants.LEFT);
                                }

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
                                
                                return c;
                            };
                        });
                       //***end of alternating colors
                 
                       //set column header fonts
//                       MyValues.clientAll_jTable.getTableHeader().setFont(InternalFont.getFont(InternalFont.Ubuntu_Mono_Bold, Font.PLAIN, 15));
        jTable1.setModel(tableModel);
        jTable1.setRowHeight(30); //we adjust the row height so that icons can fit properly
      //  System.out.println("size:" + icons_To_Show.size());
           //set renderers to show icons
         if(jCheckBox3.isSelected() || jCheckBox4.isSelected() || jCheckBox5.isSelected()){ //if user has selected any media options use a different list to show the right icons
            jTable1.getColumnModel().getColumn(0).setCellRenderer(new Icons_JTable(all_Icon_Types_Media)); 
         }
         else{
            jTable1.getColumnModel().getColumn(0).setCellRenderer(new Icons_JTable(icons_To_Show));
         }
                  
        jTable1.getColumnModel().getColumn(0).setMaxWidth(50); //set column width for first column
        jTable1.getColumnModel().getColumn(2).setMaxWidth(100); //set column width for 3rd column
        
                //align patient id and form number columns data to right
           
//        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)MyValues.table_all_profiles.getDefaultRenderer(Object.class);
//        renderer.setHorizontalAlignment(JLabel.LEFT);
//        MyValues.table_all_profiles.getColumn("User name").setCellRenderer(renderer);
//        allPatientsIdsAndForm.getColumn("Form number").setCellRenderer( rightRenderer );
        
       jTable1.getTableHeader().setReorderingAllowed(false); //disable column dragging
    }
          
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

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

        jPanel2.setBackground(StaticValues.color_dark);

        jPanel3.setBackground(StaticValues.color_dark);
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel4.setBackground(StaticValues.color_dark);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Options:");

        jCheckBox2.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setText("List folder content");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Select all");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addContainerGap())
        );

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/password_x_55.png"))); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jPanel5.setBackground(StaticValues.color_dark);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Allow Only:");

        jCheckBox3.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setText("Audio files");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox4.setText("Video files");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox5.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox5.setText("Image files");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox4)
                            .addComponent(jCheckBox3))
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(StaticValues.color_dark);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("If file exists:");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Replace");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setText("Keep both");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton3.setText("Skip");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton1))
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jLabel6)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable1MouseDragged(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText(" ");

        jButton2.setBackground(StaticValues.color_dark);
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/forward_x_32.png"))); // NOI18N
        jButton2.setBorder(StaticValues.border2);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(4, 4, 4))
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
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
    
    }//GEN-LAST:event_jTable1MouseReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRows[] = jTable1.getSelectedRows(); //get the selected row indexes

        if(ArrayUtils.isEmpty(selectedRows)){ //if no rows are selected we return
            JOptionPane.showMessageDialog(JFrame_confirm_selection.this, "Please make a selection", "Selection empty.", JOptionPane.INFORMATION_MESSAGE);
             return;
           }
        
        //ask user to select destination where to save
        JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jFileChooser.setDialogTitle("Choose where to save items: ");
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //user should only select directories

        int returnValue = jFileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jFileChooser.getSelectedFile().isDirectory()) {
	System.out.println("You selected the directory: " + jFileChooser.getSelectedFile());
                save_Directory = jFileChooser.getSelectedFile().getAbsolutePath();
                //check if the the destination has enough space. If it doesn't tell user
                Long free_Space = jFileChooser.getSelectedFile().getUsableSpace(); //get free space
                if(free_Space < size_Of_Items_Selected){ //free space not enough
                    Long size_To_Delete = size_Of_Items_Selected - free_Space;
                    //tell user how much more space is needed
                    JOptionPane.showMessageDialog(JFrame_confirm_selection.this, "Please free up more space or select another destination."
                            + "\nFree up atleast " + MyMethods.humanReadableByteCount(size_To_Delete, true), "Space not enough.", JOptionPane.INFORMATION_MESSAGE);
                    
                    return;
                }
                
                }
        }
        else{
            System.err.println("user has not chosen where to save");
            JOptionPane.showMessageDialog(JFrame_confirm_selection.this, "You must choose where to save in order to proceed.", "Choose where to save.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList <String> file_Paths ;
         ArrayList <String> file_Names;
         ArrayList <String> icon_Types;
         ArrayList <String> file_Sizes;
          
         ArrayList <String> icons_To_Show = new ArrayList<>(); //populate this list as the table populates. use file name if file is not a directory
         
         //arrayLists that will contain the selection info to be used for requests. These lists are sent to the download window
         ArrayList <String> file_Paths_2 = new ArrayList<>();
         ArrayList <String> file_Names_2 = new ArrayList<>();
         ArrayList <String> file_Sizes_2 = new ArrayList<>();
          
       if(jCheckBox3.isSelected() || jCheckBox4.isSelected() || jCheckBox5.isSelected()){ //if user has selected any media options use a different list to show the right icons
                                    file_Paths = all_File_Paths_Media;
                                    file_Names = all_File_Names_Media;
                                    icon_Types = all_Icon_Types_Media;
                                    file_Sizes = all_File_Sizes_Media;
                               }
    else
          //check flag and consider which information to use
        if(show_Root_Selection){
             file_Paths = root_Selection_File_Paths;
             file_Names = root_Selection_File_Names;
             icon_Types = root_Selection_Icon_Types;
             file_Sizes = root_Selection_File_Sizes;
         }
         else{
             file_Paths = all_File_Paths;
             file_Names = all_File_Names;
             icon_Types = all_Icon_Types;
             file_Sizes = all_File_Sizes;
         }
         
        HashMap <String, Object> folders_In_Root_Selection = new HashMap<>(); //keep the paths of the folders in root selection, path is the key , name is the value
        
        int index;
        
        for (int i = 0; i < selectedRows.length; i++){
            index = selectedRows[i];
            
            String file_Name = file_Names.get(index);
            String file_Path = file_Paths.get(index);
            String icon_Type = icon_Types.get(index);
            String file_Size = file_Sizes.get(index);
            //if 'file_Size' as a string is empty then default it zero so as to avoid an exception when parsing a Long
            if(StringUtils.isEmpty(file_Size)){
                file_Size = "0";
            }
            
            //if the icon type is not folder then we keep the file name. This is the list that will provided when drawing icons in the first column
            if(StringUtils.equalsIgnoreCase(icon_Type, "folder")){
           
                //keep the folder path and file name in map;
                folders_In_Root_Selection.put(file_Path, file_Name);
               
                ArrayList <String> file_names_list = null;
                ArrayList <String> file_sizes_list = null;
                ArrayList <String> file_paths_list = null ;
                ArrayList <String > file_types_list = null;
                long  content_size_for_folder ;  //no need coz we already have it in 'file_Size' variable
                
                //if it is a folder we check if it has content, if it does not then it an empty folder
                //isolate this folders info from 'folders_Info' map. The file path is the key
                HashMap <String, Object> folder_Information = (HashMap) folders_Info.get(file_Path);
               if(folder_Information != null ){ //can happen if 'show_Root_Selection' is false
                //get the arraylists and other information about this folder contained in the map
                   file_names_list = (ArrayList)folder_Information.get("file_names_list");
                   file_sizes_list = (ArrayList)folder_Information.get("file_sizes_list");
                   file_paths_list = (ArrayList)folder_Information.get("file_paths_list");
                    file_types_list = (ArrayList)folder_Information.get("file_types_list");
                   content_size_for_folder = (long)folder_Information.get("content_size_for_folder");  //no need coz we already have it in 'file_Size' variable
                }
           
        
                String file_Name_2;
                String file_Path_2;
                String file_Size_2;
                String icon_Type_2;
                        
                //if folder is empty then we simply set the icon to a folder icon
                if(file_names_list == null || file_names_list.isEmpty()){
                         icons_To_Show.add("folder");
                      
                        //add content folder to general lists including all files
                       // icons_To_Show.add(icon_Type_2);
                   //populate selection info into the lists
                        file_Names_2.add(file_Name);
                        file_Paths_2.add(file_Path);
                        file_Sizes_2.add(file_Size);
                        
                        System.out.println("folder empty:" + file_Path);
                }
                else{ //loop through and add the paths, names and sizes of the files contained in the lists that will be finally sent
                    for(int j = 0; j < file_names_list.size(); j++){
                        
                        
                        file_Name_2 = file_names_list.get(j);
                        file_Path_2 = file_paths_list.get(j);
                        file_Size_2 = file_sizes_list.get(j);
                        icon_Type_2 = file_types_list.get(j);
                        
                        //add content folder to general lists including all files
                        icons_To_Show.add(icon_Type_2);
                        file_Names_2.add(file_Name_2);
                        file_Paths_2.add(file_Path_2);
                        file_Sizes_2.add(file_Size_2);
                        
                      // System.out.println("file ("+ j + "):" + file_Path_2);
                    }
                    
                }
            
            }
            else{ //keep file name
                icons_To_Show.add(file_Name);
                
                  //populate selection info into the lists
                file_Names_2.add(file_Name);
                file_Paths_2.add(file_Path);
                file_Sizes_2.add(file_Size);
            }
            
            //incase you want to regard item count with folders in the root selection then uncomment this and comment the previous occurence
            //populate selection info into the lists
            /*
            file_Names_2.add(file_Name);
            file_Paths_2.add(file_Path);
            file_Sizes_2.add(file_Size);
            */
        }

        
        //what the user has chosen to do incase files with the same name exist.
        String what_To_Do_If_File_Exists = "";
        
        if(jRadioButton1.isSelected()){
            what_To_Do_If_File_Exists = "replace";
        }
        else
            if(jRadioButton2.isSelected()){
                what_To_Do_If_File_Exists = "keep_Both";
            }
        else
                if(jRadioButton3.isSelected()){
                    what_To_Do_If_File_Exists = "skip";
                }
        
        //compose map with the data to be used by the download window
        HashMap <String, Object> info_2 = new HashMap<>();
        info_2.put("file_Names", file_Names_2);
        info_2.put("file_Paths", file_Paths_2);
        info_2.put("file_Sizes", file_Sizes_2);
        info_2.put("icons_To_Show", icons_To_Show); //will help determing what icon to display and also whether file is folder
        info_2.put("folders_Info", folders_Info); //any folders in root selection and all the content plus more info such as size although size may already be available in  'file_Sizes_2'
      
        info_2.put("what_To_Do_If_File_Exists", what_To_Do_If_File_Exists);
        
        info_2.put("selections_And_Size_Count", selections_And_Size_Count + "");
        info_2.put("number_Of_Items_Selected", number_Of_Items_Selected + "");
        info_2.put("size_Of_Items_Selected", size_Of_Items_Selected + "");
        
        info_2.put("save_Directory", save_Directory);

        info_2.put("os_Name_For_Server", os_Name_For_Server); //operating system of the server
        //include the folders in root selection;
        info_2.put("folders_In_Root_Selection", folders_In_Root_Selection);
        
        //total number of items, all files even in root selection folders
        int total_items = file_Names.size();
        //if there are no items then we don't proceed
        if(total_items == 0){
            JOptionPane.showMessageDialog(JFrame_confirm_selection.this, "There are no items to get.", "No items", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
 
        //show new dialog window and pass in the info map
        dialog_Download_Files = new  Dialog_Download_Files(JFrame_confirm_selection.this, info_2);
        
              new Thread(){
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                 dialog_Download_Files.Download_Files();
            }
        }.start();

       dialog_Download_Files.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseDragged
               //unselect the 'select all' check box coz it will remain selected yet only a few things seem selected
            jCheckBox1.setSelected(false);
            
             //display the number of items selected in the table and the size
            Display_Selected_Item_Count_And_Size();
    }//GEN-LAST:event_jTable1MouseDragged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
               //if we have no row in the table we quit
            if (jTable1.getRowCount() < 1)
                           return;

            row_Clicked  = jTable1.rowAtPoint(evt.getPoint());//better than getSelectedRow()
            
            //unselect the 'select all' check box coz it will remain selected yet only a few things seem selected
            jCheckBox1.setSelected(false);
            
            //display the number of items selected in the table and the size
            Display_Selected_Item_Count_And_Size();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed

        //reset some values
        //reset the lable that shows the total items selected and there size
        selections_And_Size_Count = " ";
        jLabel4.setText(selections_And_Size_Count);

        //reset the size and number of items selected
        number_Of_Items_Selected = 0L;
        size_Of_Items_Selected = 0L;

        Populate_Table();
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed

        //reset some values
        //reset the lable that shows the total items selected and there size
        selections_And_Size_Count = " ";
        jLabel4.setText(selections_And_Size_Count);

        //reset the size and number of items selected
        number_Of_Items_Selected = 0L;
        size_Of_Items_Selected = 0L;

        Populate_Table();
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed

        //reset some values
        //reset the lable that shows the total items selected and there size
        selections_And_Size_Count = " ";
        jLabel4.setText(selections_And_Size_Count);

        //reset the size and number of items selected
        number_Of_Items_Selected = 0L;
        size_Of_Items_Selected = 0L;

        //uncheck select all
        Populate_Table();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        //show dialog to input credentials. This dialog will pass the 'JFrame_confirm_selection' object which will be manipulated when a response is recieved.
        dialogConnectToServer = new  DialogConnectToServer(JFrame_confirm_selection.this, true);
        try{
            dialogConnectToServer.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(jCheckBox1.isSelected()){
            //if we have no row in the table we don't proceed.
            if (jTable1.getRowCount() < 1)
            return;

            jTable1.getSelectionModel().addSelectionInterval(0, (jTable1.getRowCount()- 1)); //select all rows in the table programmatically
        }
        else{
            //deselect all tables programmatically
            jTable1.getSelectionModel().clearSelection();
        }

        //display the number of items selected in the table and the size
        Display_Selected_Item_Count_And_Size();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed

        //clear table selection
        //deselect all tables programmatically
        jTable1.getSelectionModel().clearSelection();
        jCheckBox1.setSelected(false);

        //reset some values
        //reset the lable that shows the total items selected and there size
        selections_And_Size_Count = " ";
        jLabel4.setText(selections_And_Size_Count);

        //reset the size and number of items selected
        number_Of_Items_Selected = 0L;
        size_Of_Items_Selected = 0L;

        //change flag to determine which info to display. if true then we show root selection else we show all content
        if(jCheckBox2.isSelected()){
            show_Root_Selection = false; //show folder and sub folder content in root selection
        }
        else{
            show_Root_Selection = true;
        }

        Populate_Table(); //show info for flag changes to take effect
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    //display the number of items selected in the table and the size
    public void Display_Selected_Item_Count_And_Size(){
        /*
            //if user has selected all then simply use the arraylist sizes
            if(jCheckBox1.isSelected()){
                              
                ArrayList <String> file_Paths ;
                ArrayList <String> file_Names;
                ArrayList <String> icon_Types;
                ArrayList <String> file_Sizes;
                                                
                                                         //determine which arraylist to use 
                if(show_Root_Selection){
                  file_Paths = root_Selection_File_Paths;
                  file_Names = root_Selection_File_Names;
                  icon_Types = root_Selection_Icon_Types;
                  file_Sizes = root_Selection_File_Sizes;
                 }
                 else{
                         file_Paths = all_File_Paths;
                         file_Names = all_File_Names;
                         icon_Types = all_Icon_Types;
                         file_Sizes = all_File_Sizes;
                         }
            
                    selected_Items_And_Size_Count = file_Names.size() + " items selected.";
                          
                      selected_Items_And_Size_Count += " Size: " + MyMethods.humanReadableByteCount(content_Size_For_Everything,  true);  
                      //update the vars for number of items and size
                      number_Of_Items_Selected = (long)file_Names.size();
                      size_Of_Items_Selected = new Long(content_Size_For_Everything);
            }
        else
                */
             if (jTable1.getRowCount() > 0){ //if we have some rows present in the table
               int selectedRows[] = jTable1.getSelectedRows(); //get the selected row indexes
               if(!ArrayUtils.isEmpty(selectedRows)){
               
                       java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            if(selectedRows.length == 1){
                                selections_And_Size_Count = "1 Selection.";
                                
                                //if user deselects one item at a time and there happens to be one more remaining then we have to get the index of the selected item
                                //and treat it like the current selected
                                row_Clicked  = selectedRows[0];
                                
                                ArrayList <String> file_Paths ;
                                ArrayList <String> file_Names;
                                ArrayList <String> icon_Types;
                                ArrayList <String> file_Sizes;
                                
                                    //determine which arraylist to use 
                                
                               if(jCheckBox3.isSelected() || jCheckBox4.isSelected() || jCheckBox5.isSelected()){ //if user has selected any media options use a different list to show the right icons
                                    file_Paths = all_File_Paths_Media;
                                    file_Names = all_File_Names_Media;
                                    icon_Types = all_Icon_Types_Media;
                                    file_Sizes = all_File_Sizes_Media;
                               }
                               else
                                if(show_Root_Selection){
                                    file_Paths = root_Selection_File_Paths;
                                    file_Names = root_Selection_File_Names;
                                    icon_Types = root_Selection_Icon_Types;
                                    file_Sizes = root_Selection_File_Sizes;
                                }
                                else{
                                    file_Paths = all_File_Paths;
                                    file_Names = all_File_Names;
                                    icon_Types = all_Icon_Types;
                                    file_Sizes = all_File_Sizes;
                                }
           
                                //we are interested in the file size
                                String file_Size = file_Sizes.get(row_Clicked);
                                //if 'file_Size' as a string is empty then default it zero so as to avoid an exception when parsing a Long
                                if(StringUtils.isEmpty(file_Size)){
                                    file_Size = "0";
                                }
                                
                                selections_And_Size_Count += " Size:" + MyMethods.humanReadableByteCount(Long.parseLong(file_Size), true);
                                //update the vars for number of items and size
                                number_Of_Items_Selected = 1L;
                                size_Of_Items_Selected = Long.parseLong(file_Size);
                            }
                                  else{ 
                                              selections_And_Size_Count = selectedRows.length + " Selections.";
                                           
                                                ArrayList <String> file_Paths ;
                                                ArrayList <String> file_Names;
                                                ArrayList <String> icon_Types;
                                                ArrayList <String> file_Sizes;
                                                
                                                         //determine which arraylist to use 
                                            if(jCheckBox3.isSelected() || jCheckBox4.isSelected() || jCheckBox5.isSelected()){ //if user has selected any media options use a different list to show the right icons
                                                        file_Paths = all_File_Paths_Media;
                                                        file_Names = all_File_Names_Media;
                                                        icon_Types = all_Icon_Types_Media;
                                                        file_Sizes = all_File_Sizes_Media;
                                                   }
                                            else
                                                if(show_Root_Selection){
                                                    file_Paths = root_Selection_File_Paths;
                                                    file_Names = root_Selection_File_Names;
                                                    icon_Types = root_Selection_Icon_Types;
                                                    file_Sizes = root_Selection_File_Sizes;
                                                }
                                                else{
                                                    file_Paths = all_File_Paths;
                                                    file_Names = all_File_Names;
                                                    icon_Types = all_Icon_Types;
                                                    file_Sizes = all_File_Sizes;
                                                }
                                                
                                                int index;
                                                long total_Size = 0L;
        
                                                for (int i = 0; i < selectedRows.length; i++){
                                                    index = selectedRows[i];

                                                    String file_Name = file_Names.get(index);
                                                    String file_Path = file_Paths.get(index);
                                                    String icon_Type = icon_Types.get(index);
                                                    String file_Size = file_Sizes.get(index);
                                                    //if 'file_Size' as a string is empty then default it zero so as to avoid an exception when parsing a Long
                                                    if(StringUtils.isEmpty(file_Size)){
                                                        file_Size = "0";
                                                    }

                                                    //we are interested in only totalling the size of the selected content
                                                  total_Size += Long.parseLong(file_Size); 
                                                }
                                             
                                               selections_And_Size_Count += " Size: " + MyMethods.humanReadableByteCount(total_Size,  true);  
                                                  //update the vars for number of items and size
                                                number_Of_Items_Selected = (long)selectedRows.length;
                                                size_Of_Items_Selected = total_Size;
                                          }
                            
                            //update the lable to show the number of items and the size of the content
                            jLabel4.setText(selections_And_Size_Count);
                        }
                      });
                   }
               else{ //      //if we have no rows selected then we reset some variables and display nothing in the string
                                selections_And_Size_Count = " ";
                                //update the vars for number of items and size
                                number_Of_Items_Selected = 0L;
                                size_Of_Items_Selected = 0L;
                                
                                //update the lable to show the number of items and the size of the content
                                jLabel4.setText(selections_And_Size_Count);
               }
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
            java.util.logging.Logger.getLogger(JFrame_confirm_selection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame_confirm_selection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame_confirm_selection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame_confirm_selection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrame_confirm_selection().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables


}
