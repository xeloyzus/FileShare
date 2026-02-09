/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import Custom_Listeners.Exit_Listener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Painter;
import javax.swing.UIManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author vian
 */
public class MainInterface extends javax.swing.JFrame {

    public static String version = "1.1";
    
    JFrame_select_items frame_select_items;
    JFrame_confirm_selection frame_confirm_selection;
    
    public static List<Exit_Listener> exit_Listeners = new ArrayList<>(); //listen to exit command for any open windows
    /**
     * Creates new form MainInterface
     */
    public MainInterface() {
        initComponents();
        
        setLocationRelativeTo(null); //postion this JFrame in center of screen
        
        //initialise some interface attributes such as component colours
        initComponents2();
        
        try {
            System.out.println("*** " + MyMethods.Path_Of_Running_Jar(MainInterface.class));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
          //keep os name in a static var
        StaticValues.os_Name =  System.getProperty("os.name");
        
        //load image icons
        load_Image_Icons();
        
        //load files extension in static arraylists
        load_File_Extensions();
        
      
        /*
             JOptionPane.showMessageDialog(MainInterface.this, "Project Blu File Share"
              + "\nVersion 1.0"
              + "\nIcons made by Freepik from www.flaticon.com"
              + "'\nDeveloped by Code Brilliance"
              + "\nwww.code-brilliance.com"
              + "\n");
             */
          
//expiration  
/*
       long current_Date = new Date().getTime();
       long expiration_TimeStamp =  1514764800000L;
        if(current_Date > expiration_TimeStamp){ //Jan 01 2018
                                            //1504912400844
             JOptionPane.showMessageDialog(null, "Please download full version. BETA version expired!");
             System.exit(0);
         }
        */


    }

    //returns the version of the module
    public static String getModuleVersion(){
        return version;
    }
    
    public static BufferedImage getModuleIcon(){
        BufferedImage buf = null ;
        try {
             buf = MyMethods.BufferedImageFromResourcePackage("/images/module_icon.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
            
            return buf;
    }
    
        //for custom listener. 
  public static void add_Exit_Listener(Exit_Listener toAdd) {
        exit_Listeners.add(toAdd);
    }
    
    //for custom listener. Windows should be removed from recieving events
  public static void remove_Exit_Listener(Exit_Listener toRemove) {
        exit_Listeners.remove(toRemove);
    }
    
    //initialise interface components colours and other attributes
    public void initComponents2(){
 
        if(StaticValues.military_theme){
            System.out.println("military theme");
            jPanel2.setOpaque(false); //set opaque so that it can be completely transparent to expose the military theme. 
            jPanel3.setOpaque(false);
            
            jButton1.setBackground(Color.black);
            jButton2.setBackground(Color.black);
        }
    }
    
    public void load_Image_Icons(){
 
    try{    
        StaticValues.folder_blue_x_48 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png");
        StaticValues.folder_gray_x_48 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_48.png");
        StaticValues.document_blank = new ImageIcon("content" + File.separator +  "icons" + File.separator + "document_blank.png");
        StaticValues.harddrive = new ImageIcon("content" + File.separator +  "icons" + File.separator + "harddrive.png");
        StaticValues.cd = new ImageIcon("content" + File.separator +  "icons" + File.separator + "cd_spectrum.png");
        StaticValues.file_txt = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_txt.png");
        StaticValues.file_mp3 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_mp3.png");
        StaticValues.file_xml = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_xml.png");
        StaticValues.file_wma = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_wma.png");
        StaticValues.file_php = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_php.png");
        StaticValues.file_mpg = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_mpg.png");
        StaticValues.file_mov = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_mov.png");
        StaticValues.file_html = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_html.png");
        StaticValues.file_flv = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_flv.png");
        StaticValues.file_divx = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_divx.png");
        StaticValues.file_avi = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_avi.png");
        StaticValues.box_zip = new ImageIcon("content" + File.separator +  "icons" + File.separator + "box_zip.png");
        StaticValues.box_rar = new ImageIcon("content" + File.separator +  "icons" + File.separator + "box_rar.png");
        StaticValues.file_wav = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_wav.png");
        StaticValues.file_vcf = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_vcf.png");
        StaticValues.file_tif = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_tif.png");
        StaticValues.file_raw = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_raw.png");
        StaticValues.file_qxp = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_qxp.png");
        StaticValues.file_qxd = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_qxd.png");
        StaticValues.file_psd = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_psd.png");
        StaticValues.file_png = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_png.png");
        StaticValues.file_jpg = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_jpg.png");
        StaticValues.file_iso = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_iso.png");
        StaticValues.file_inx = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_inx.png");
        StaticValues.file_ical = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_ical.png");
        StaticValues.file_gif = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_gif.png");
        StaticValues.file_flac = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_flac.png");
        StaticValues.file_cue = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_cue.png");
        StaticValues.file_bin = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_bin.png");
        StaticValues.file_aac = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_aac.png");
        StaticValues.file_bmp = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_bmp.png");
        StaticValues.file_xls = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_xls.png");
        StaticValues.file_rtf = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_rtf.png");
        StaticValues.file_ppt = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_ppt.png");
        StaticValues.file_pps = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_pps.png");
        StaticValues.file_svg = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_svg.png");
        StaticValues.file_pdf = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_pdf.png");
        StaticValues.file_eps = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_eps.png");
        StaticValues.file_ai = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_ai.png");
        StaticValues.file_indd = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_indd.png");
        StaticValues.file_doc = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_doc.png");
        //buffered images of the above
        
        StaticValues.folder_blue_x_48_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png"));
        StaticValues.folder_gray_x_48_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_48.png"));
        StaticValues.document_blank_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "document_blank.png"));
        StaticValues.harddrive_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "harddrive.png"));
        StaticValues.cd_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "cd_spectrum.png"));
        StaticValues.file_txt_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_txt.png"));
        StaticValues.file_mp3_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_mp3.png"));
        StaticValues.file_xml_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_xml.png"));
        StaticValues.file_wma_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_wma.png"));
        StaticValues.file_php_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_php.png"));
        StaticValues.file_mpg_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_mpg.png"));
        StaticValues.file_mov_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_mov.png"));
        StaticValues.file_html_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_html.png"));
        StaticValues.file_flv_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_flv.png"));
        StaticValues.file_divx_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_divx.png"));
        StaticValues.file_avi_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_avi.png"));
        StaticValues.box_zip_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "box_zip.png"));
        StaticValues.box_rar_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "box_rar.png"));
        StaticValues.file_wav_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_wav.png"));
        StaticValues.file_vcf_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_vcf.png"));
        StaticValues.file_tif_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_tif.png"));
        StaticValues.file_raw_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_raw.png"));
        StaticValues.file_qxp_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_qxp.png"));
        StaticValues.file_qxd_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_qxd.png"));
        StaticValues.file_psd_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_psd.png"));
        StaticValues.file_png_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_png.png"));
        StaticValues.file_jpg_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_jpg.png"));
        StaticValues.file_iso_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_iso.png"));
        StaticValues.file_inx_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_inx.png"));
        StaticValues.file_ical_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_ical.png"));
        StaticValues.file_gif_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_gif.png"));
        StaticValues.file_flac_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_flac.png"));
        StaticValues.file_cue_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_cue.png"));
        StaticValues.file_bin_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_bin.png"));
        StaticValues.file_aac_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_aac.png"));
        StaticValues.file_bmp_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_bmp.png"));
        StaticValues.file_xls_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_xls.png"));
        StaticValues.file_rtf_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_rtf.png"));
        StaticValues.file_ppt_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_ppt.png"));
        StaticValues.file_pps_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_pps.png"));
        StaticValues.file_svg_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_svg.png"));
        StaticValues.file_pdf_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_pdf.png"));
        StaticValues.file_eps_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_eps.png"));
        StaticValues.file_ai_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_ai.png"));
        StaticValues.file_indd_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_indd.png"));
        StaticValues.file_doc_Buffered_Image =  ImageIO.read(new File("content" + File.separator +  "icons" + File.separator + "file_doc.png"));
        
            StaticValues.cd_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "cd_x_20.png");
            StaticValues.folder_red_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "folder_red_x_20.png");
            StaticValues.folder_blue_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "folder-2_x_20.png");
            StaticValues.folder_gray_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_20.png");
            StaticValues.document_blank_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "document_blank_x_20.png");
            StaticValues.file_txt_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_txt_x_20.png");
            StaticValues.file_mp3_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_mp3_x_20.png");
            StaticValues.file_doc_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_doc_x_20.png");
            StaticValues.file_indd_16 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_indd_16.png");
            StaticValues.file_ai_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_ai_x_20.png");
            StaticValues.file_eps_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_eps_x_20.png");
            StaticValues.file_pdf_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_pdf_x_20.png");
            StaticValues.file_svg_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_svg_x_20.png");
            StaticValues.file_pps_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_pps_x_20.png");
            StaticValues.file_ppt_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_ppt_x_20.png");
            StaticValues.file_rtf_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_rtf_x_20.png");
            StaticValues.file_xls_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_xls_x_20.png");
            StaticValues.file_bmp_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_bmp_x_20.png");
            StaticValues.file_aac_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_aac_x_20.png");
            StaticValues.file_bin_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_bin_x_20.png");
            StaticValues.file_cue_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_cue_x_20.png");
            StaticValues.file_flac_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_flac_x_20.png");
            StaticValues.file_gif_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_gif_x_20.png");
            StaticValues.file_ical_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_ical_x_20.png");
            StaticValues.file_inx_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_inx_x_20.png");
            StaticValues.file_iso_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_iso_x_20.png");
            StaticValues.file_jpg_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_jpg_x_20.png");
            StaticValues.file_png_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_png_x_20.png");
            StaticValues.file_psd_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_psd_x_20.png");
            StaticValues.file_qxd_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_qxd_x_20.png");
            StaticValues.file_qxp_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_qxp_x_20.png");
            StaticValues.file_raw_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_raw_x_20.png");
            StaticValues.file_tif_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_tif_x_20.png");
            StaticValues.file_vcf_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_vcf_x_20.png");
            StaticValues.file_wav_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_wav_x_20.png");
            StaticValues.box_rar_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "box_rar_x_20.png");
            StaticValues.box_zip_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "box_zip_x_20.png");
            StaticValues.file_avi_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_avi_x_20.png");
            StaticValues.file_divx_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_divx_x_20.png");
            StaticValues.file_flv_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_flv_x_20.png");
            StaticValues.file_html_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_html_x_20.png");
            StaticValues.file_mov_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_mov_x_20.png");
            StaticValues.file_mpg_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_mpg_x_20.png");
            StaticValues.file_php_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_php_x_20.png");
            StaticValues.file_wma_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_wma_x_20.png");
            StaticValues.file_xml_x_20 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "file_xml_x_20.png");

            StaticValues.drag_and_drop_x_32 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "drag_and_drop_x_32_material_blue.png"); //default
            if(StaticValues.military_theme){
               StaticValues.drag_and_drop_x_32 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "drag and drop icon2_gray_x_32.png");
            }

            StaticValues.drag_and_drop_white_x_32 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "drag and drop icon2_white_x_32.png"); //default

            StaticValues.green_rectangle_16_x_8 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "green_rectangle_x_16_x_8.png"); 
            StaticValues.red_rectangle_16_x_8 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "red_rectangle_x_16_x_8.png"); 
            StaticValues.orange_rectangle_16_x_8 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "orange_rectangle_x_16_x_8.png"); 
            StaticValues.loading_gif_3_16_x_8 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "loader3_x_16.gif"); 
            StaticValues.internet_x_32 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "internet_x_32.png"); 
            StaticValues.internet_x_64 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "internet_x_64.png"); 
            StaticValues.internet_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "internet_x_55.png"); 
            StaticValues.double_ring_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "double-ring_x_55.gif"); 
            StaticValues.double2_ring_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "Double Ring2_x_55.gif"); 
            StaticValues.double3_ring_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "Double Ring3_x_55.gif"); 
            StaticValues.password_x_64 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "password_x_64.png"); 
            StaticValues.password_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "password_x_55.png"); 
            StaticValues.user_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "user_x_55.png"); 
            StaticValues.list_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "list_x_55.png"); 
            StaticValues.success_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "success_x_55.png"); 
            StaticValues.info_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "info_x_55.png"); 
            StaticValues.warning_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "warning_x_55.png"); 
            StaticValues.error_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "error_x_55.png"); 
            StaticValues.download_x_55 =  new ImageIcon("content" + File.separator +  "icons" + File.separator + "download-2_x_55.png"); 
   
    }   catch (IOException ex) {
           Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        try {
                //JOptionPane.showMessageDialog(null, MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png");
                StaticValues.folder_blue_x_48 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png");
                StaticValues.folder_gray_x_48 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_48.png");
                StaticValues.document_blank = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "document_blank.png");
                StaticValues.harddrive = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "harddrive.png");
                StaticValues.cd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "cd_spectrum.png");
                StaticValues.file_txt = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_txt.png");
                StaticValues.file_mp3 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mp3.png");
                StaticValues.file_xml = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_xml.png");
                StaticValues.file_wma = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_wma.png");
                StaticValues.file_php = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_php.png");
                StaticValues.file_mpg = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mpg.png");
                StaticValues.file_mov = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mov.png");
                StaticValues.file_html = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_html.png");
                StaticValues.file_flv = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_flv.png");
                StaticValues.file_divx = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_divx.png");
                StaticValues.file_avi = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_avi.png");
                StaticValues.box_zip = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "box_zip.png");
                StaticValues.box_rar = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "box_rar.png");
                StaticValues.file_wav = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_wav.png");
                StaticValues.file_vcf = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_vcf.png");
                StaticValues.file_tif = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_tif.png");
                StaticValues.file_raw = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_raw.png");
                StaticValues.file_qxp = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_qxp.png");
                StaticValues.file_qxd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_qxd.png");
                StaticValues.file_psd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_psd.png");
                StaticValues.file_png = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_png.png");
                StaticValues.file_jpg = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_jpg.png");
                StaticValues.file_iso = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_iso.png");
                StaticValues.file_inx = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_inx.png");
                StaticValues.file_ical = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ical.png");
                
               // StaticValues.file_ical = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "document_blank.png");
                StaticValues.file_gif = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_gif.png");
                StaticValues.file_flac = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_flac.png");
                StaticValues.file_cue = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_cue.png");
                StaticValues.file_bin = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_bin.png");
                StaticValues.file_aac = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_aac.png");
                StaticValues.file_bmp = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_bmp.png");
                StaticValues.file_xls = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_xls.png");
                StaticValues.file_rtf = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_rtf.png");
                StaticValues.file_ppt = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ppt.png");
                StaticValues.file_pps = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_pps.png");
                StaticValues.file_svg = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_svg.png");
                StaticValues.file_pdf = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_pdf.png");
                StaticValues.file_eps = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_eps.png");
                StaticValues.file_ai = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ai.png");
                StaticValues.file_indd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_indd.png");
                StaticValues.file_doc = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_doc.png");
            //buffered images of the above
            //try and re-load them if non windows operating system. try to location of jar location
            System.out.println("path:\n" +MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png");
            StaticValues.folder_blue_x_48_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png"));
            StaticValues.folder_gray_x_48_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_48.png"));
            StaticValues.document_blank_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "document_blank.png"));
            StaticValues.harddrive_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "harddrive.png"));
            StaticValues.cd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "cd_spectrum.png"));
            StaticValues.file_txt_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_txt.png"));
            StaticValues.file_mp3_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mp3.png"));
            StaticValues.file_xml_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_xml.png"));
            StaticValues.file_wma_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_wma.png"));
            StaticValues.file_php_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_php.png"));
            StaticValues.file_mpg_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mpg.png"));
            StaticValues.file_mov_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mov.png"));
            StaticValues.file_html_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_html.png"));
            StaticValues.file_flv_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_flv.png"));
            StaticValues.file_divx_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_divx.png"));
            StaticValues.file_avi_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_avi.png"));
            StaticValues.box_zip_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "box_zip.png"));
            StaticValues.box_rar_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "box_rar.png"));
            StaticValues.file_wav_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_wav.png"));
            StaticValues.file_vcf_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_vcf.png"));
            StaticValues.file_tif_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_tif.png"));
            StaticValues.file_raw_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_raw.png"));
            StaticValues.file_qxp_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_qxp.png"));
            StaticValues.file_qxd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_qxd.png"));
            StaticValues.file_psd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_psd.png"));
            StaticValues.file_png_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_png.png"));
            StaticValues.file_jpg_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_jpg.png"));
            StaticValues.file_iso_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_iso.png"));
            StaticValues.file_inx_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_inx.png"));
            StaticValues.file_ical_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ical.png"));
            StaticValues.file_gif_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_gif.png"));
            StaticValues.file_flac_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_flac.png"));
            StaticValues.file_cue_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_cue.png"));
            StaticValues.file_bin_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_bin.png"));
            StaticValues.file_aac_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_aac.png"));
            StaticValues.file_bmp_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_bmp.png"));
            StaticValues.file_xls_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_xls.png"));
            StaticValues.file_rtf_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_rtf.png"));
            StaticValues.file_ppt_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ppt.png"));
            StaticValues.file_pps_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_pps.png"));
            StaticValues.file_svg_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_svg.png"));
            StaticValues.file_pdf_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_pdf.png"));
            StaticValues.file_eps_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_eps.png"));
            StaticValues.file_ai_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ai.png"));
            StaticValues.file_indd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_indd.png"));
            StaticValues.file_doc_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_doc.png"));
       
        
            StaticValues.cd_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "cd_x_20.png");
            StaticValues.folder_red_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder_red_x_20.png");
            StaticValues.folder_blue_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_x_20.png");
            StaticValues.folder_gray_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_20.png");
            StaticValues.document_blank_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "document_blank_x_20.png");
            StaticValues.file_txt_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_txt_x_20.png");
            StaticValues.file_mp3_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mp3_x_20.png");
            StaticValues.file_doc_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_doc_x_20.png");
            StaticValues.file_indd_16 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_indd_16.png");
            StaticValues.file_ai_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ai_x_20.png");
            StaticValues.file_eps_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_eps_x_20.png");
            StaticValues.file_pdf_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_pdf_x_20.png");
            StaticValues.file_svg_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_svg_x_20.png");
            StaticValues.file_pps_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_pps_x_20.png");
            StaticValues.file_ppt_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ppt_x_20.png");
            StaticValues.file_rtf_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_rtf_x_20.png");
            StaticValues.file_xls_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_xls_x_20.png");
            StaticValues.file_bmp_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_bmp_x_20.png");
            StaticValues.file_aac_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_aac_x_20.png");
            StaticValues.file_bin_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_bin_x_20.png");
            StaticValues.file_cue_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_cue_x_20.png");
            StaticValues.file_flac_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_flac_x_20.png");
            StaticValues.file_gif_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_gif_x_20.png");
            StaticValues.file_ical_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_ical_x_20.png");
            StaticValues.file_inx_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_inx_x_20.png");
            StaticValues.file_iso_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_iso_x_20.png");
            StaticValues.file_jpg_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_jpg_x_20.png");
            StaticValues.file_png_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_png_x_20.png");
            StaticValues.file_psd_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_psd_x_20.png");
            StaticValues.file_qxd_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_qxd_x_20.png");
            StaticValues.file_qxp_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_qxp_x_20.png");
            StaticValues.file_raw_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_raw_x_20.png");
            StaticValues.file_tif_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_tif_x_20.png");
            StaticValues.file_vcf_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_vcf_x_20.png");
            StaticValues.file_wav_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_wav_x_20.png");
            StaticValues.box_rar_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "box_rar_x_20.png");
            StaticValues.box_zip_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "box_zip_x_20.png");
            StaticValues.file_avi_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_avi_x_20.png");
            StaticValues.file_divx_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_divx_x_20.png");
            StaticValues.file_flv_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_flv_x_20.png");
            StaticValues.file_html_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_html_x_20.png");
            StaticValues.file_mov_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mov_x_20.png");
            StaticValues.file_mpg_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_mpg_x_20.png");
            StaticValues.file_php_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_php_x_20.png");
            StaticValues.file_wma_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_wma_x_20.png");
            StaticValues.file_xml_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "file_xml_x_20.png");

            StaticValues.drag_and_drop_x_32 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "drag_and_drop_x_32_material_blue.png"); //default
            if(StaticValues.military_theme){
               StaticValues.drag_and_drop_x_32 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "drag and drop icon2_gray_x_32.png");
            }

            StaticValues.drag_and_drop_white_x_32 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "drag and drop icon2_white_x_32.png"); //default

            StaticValues.green_rectangle_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "green_rectangle_x_16_x_8.png"); 
            StaticValues.red_rectangle_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "red_rectangle_x_16_x_8.png"); 
            StaticValues.orange_rectangle_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "orange_rectangle_x_16_x_8.png"); 
            StaticValues.loading_gif_3_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "loader3_x_16.gif"); 
            StaticValues.internet_x_32 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "internet_x_32.png"); 
            StaticValues.internet_x_64 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "internet_x_64.png"); 
            StaticValues.internet_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "internet_x_55.png"); 
            StaticValues.double_ring_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "double-ring_x_55.gif"); 
            StaticValues.double2_ring_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "Double Ring2_x_55.gif"); 
            StaticValues.double3_ring_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "Double Ring3_x_55.gif"); 
            StaticValues.password_x_64 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "password_x_64.png"); 
            StaticValues.password_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "password_x_55.png"); 
            StaticValues.user_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "user_x_55.png"); 
            StaticValues.list_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "list_x_55.png"); 
            StaticValues.success_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "success_x_55.png"); 
            StaticValues.info_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "info_x_55.png"); 
            StaticValues.warning_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "warning_x_55.png"); 
            StaticValues.error_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "error_x_55.png"); 
            StaticValues.download_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "download-2_x_55.png"); 
        } catch (IOException ex1) {
     
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex1);
            
              try {
                //JOptionPane.showMessageDialog(null, MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png");
                StaticValues.folder_blue_x_48 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png");
                StaticValues.folder_gray_x_48 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_48.png");
                StaticValues.document_blank = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "document_blank.png");
                StaticValues.harddrive = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "harddrive.png");
                StaticValues.cd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "cd_spectrum.png");
                StaticValues.file_txt = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_txt.png");
                StaticValues.file_mp3 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mp3.png");
                StaticValues.file_xml = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_xml.png");
                StaticValues.file_wma = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_wma.png");
                StaticValues.file_php = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_php.png");
                StaticValues.file_mpg = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mpg.png");
                StaticValues.file_mov = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mov.png");
                StaticValues.file_html = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_html.png");
                StaticValues.file_flv = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_flv.png");
                StaticValues.file_divx = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_divx.png");
                StaticValues.file_avi = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_avi.png");
                StaticValues.box_zip = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "box_zip.png");
                StaticValues.box_rar = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "box_rar.png");
                StaticValues.file_wav = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_wav.png");
                StaticValues.file_vcf = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_vcf.png");
                StaticValues.file_tif = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_tif.png");
                StaticValues.file_raw = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_raw.png");
                StaticValues.file_qxp = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_qxp.png");
                StaticValues.file_qxd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_qxd.png");
                StaticValues.file_psd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_psd.png");
                StaticValues.file_png = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_png.png");
                StaticValues.file_jpg = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_jpg.png");
                StaticValues.file_iso = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_iso.png");
                StaticValues.file_inx = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_inx.png");
                StaticValues.file_ical = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ical.png");
                
               // StaticValues.file_ical = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "document_blank.png");
                StaticValues.file_gif = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_gif.png");
                StaticValues.file_flac = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_flac.png");
                StaticValues.file_cue = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_cue.png");
                StaticValues.file_bin = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_bin.png");
                StaticValues.file_aac = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_aac.png");
                StaticValues.file_bmp = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_bmp.png");
                StaticValues.file_xls = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_xls.png");
                StaticValues.file_rtf = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_rtf.png");
                StaticValues.file_ppt = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ppt.png");
                StaticValues.file_pps = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_pps.png");
                StaticValues.file_svg = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_svg.png");
                StaticValues.file_pdf = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_pdf.png");
                StaticValues.file_eps = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_eps.png");
                StaticValues.file_ai = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ai.png");
                StaticValues.file_indd = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_indd.png");
                StaticValues.file_doc = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_doc.png");
            //buffered images of the above
            //try and re-load them if in mac os package. try to location of jar location
            System.out.println("path2:\n" +MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png");
            StaticValues.folder_blue_x_48_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder-2_x_48.png"));
            StaticValues.folder_gray_x_48_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_48.png"));
            StaticValues.document_blank_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "document_blank.png"));
            StaticValues.harddrive_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "harddrive.png"));
            StaticValues.cd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "cd_spectrum.png"));
            StaticValues.file_txt_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_txt.png"));
            StaticValues.file_mp3_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mp3.png"));
            StaticValues.file_xml_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_xml.png"));
            StaticValues.file_wma_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_wma.png"));
            StaticValues.file_php_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_php.png"));
            StaticValues.file_mpg_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mpg.png"));
            StaticValues.file_mov_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mov.png"));
            StaticValues.file_html_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_html.png"));
            StaticValues.file_flv_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_flv.png"));
            StaticValues.file_divx_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_divx.png"));
            StaticValues.file_avi_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_avi.png"));
            StaticValues.box_zip_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "box_zip.png"));
            StaticValues.box_rar_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "box_rar.png"));
            StaticValues.file_wav_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_wav.png"));
            StaticValues.file_vcf_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_vcf.png"));
            StaticValues.file_tif_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_tif.png"));
            StaticValues.file_raw_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_raw.png"));
            StaticValues.file_qxp_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_qxp.png"));
            StaticValues.file_qxd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_qxd.png"));
            StaticValues.file_psd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_psd.png"));
            StaticValues.file_png_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_png.png"));
            StaticValues.file_jpg_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_jpg.png"));
            StaticValues.file_iso_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_iso.png"));
            StaticValues.file_inx_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_inx.png"));
            StaticValues.file_ical_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ical.png"));
            StaticValues.file_gif_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_gif.png"));
            StaticValues.file_flac_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_flac.png"));
            StaticValues.file_cue_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_cue.png"));
            StaticValues.file_bin_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_bin.png"));
            StaticValues.file_aac_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_aac.png"));
            StaticValues.file_bmp_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_bmp.png"));
            StaticValues.file_xls_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_xls.png"));
            StaticValues.file_rtf_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_rtf.png"));
            StaticValues.file_ppt_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ppt.png"));
            StaticValues.file_pps_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_pps.png"));
            StaticValues.file_svg_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_svg.png"));
            StaticValues.file_pdf_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_pdf.png"));
            StaticValues.file_eps_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_eps.png"));
            StaticValues.file_ai_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ai.png"));
            StaticValues.file_indd_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_indd.png"));
            StaticValues.file_doc_Buffered_Image =  ImageIO.read(new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_doc.png"));
       
        
            StaticValues.cd_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "cd_x_20.png");
            StaticValues.folder_red_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder_red_x_20.png");
            StaticValues.folder_blue_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder-2_x_20.png");
            StaticValues.folder_gray_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "folder-2_gray_x_20.png");
            StaticValues.document_blank_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "document_blank_x_20.png");
            StaticValues.file_txt_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_txt_x_20.png");
            StaticValues.file_mp3_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mp3_x_20.png");
            StaticValues.file_doc_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_doc_x_20.png");
            StaticValues.file_indd_16 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_indd_16.png");
            StaticValues.file_ai_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ai_x_20.png");
            StaticValues.file_eps_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_eps_x_20.png");
            StaticValues.file_pdf_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_pdf_x_20.png");
            StaticValues.file_svg_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_svg_x_20.png");
            StaticValues.file_pps_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_pps_x_20.png");
            StaticValues.file_ppt_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ppt_x_20.png");
            StaticValues.file_rtf_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_rtf_x_20.png");
            StaticValues.file_xls_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_xls_x_20.png");
            StaticValues.file_bmp_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_bmp_x_20.png");
            StaticValues.file_aac_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_aac_x_20.png");
            StaticValues.file_bin_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_bin_x_20.png");
            StaticValues.file_cue_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_cue_x_20.png");
            StaticValues.file_flac_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_flac_x_20.png");
            StaticValues.file_gif_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_gif_x_20.png");
            StaticValues.file_ical_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_ical_x_20.png");
            StaticValues.file_inx_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_inx_x_20.png");
            StaticValues.file_iso_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_iso_x_20.png");
            StaticValues.file_jpg_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_jpg_x_20.png");
            StaticValues.file_png_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_png_x_20.png");
            StaticValues.file_psd_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_psd_x_20.png");
            StaticValues.file_qxd_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_qxd_x_20.png");
            StaticValues.file_qxp_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_qxp_x_20.png");
            StaticValues.file_raw_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_raw_x_20.png");
            StaticValues.file_tif_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_tif_x_20.png");
            StaticValues.file_vcf_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_vcf_x_20.png");
            StaticValues.file_wav_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_wav_x_20.png");
            StaticValues.box_rar_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "box_rar_x_20.png");
            StaticValues.box_zip_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "box_zip_x_20.png");
            StaticValues.file_avi_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_avi_x_20.png");
            StaticValues.file_divx_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_divx_x_20.png");
            StaticValues.file_flv_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_flv_x_20.png");
            StaticValues.file_html_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_html_x_20.png");
            StaticValues.file_mov_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mov_x_20.png");
            StaticValues.file_mpg_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_mpg_x_20.png");
            StaticValues.file_php_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_php_x_20.png");
            StaticValues.file_wma_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_wma_x_20.png");
            StaticValues.file_xml_x_20 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "file_xml_x_20.png");

            StaticValues.drag_and_drop_x_32 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "drag_and_drop_x_32_material_blue.png"); //default
            if(StaticValues.military_theme){
               StaticValues.drag_and_drop_x_32 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "drag and drop icon2_gray_x_32.png");
            }

            StaticValues.drag_and_drop_white_x_32 = new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "drag and drop icon2_white_x_32.png"); //default

            StaticValues.green_rectangle_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "green_rectangle_x_16_x_8.png"); 
            StaticValues.red_rectangle_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "red_rectangle_x_16_x_8.png"); 
            StaticValues.orange_rectangle_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "orange_rectangle_x_16_x_8.png"); 
            StaticValues.loading_gif_3_16_x_8 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "loader3_x_16.gif"); 
            StaticValues.internet_x_32 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "internet_x_32.png"); 
            StaticValues.internet_x_64 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "internet_x_64.png"); 
            StaticValues.internet_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "internet_x_55.png"); 
            StaticValues.double_ring_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "double-ring_x_55.gif"); 
            StaticValues.double2_ring_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "Double Ring2_x_55.gif"); 
            StaticValues.double3_ring_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "Double Ring3_x_55.gif"); 
            StaticValues.password_x_64 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "password_x_64.png"); 
            StaticValues.password_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "password_x_55.png"); 
            StaticValues.user_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "user_x_55.png"); 
            StaticValues.list_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "list_x_55.png"); 
            StaticValues.success_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "success_x_55.png"); 
            StaticValues.info_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "info_x_55.png"); 
            StaticValues.warning_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "warning_x_55.png"); 
            StaticValues.error_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "error_x_55.png"); 
            StaticValues.download_x_55 =  new ImageIcon(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" +File.separator +  "content" + File.separator +  "icons" + File.separator + "download-2_x_55.png"); 
        } catch (IOException ex2) {
     
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex2);
        }
        }
        }
        
         
        //buffered images with reflections
        StaticValues.folder_blue_x_48_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.folder_gray_x_48_Buffered_Image);
        StaticValues.folder_gray_x_48_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.folder_gray_x_48_Buffered_Image);
        StaticValues.document_blank_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.document_blank_Buffered_Image);
        StaticValues.harddrive_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.harddrive_Buffered_Image);
        StaticValues.cd_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.cd_Buffered_Image);
        StaticValues.file_txt_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_txt_Buffered_Image);
        StaticValues.file_mp3_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_mp3_Buffered_Image);
        StaticValues.file_xml_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_xml_Buffered_Image);
        StaticValues.file_wma_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_wma_Buffered_Image);
        StaticValues.file_php_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_php_Buffered_Image);
        StaticValues.file_mpg_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_mpg_Buffered_Image);
        StaticValues.file_mov_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_mov_Buffered_Image);
        StaticValues.file_html_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_html_Buffered_Image);
        StaticValues.file_flv_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_flv_Buffered_Image);
        StaticValues.file_divx_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_divx_Buffered_Image);
        StaticValues.file_avi_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_avi_Buffered_Image);
        StaticValues.box_zip_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.box_zip_Buffered_Image);
        StaticValues.box_rar_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.box_rar_Buffered_Image);
        StaticValues.file_wav_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_wav_Buffered_Image);
        StaticValues.file_vcf_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_vcf_Buffered_Image);
        StaticValues.file_tif_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_tif_Buffered_Image);
        StaticValues.file_raw_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_raw_Buffered_Image);
        StaticValues.file_qxp_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_qxp_Buffered_Image);
        StaticValues.file_qxd_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_qxd_Buffered_Image);
        StaticValues.file_psd_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_psd_Buffered_Image);
        StaticValues.file_png_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_png_Buffered_Image);
        StaticValues.file_jpg_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_jpg_Buffered_Image);
        StaticValues.file_iso_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_iso_Buffered_Image);
        StaticValues.file_inx_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_inx_Buffered_Image);
        StaticValues.file_ical_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_ical_Buffered_Image);
        StaticValues.file_gif_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_gif_Buffered_Image);
        StaticValues.file_flac_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_flac_Buffered_Image);
        StaticValues.file_cue_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_cue_Buffered_Image);
        StaticValues.file_bin_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_bin_Buffered_Image);
        StaticValues.file_aac_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_aac_Buffered_Image);
        StaticValues.file_bmp_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_bmp_Buffered_Image);
        StaticValues.file_xls_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_xls_Buffered_Image);
        StaticValues.file_rtf_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_rtf_Buffered_Image);
        StaticValues.file_ppt_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_ppt_Buffered_Image);
        StaticValues.file_pps_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_pps_Buffered_Image);
        StaticValues.file_svg_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_svg_Buffered_Image);
        StaticValues.file_pdf_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_pdf_Buffered_Image);
        StaticValues.file_eps_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_eps_Buffered_Image);
        StaticValues.file_ai_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_ai_Buffered_Image);
        StaticValues.file_indd_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_indd_Buffered_Image);
        StaticValues.file_doc_Buffered_Image_with_Reflection = MyMethods.createReflection(StaticValues.file_doc_Buffered_Image);
        
//        StaticValues.monitor_x_16 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "monitor_x_16.png");
//        StaticValues.harddrive_x_16 = new ImageIcon("content" + File.separator +  "icons" + File.separator + "harddrive_x_16.png");

    }
    
    //read audio, image and video file extensions and populate them in static Arraylists
    public void load_File_Extensions(){
   
        URL audio_Extensions_txt_URL = getClass().getResource(File.separator + "file_Extensions" + File.separator + "audio_Extensions.txt");
        URL video_Extensions_txt_URL = getClass().getResource(File.separator + "file_Extensions" + File.separator + "video_Extensions.txt");
        URL image_Extensions_txt_URL = getClass().getResource(File.separator + "file_Extensions" + File.separator + "image_Extensions.txt");
      
       File audio_Extensions_txt = null;
       File video_Extensions_txt = null;
       File image_Extensions_txt = null;
        
       StaticValues.os_Name =  System.getProperty("os.name");

        try{
    
            audio_Extensions_txt = new File(audio_Extensions_txt_URL.toURI());
            video_Extensions_txt = new File(video_Extensions_txt_URL.toURI());
            image_Extensions_txt = new File(image_Extensions_txt_URL.toURI());
        }
        catch(Exception e){
           // e.printStackTrace();
            //windows won't read uri's just as easy
            audio_Extensions_txt = new File("content" + File.separator + "files" + File.separator + "audio_Extensions.txt");
            video_Extensions_txt = new File("content" + File.separator + "files" + File.separator + "video_Extensions.txt");
            image_Extensions_txt = new File("content" + File.separator + "files" + File.separator + "image_Extensions.txt");
            
            if(!audio_Extensions_txt.exists() || !video_Extensions_txt.exists() || !image_Extensions_txt.exists()){
                try{
                     audio_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator + "files" + File.separator + "audio_Extensions.txt");
                     video_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator + "files" + File.separator + "video_Extensions.txt");
                    image_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator + "files" + File.separator + "image_Extensions.txt");
                }
                catch(Exception e2){
                    e2.printStackTrace();
                }
            }
            
            if(!audio_Extensions_txt.exists() || !video_Extensions_txt.exists() || !image_Extensions_txt.exists()){
                try{
                     audio_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" + File.separator + "content" + File.separator + "files" + File.separator + "audio_Extensions.txt");
                     video_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" + File.separator+ "content" + File.separator + "files" + File.separator + "video_Extensions.txt");
                    image_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "File sharing" + File.separator+ "content" + File.separator + "files" + File.separator + "image_Extensions.txt");
                }
                catch(Exception e2){
                    e2.printStackTrace();
                }
            }
            
            //if mac:
            //when packaging the mac executable, howvever close you put resource folders, it just won't recognize them. Solution is to check the location of the jar and then locate the folders
            if(StringUtils.containsIgnoreCase(StaticValues.os_Name, "mac")){
                try {
                    audio_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator + "files" + File.separator + "audio_Extensions.txt");
                    video_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator + "files" + File.separator + "video_Extensions.txt");
                    image_Extensions_txt = new File(MyMethods.Path_Of_Running_Jar(MainInterface.class) + File.separator + "content" + File.separator + "files" + File.separator + "image_Extensions.txt");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
        }
        
        //get file objects from the URLs before trying to read lines
        try{
            /*
            audio_Extensions_txt = new File(audio_Extensions_txt_URL.toURI());
            video_Extensions_txt = new File(video_Extensions_txt_URL.toURI());
            image_Extensions_txt = new File(image_Extensions_txt_URL.toURI());
            */
            
            System.out.println("location:" + audio_Extensions_txt.getAbsolutePath());
            //try and read lines 
            List <String> audio_Extensions_Lines = FileUtils.readLines(audio_Extensions_txt, "UTF-8");
            List <String> video_Extensions_Lines = FileUtils.readLines(video_Extensions_txt, "UTF-8");
            List <String> image_Extensions_Lines = FileUtils.readLines(image_Extensions_txt, "UTF-8");
            
            //populate the static lists. Avoid repeatitions
            
            //audio extensions:
            for(String extension : audio_Extensions_Lines){
                //change extension to lower case
                extension = StringUtils.lowerCase(extension);
                //trim
                extension = StringUtils.trim(extension);
                
                //only add if it doesn't exist
                if(!StaticValues.audio_File_Extensions.contains(extension)) //if it is not contained in the list yet
                    StaticValues.audio_File_Extensions.add(extension);
            }
           // System.out.println("audio extensions:" + StaticValues.audio_File_Extensions.size());
            
            //video extensions:
            for(String extension : video_Extensions_Lines){
                //change extension to lower case
                extension = StringUtils.lowerCase(extension);
                //trim
                extension = StringUtils.trim(extension);
                
                //only add if it doesn't exist
                if(!StaticValues.video_File_Extensions.contains(extension)) //if it is not contained in the list yet
                    StaticValues.video_File_Extensions.add(extension);
            }
            // System.out.println("video extensions:" + StaticValues.video_File_Extensions.size());
             
            //image extensions:
            for(String extension : image_Extensions_Lines){
                //change extension to lower case
                extension = StringUtils.lowerCase(extension);
                //trim
                extension = StringUtils.trim(extension);
                
                //only add if it doesn't exist
                if(!StaticValues.image_File_Extensions.contains(extension)) //if it is not contained in the list yet
                    StaticValues.image_File_Extensions.add(extension);
            }
             //System.out.println("image extensions:" + StaticValues.image_File_Extensions.size());
           
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new BackgroundMenuBar(StaticValues.color_5);
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setTitle("Blue apps File Share");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));
        if(StaticValues.military_theme){
            try {
                Image image = ImageIO.read(getClass().getResource(File.separator + "images" + File.separator + "Woodland-Camo_x_500.jpg"));
                jPanel1 = new ImagePanel(image, true);
            } catch (IOException ex) {
                Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(StaticValues.color_dark);

        jButton1.setBackground(StaticValues.color_dark);
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/upload_x_64.png"))); // NOI18N
        jButton1.setText("Select items to share");
        jButton1.setBorder(StaticValues.border);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);

        jPanel3.setBackground(StaticValues.color_dark);

        jButton2.setBackground(StaticValues.color_dark);
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/download-2_x_64.png"))); // NOI18N
        jButton2.setText("Get items from device");
        jButton2.setBorder(StaticValues.border);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);

        jMenuBar1.setForeground(StaticValues.color_dark);
        if(StaticValues.military_theme){
            jMenuBar1 = new BackgroundMenuBar(Color.DARK_GRAY);
        }

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menu-2_roll_x_16.png"))); // NOI18N
        jMenu2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menu-2_x_16.png"))); // NOI18N
        jMenu2.setRolloverEnabled(true);
        jMenu2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menu-2_x_16.png"))); // NOI18N
        jMenu2.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menu-2_x_16.png"))); // NOI18N
        jMenu2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/menu-2_x_16.png"))); // NOI18N

        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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
       //if not disposed then display and return
      if(frame_confirm_selection != null)
        if(!JFrame_confirm_selection.is_Disposed){
            frame_confirm_selection.setVisible(true);
            frame_confirm_selection.setExtendedState(JFrame.NORMAL);
            frame_confirm_selection.toFront();
            
            return;
        }
        
        if(frame_select_items == null){
            frame_select_items = new JFrame_select_items();
        }
       
        frame_select_items.setVisible(true);
        frame_select_items.setExtendedState(JFrame.NORMAL);
        frame_select_items.toFront();
        
        frame_confirm_selection = null; //make this null
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//        new DialogConnectToServer(MainInterface.this, true).setVisible(true);
        //if 'frame_select_items' is not disposed then we show it instead
      if(frame_select_items != null)
        if(!JFrame_select_items.is_Disposed){
            frame_select_items.setVisible(true);
            frame_select_items.setExtendedState(JFrame.NORMAL);
            frame_select_items.toFront();
            
            return;
        }
  
        
      if(frame_confirm_selection == null){
            frame_confirm_selection = new JFrame_confirm_selection();
        }
       
        frame_confirm_selection.setVisible(true);
        frame_confirm_selection.setExtendedState(JFrame.NORMAL);
        frame_confirm_selection.toFront();
        
        frame_select_items = null;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
      JOptionPane.showMessageDialog(MainInterface.this, "Blue apps File share"
              + "\nVersion " + version + ""
              + "\nIcons made by Freepik from www.flaticon.com"
              + "\nDeveloped by Vianney Ssozi"
              + "\nemail: vianneyssozi@gmail.com"
              + "\n");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
                    
//                                       UIManager.getLookAndFeelDefaults().put("MenuItem.selectionForeground", Color.GREEN);
//        UIManager.getLookAndFeelDefaults().put("MenuItem.selectionBackground", Color.RED);
                    //change some component attributes when using nimbus look and feel
                    

                    if(!StaticValues.military_theme){
                    UIManager.getLookAndFeelDefaults().put("MenuBar:Menu[Selected].backgroundPainter",
                            new FillPainter(StaticValues.color_3));
                    
                    UIManager.getLookAndFeelDefaults().put("MenuBar:Menu[Selected].textForeground",
                            StaticValues.color_dark);
                    
                    
                    
//                    UIManager.getLookAndFeelDefaults().put("MenuItem[MouseOver].textForeground",
//                            new FillPainter(StaticValues.color_5));
                    UIManager.getLookAndFeelDefaults().put("PopupMenu[Enabled].backgroundPainter",
                    new FillPainter(StaticValues.color_3));
   
                     UIManager.getLookAndFeelDefaults().put("Menu[Enabled+Selected].backgroundPainter",
                      new FillPainter(StaticValues.color_dark));
   
//                     UIManager.getLookAndFeelDefaults().put("MenuItem[Enabled].textForeground",
//                     StaticValues.color_dark);
   
//                     UIManager.getLookAndFeelDefaults().put("MenuItem[MouseOver].textForeground",
//                      Color.white);
                     UIManager.getLookAndFeelDefaults().put("MenuItem:MenuItemAccelerator[MouseOver].textForeground",
                      Color.white);
   
                     UIManager.getLookAndFeelDefaults().put("MenuItem[MouseOver].backgroundPainter",
                      new FillPainter(StaticValues.color_dark));
                    }
                    break;
                }
            }
            
                
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

      
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainInterface().setVisible(true);
            }
        });
    }

    //quit
    public void Exit(){
        //send broadcast to close any open windows
        for(Exit_Listener exit_Listener : MainInterface.exit_Listeners){
            System.out.println("**** starting exit loop");
            exit_Listener.exit_Listener();
        }
        
      MainInterface.this.setVisible(false);
      MainInterface.this.dispose();
      System.exit(0);
    }
    
   static  class FillPainter implements Painter<JComponent> {

    private final Color color;

    FillPainter(Color c) {
        color = c;
    }

    @Override
    public void paint(Graphics2D g, JComponent object, int width, int height) {
        g.setColor(color);
        g.fillRect(0, 0, width - 1, height - 1);
    }
}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
