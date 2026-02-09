package project_a;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vian
 */
public class StaticValues {
    //static lists that will contain the media file extensions
    public static ArrayList <String>  audio_File_Extensions = new ArrayList<>();
    public static ArrayList <String> video_File_Extensions = new ArrayList<>();
    public static ArrayList <String> image_File_Extensions = new ArrayList<>();
    
    //for uploading---
    public static long number_Of_Requested_Items;//will have the number of items requested if requested for files.  Remember to reset to zero when needed because it is static
    public static long size_of_Requested_Items; //will have the size of the items requested. Remember to reset value to zero for every new download session since it's static
    public static long bytes_Sent;//total number of bytes sent since session started
    public static int percentage_Sent; //percentage of data sent
    public static int counter2;
    public static boolean sending_Data; //will be true if the device is actively uploading
    public static  Socket socket2; //to be used for sending data
    //----
    public static String os_Name; //operating system of the local machine
    public static String mac_address_of_connected_device; //the mac address of the device currently in connection
    
   // public static Color color_dark = new Color(0, 100, 255);
    public static Color color_dark = new Color(13, 71, 161); //material
    public static Color color_dark_1 = new Color(0, 130, 255);
   // public static Color color_dark_2 = new Color(0, 100, 160); //for text in tables
    public static Color color_dark_2 = new Color(13, 71, 161); //for text in tables 
    public static Color color_1 = new Color(0, 153, 204);
    public static Color color_2 = new Color(36, 173, 222);
    public static Color color_3 = new Color(138, 213, 240);
    public static Color color_4 = new Color(168, 223, 244);
   // public static Color color_5 = new Color(197, 234, 248);
    public static Color color_5 = new Color(187, 222, 251); //material
    public static Color color_6 = new Color(226, 244, 251);
    
    //greys
    public static Color gray_1 = new Color(64, 64, 64);
    public static Color gray_2 = new Color(194, 194, 194);
    public static Color gray_3 = new Color(150, 150, 150);
    public static Color gray_4 = new Color(180, 180, 180);
    public static Color gray_5 = new Color(200, 200, 200);
    
    //red color for errors
    public static Color red = new Color(153, 0, 0);
    
    //borders
   public static LineBorder lineBorder1 =  new LineBorder(StaticValues.color_dark, 1);
   public static EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);
   
   public static Border border =  BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(StaticValues.color_3, 2),
               BorderFactory.createEmptyBorder(10, 5, 5, 10)); //creates some padding as desired
   
   public static Border border2 =  BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(StaticValues.color_3, 2),
               BorderFactory.createEmptyBorder(5, 10, 5, 10)); //creates some padding as desired
   
  public static boolean military_theme = true;
  
    public static ImageIcon folder_blue_x_48;
    public static ImageIcon folder_gray_x_48;
    public static ImageIcon document_blank;
    public static ImageIcon harddrive;
    public static ImageIcon cd;
    public static ImageIcon file_txt;
    public static ImageIcon file_mp3;
    public static ImageIcon file_xml;
    public static ImageIcon file_wma;
    public static ImageIcon file_php;
    public static ImageIcon file_mpg;
    public static ImageIcon file_mov;
    public static ImageIcon file_html;
    public static ImageIcon file_flv;
    public static ImageIcon file_divx;
    public static ImageIcon file_avi;
    public static ImageIcon box_zip;
    public static ImageIcon box_rar;
    public static ImageIcon file_wav;
    public static ImageIcon file_vcf;
    public static ImageIcon file_tif;
    public static ImageIcon file_raw;
    public static ImageIcon file_qxp;
    public static ImageIcon file_qxd;
    public static ImageIcon file_psd;
    public static ImageIcon file_png;
    public static ImageIcon file_jpg;
    public static ImageIcon file_iso;
    public static ImageIcon file_inx;
    public static ImageIcon file_ical;
    public static ImageIcon file_gif;
    public static ImageIcon file_flac;
    public static ImageIcon file_cue;
    public static ImageIcon file_bin;
    public static ImageIcon file_aac;
    public static ImageIcon file_bmp;
    public static ImageIcon file_xls;
    public static ImageIcon file_rtf;
    public static ImageIcon file_ppt;
    public static ImageIcon file_pps;
    public static ImageIcon file_svg;
    public static ImageIcon file_pdf;
    public static ImageIcon file_eps;
    public static ImageIcon file_ai;
    public static ImageIcon file_indd;
    public static ImageIcon file_doc;
    //buffered Images
    public static BufferedImage folder_blue_x_48_Buffered_Image;
    public static BufferedImage folder_gray_x_48_Buffered_Image;
    public static BufferedImage document_blank_Buffered_Image;
    public static BufferedImage harddrive_Buffered_Image;
    public static BufferedImage cd_Buffered_Image;
    public static BufferedImage file_txt_Buffered_Image;
    public static BufferedImage file_mp3_Buffered_Image;
    public static BufferedImage file_xml_Buffered_Image;
    public static BufferedImage file_wma_Buffered_Image;
    public static BufferedImage file_php_Buffered_Image;
    public static BufferedImage file_mpg_Buffered_Image;
    public static BufferedImage file_mov_Buffered_Image;
    public static BufferedImage file_html_Buffered_Image;
    public static BufferedImage file_flv_Buffered_Image;
    public static BufferedImage file_divx_Buffered_Image;
    public static BufferedImage file_avi_Buffered_Image;
    public static BufferedImage box_zip_Buffered_Image;
    public static BufferedImage box_rar_Buffered_Image;
    public static BufferedImage file_wav_Buffered_Image;
    public static BufferedImage file_vcf_Buffered_Image;
    public static BufferedImage file_tif_Buffered_Image;
    public static BufferedImage file_raw_Buffered_Image;
    public static BufferedImage file_qxp_Buffered_Image;
    public static BufferedImage file_qxd_Buffered_Image;
    public static BufferedImage file_psd_Buffered_Image;
    public static BufferedImage file_png_Buffered_Image;
    public static BufferedImage file_jpg_Buffered_Image;
    public static BufferedImage file_iso_Buffered_Image;
    public static BufferedImage file_inx_Buffered_Image;
    public static BufferedImage file_ical_Buffered_Image;
    public static BufferedImage file_gif_Buffered_Image;
    public static BufferedImage file_flac_Buffered_Image;
    public static BufferedImage file_cue_Buffered_Image;
    public static BufferedImage file_bin_Buffered_Image;
    public static BufferedImage file_aac_Buffered_Image;
    public static BufferedImage file_bmp_Buffered_Image;
    public static BufferedImage file_xls_Buffered_Image;
    public static BufferedImage file_rtf_Buffered_Image;
    public static BufferedImage file_ppt_Buffered_Image;
    public static BufferedImage file_pps_Buffered_Image;
    public static BufferedImage file_svg_Buffered_Image;
    public static BufferedImage file_pdf_Buffered_Image;
    public static BufferedImage file_eps_Buffered_Image;
    public static BufferedImage file_ai_Buffered_Image;
    public static BufferedImage file_indd_Buffered_Image;
    public static BufferedImage file_doc_Buffered_Image;
    //buffered images with reflections
    public static BufferedImage folder_blue_x_48_Buffered_Image_with_Reflection;
    public static BufferedImage folder_gray_x_48_Buffered_Image_with_Reflection;
    public static BufferedImage document_blank_Buffered_Image_with_Reflection;
    public static BufferedImage harddrive_Buffered_Image_with_Reflection;
    public static BufferedImage cd_Buffered_Image_with_Reflection;
    public static BufferedImage file_txt_Buffered_Image_with_Reflection;
    public static BufferedImage file_mp3_Buffered_Image_with_Reflection;
    public static BufferedImage file_xml_Buffered_Image_with_Reflection;
    public static BufferedImage file_wma_Buffered_Image_with_Reflection;
    public static BufferedImage file_php_Buffered_Image_with_Reflection;
    public static BufferedImage file_mpg_Buffered_Image_with_Reflection;
    public static BufferedImage file_mov_Buffered_Image_with_Reflection;
    public static BufferedImage file_html_Buffered_Image_with_Reflection;
    public static BufferedImage file_flv_Buffered_Image_with_Reflection;
    public static BufferedImage file_divx_Buffered_Image_with_Reflection;
    public static BufferedImage file_avi_Buffered_Image_with_Reflection;
    public static BufferedImage box_zip_Buffered_Image_with_Reflection;
    public static BufferedImage box_rar_Buffered_Image_with_Reflection;
    public static BufferedImage file_wav_Buffered_Image_with_Reflection;
    public static BufferedImage file_vcf_Buffered_Image_with_Reflection;
    public static BufferedImage file_tif_Buffered_Image_with_Reflection;
    public static BufferedImage file_raw_Buffered_Image_with_Reflection;
    public static BufferedImage file_qxp_Buffered_Image_with_Reflection;
    public static BufferedImage file_qxd_Buffered_Image_with_Reflection;
    public static BufferedImage file_psd_Buffered_Image_with_Reflection;
    public static BufferedImage file_png_Buffered_Image_with_Reflection;
    public static BufferedImage file_jpg_Buffered_Image_with_Reflection;
    public static BufferedImage file_iso_Buffered_Image_with_Reflection;
    public static BufferedImage file_inx_Buffered_Image_with_Reflection;
    public static BufferedImage file_ical_Buffered_Image_with_Reflection;
    public static BufferedImage file_gif_Buffered_Image_with_Reflection;
    public static BufferedImage file_flac_Buffered_Image_with_Reflection;
    public static BufferedImage file_cue_Buffered_Image_with_Reflection;
    public static BufferedImage file_bin_Buffered_Image_with_Reflection;
    public static BufferedImage file_aac_Buffered_Image_with_Reflection;
    public static BufferedImage file_bmp_Buffered_Image_with_Reflection;
    public static BufferedImage file_xls_Buffered_Image_with_Reflection;
    public static BufferedImage file_rtf_Buffered_Image_with_Reflection;
    public static BufferedImage file_ppt_Buffered_Image_with_Reflection;
    public static BufferedImage file_pps_Buffered_Image_with_Reflection;
    public static BufferedImage file_svg_Buffered_Image_with_Reflection;
    public static BufferedImage file_pdf_Buffered_Image_with_Reflection;
    public static BufferedImage file_eps_Buffered_Image_with_Reflection;
    public static BufferedImage file_ai_Buffered_Image_with_Reflection;
    public static BufferedImage file_indd_Buffered_Image_with_Reflection;
    public static BufferedImage file_doc_Buffered_Image_with_Reflection;
  
    // x 20
    public static ImageIcon cd_x_20;
    public static ImageIcon folder_red_x_20;
    public static ImageIcon folder_blue_x_20;
    public static ImageIcon folder_gray_x_20;
    public static ImageIcon document_blank_x_20;
    public static ImageIcon file_txt_x_20;
    public static ImageIcon file_mp3_x_20;
    public static ImageIcon file_doc_x_20;
    public static ImageIcon file_indd_16;
    public static ImageIcon file_ai_x_20;
    public static ImageIcon file_eps_x_20;
    public static ImageIcon file_pdf_x_20;
    public static ImageIcon file_svg_x_20;
    public static ImageIcon file_pps_x_20;
    public static ImageIcon file_ppt_x_20;
    public static ImageIcon file_rtf_x_20;
    public static ImageIcon file_xls_x_20;
    public static ImageIcon file_bmp_x_20;
    public static ImageIcon file_aac_x_20;
    public static ImageIcon file_bin_x_20;
    public static ImageIcon file_cue_x_20;
    public static ImageIcon file_flac_x_20;
    public static ImageIcon file_gif_x_20;
    public static ImageIcon file_ical_x_20;
    public static ImageIcon file_inx_x_20;
    public static ImageIcon file_iso_x_20;
    public static ImageIcon file_jpg_x_20;
    public static ImageIcon file_png_x_20;
    public static ImageIcon file_psd_x_20;
    public static ImageIcon file_qxd_x_20;
    public static ImageIcon file_qxp_x_20;
    public static ImageIcon file_raw_x_20;
    public static ImageIcon file_tif_x_20;
    public static ImageIcon file_vcf_x_20;
    public static ImageIcon file_wav_x_20;
    public static ImageIcon box_rar_x_20;
    public static ImageIcon box_zip_x_20;
    public static ImageIcon file_avi_x_20;
    public static ImageIcon file_divx_x_20;
    public static ImageIcon file_flv_x_20;
    public static ImageIcon file_html_x_20;
    public static ImageIcon file_mov_x_20;
    public static ImageIcon file_mpg_x_20;
    public static ImageIcon file_php_x_20;
    public static ImageIcon file_wma_x_20;
    public static ImageIcon file_xml_x_20;
    
    public static ImageIcon drag_and_drop_x_32;
    public static ImageIcon drag_and_drop_white_x_32;
    public static ImageIcon green_rectangle_16_x_8;
    public static ImageIcon red_rectangle_16_x_8;
    public static ImageIcon orange_rectangle_16_x_8;
    public static ImageIcon loading_gif_3_16_x_8;
    public static ImageIcon internet_x_32;
    public static ImageIcon internet_x_64;
    public static ImageIcon internet_x_55;
    public static ImageIcon double_ring_x_55;
    public static ImageIcon double2_ring_x_55;
    public static ImageIcon double3_ring_x_55;
    public static ImageIcon password_x_64;
    public static ImageIcon password_x_55;
    public static ImageIcon user_x_55;
    public static ImageIcon list_x_55;
    public static ImageIcon success_x_55;
    public static ImageIcon info_x_55;
    public static ImageIcon warning_x_55;
    public static ImageIcon error_x_55;
    public static ImageIcon download_x_55;
}
