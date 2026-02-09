/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_a;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author user
 */
public class Icons {
    
    public Icon getIcon(String iconType){

        Icon icon = null;
             switch(iconType){
                case "Computer":
                  //  icon = StaticValues.monitorIcon;
                    break;
                    
                case "Phone android":
                    
                    break;
                    
                case "hardDrive":
                   icon = StaticValues.harddrive;
                    break;
           
                case "cdDrive":
                      icon = StaticValues.cd;
                    break;
                    
                case "folder":
                      icon = StaticValues.folder_blue_x_20;
                        if(StaticValues.military_theme){
                            icon = StaticValues.folder_gray_x_20;
                        }
                    break;
                    
                default: 
                    File file = new File(iconType); //Note icontype is represents actual file name
                 
                    String fileExtension = FilenameUtils.getExtension(iconType);
                 
                    
                    //if statements are suitable instead of a switch because we some extensions could contain upper and small case
                    if(StringUtils.equalsIgnoreCase(fileExtension, "txt"))
                        icon = StaticValues.file_txt;
                    else
                        if(StringUtils.equalsIgnoreCase(fileExtension, "mp3"))
                           icon = StaticValues.file_mp3;
                    else
                            if(StringUtils.equalsIgnoreCase(fileExtension, "doc") || StringUtils.equalsIgnoreCase(fileExtension, "docx"))
                             icon = StaticValues.file_doc;
                    else
                                if(StringUtils.equalsIgnoreCase(fileExtension, "indd"))
                                     icon = StaticValues.file_indd;
                    else
                                    if(StringUtils.equalsIgnoreCase(fileExtension, "ai"))
                                         icon = StaticValues.file_ai;
                    else
                                        if(StringUtils.equalsIgnoreCase(fileExtension, "eps"))
                                            icon = StaticValues.file_eps;
                    else
                                            if(StringUtils.equalsIgnoreCase(fileExtension, "pdf"))
                                                icon = StaticValues.file_pdf;
                    else
                                                if(StringUtils.equalsIgnoreCase(fileExtension, "svg"))
                                                    icon = StaticValues.file_svg;
                    else
                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "pps") )
                                                        icon = StaticValues.file_pps;
                    else
                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "ppt"))
                                                          icon = StaticValues.file_ppt;
                    else
                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "rtf"))
                                                                icon = StaticValues.file_rtf;
                    else
                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "xls"))
                                                                    icon = StaticValues.file_xls;
                    else
                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "bmp"))
                                                                        icon = StaticValues.file_bmp;
                    else
                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "aac"))
                                                                            icon = StaticValues.file_aac;
                    else
                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "bin"))
                                                                                 icon = StaticValues.file_bin;
                    else
                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "cue"))
                                                                                    icon = StaticValues.file_cue;
                    else
                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "flac"))
                                                                                        icon = StaticValues.file_flac;
                    else
                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "gif"))
                                                                                            icon = StaticValues.file_gif;
                    else
                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "ical"))
                                                                                                icon = StaticValues.file_ical;
                    else
                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "inx"))
                                                                                                    icon = StaticValues.file_inx;
                    else
                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "iso"))
                                                                                                        icon = StaticValues.file_iso;
                    else
                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "jpg"))
                                                                                                            icon = StaticValues.file_jpg;
                    else
                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "png"))
                                                                                                                icon = StaticValues.file_png;
                    else
                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "psd"))
                                                                                                                    icon = StaticValues.file_psd;
                    else
                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "qxd"))
                                                                                                                        icon = StaticValues.file_qxd;
                    else
                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "qxp"))
                                                                                                                            icon = StaticValues.file_qxp;
                    else
                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "raw"))
                                                                                                                                icon = StaticValues.file_raw;
                    else
                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "tif"))
                                                                                                                                    icon = StaticValues.file_tif;
                    else
                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "vcf"))
                                                                                                                                        icon =  StaticValues.file_vcf;
                    else
                                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "wav"))
                                                                                                                                            icon = StaticValues.file_wav;
                    else
                                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "rar"))
                                                                                                                                                icon = StaticValues.box_rar;
                    else
                                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "zip"))
                                                                                                                                                    icon = StaticValues.box_zip;
                    else
                                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "avi"))
                                                                                                                                                        icon = StaticValues.file_avi;
                    else
                                                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "divx"))
                                                                                                                                                            icon = StaticValues.file_divx;
                    else
                                                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "flv"))
                                                                                                                                                                icon = StaticValues.file_flv;
                    else
                                                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "html") || StringUtils.equalsIgnoreCase(fileExtension, "htm")  )
                                                                                                                                                                     icon = StaticValues.file_html;
                    else
                                                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "mov"))
                                                                                                                                                                        icon = StaticValues.file_mov;
                    else
                                                                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "mpg"))
                                                                                                                                                                            icon = StaticValues.file_mpg;
                    else
                                                                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "php"))
                                                                                                                                                                                icon = StaticValues.file_php;
                    else
                                                                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "wma"))
                                                                                                                                                                                    icon = StaticValues.file_wma;
                    else
                                                                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "xml"))
                                                                                                                                                                                        icon = StaticValues.file_xml;
                    else
                                                                                                                                                                                         try{
                                                                                                                                                                                          //  icon = new ImageIcon(iconToImage(javax.swing.filechooser.FileSystemView.getFileSystemView().getSystemIcon(file)));
                                                                                                                                                                                             icon = StaticValues.document_blank;
                                                                                                                                                                                             }
                                                                                                                                                                                             catch(NullPointerException ex){
                                                                                                                                                                                             }
                           
                   
            } 
             
             return icon;
    }
    
    //get the buffered image of the icon
    public BufferedImage Get_Buffered_Image(String iconType, boolean with_Reflection){ //if buffered image with reflection is requested then 'with_Reflection' will be true

        BufferedImage bufferedImage = null;
             switch(iconType){
                case "Computer":
                  //  icon = StaticValues.monitorIcon;
                    break;
                    
                case "Phone android":
                    
                    break;
                    
                case "hardDrive":
                   bufferedImage = StaticValues.harddrive_Buffered_Image;
                    break;
           
                case "cdDrive":
                      bufferedImage = StaticValues.cd_Buffered_Image;
                    break;
                    
                case "folder":
                      bufferedImage = StaticValues.folder_blue_x_48_Buffered_Image;
                        if(StaticValues.military_theme){
                            bufferedImage = StaticValues.folder_gray_x_48_Buffered_Image;
                        }
                    break;
                    
                default: 
                    File file = new File(iconType); //Note bufferedImagetype is represents actual file name
                 
                    String fileExtension = FilenameUtils.getExtension(iconType);
                 
                    
                    //if statements are suitable instead of a switch because we some extensions could contain upper and small case
                    if(StringUtils.equalsIgnoreCase(fileExtension, "txt")){
                        bufferedImage = StaticValues.file_txt_Buffered_Image;
                        if(with_Reflection)
                             bufferedImage = StaticValues.file_txt_Buffered_Image_with_Reflection; 
                    }
                    else
                        if(StringUtils.equalsIgnoreCase(fileExtension, "mp3")){
                           bufferedImage = StaticValues.file_mp3_Buffered_Image;
                           if(with_Reflection)
                                bufferedImage = StaticValues.file_mp3_Buffered_Image_with_Reflection;
                        }
                    else
                            if(StringUtils.equalsIgnoreCase(fileExtension, "doc") || StringUtils.equalsIgnoreCase(fileExtension, "docx")){
                             bufferedImage = StaticValues.file_doc_Buffered_Image;
                             if(with_Reflection)
                                bufferedImage = StaticValues.file_doc_Buffered_Image_with_Reflection;
                            }
                    else
                                if(StringUtils.equalsIgnoreCase(fileExtension, "indd")){
                                     bufferedImage = StaticValues.file_indd_Buffered_Image;
                                     if(with_Reflection)
                                        bufferedImage = StaticValues.file_indd_Buffered_Image_with_Reflection;
                                }
                    else
                                    if(StringUtils.equalsIgnoreCase(fileExtension, "ai")){
                                         bufferedImage = StaticValues.file_ai_Buffered_Image;
                                         if(with_Reflection)
                                            bufferedImage = StaticValues.file_ai_Buffered_Image_with_Reflection;
                                    }
                    else
                                        if(StringUtils.equalsIgnoreCase(fileExtension, "eps")){
                                            bufferedImage = StaticValues.file_eps_Buffered_Image;
                                            if(with_Reflection)
                                                bufferedImage = StaticValues.file_eps_Buffered_Image_with_Reflection;
                                        }
                    else
                                            if(StringUtils.equalsIgnoreCase(fileExtension, "pdf")){
                                                bufferedImage = StaticValues.file_pdf_Buffered_Image;
                                                if(with_Reflection)
                                                    bufferedImage = StaticValues.file_pdf_Buffered_Image_with_Reflection;
                                            }
                    else
                                                if(StringUtils.equalsIgnoreCase(fileExtension, "svg")){
                                                    bufferedImage = StaticValues.file_svg_Buffered_Image;
                                                    if(with_Reflection)
                                                        bufferedImage = StaticValues.file_svg_Buffered_Image_with_Reflection;
                                                }
                    else
                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "pps")){
                                                        bufferedImage = StaticValues.file_pps_Buffered_Image;
                                                        if(with_Reflection)
                                                            bufferedImage = StaticValues.file_pps_Buffered_Image_with_Reflection;
                                                    }
                    else
                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "ppt")){
                                                          bufferedImage = StaticValues.file_ppt_Buffered_Image;
                                                          if(with_Reflection)
                                                            bufferedImage = StaticValues.file_ppt_Buffered_Image_with_Reflection;
                                                        }
                    else
                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "rtf")){
                                                                bufferedImage = StaticValues.file_rtf_Buffered_Image;
                                                                if(with_Reflection)
                                                                    bufferedImage = StaticValues.file_rtf_Buffered_Image_with_Reflection;
                                                            }
                    else
                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "xls")){
                                                                    bufferedImage = StaticValues.file_xls_Buffered_Image;
                                                                    if(with_Reflection)
                                                                        bufferedImage = StaticValues.file_xls_Buffered_Image_with_Reflection;
                                                                }
                    else
                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "bmp")){
                                                                        bufferedImage = StaticValues.file_bmp_Buffered_Image;
                                                                        if(with_Reflection)
                                                                            bufferedImage = StaticValues.file_bmp_Buffered_Image_with_Reflection;
                                                                    }
                    else
                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "aac")){
                                                                            bufferedImage = StaticValues.file_aac_Buffered_Image;
                                                                            if(with_Reflection)
                                                                                bufferedImage = StaticValues.file_aac_Buffered_Image_with_Reflection;
                                                                        }
                    else
                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "bin")){
                                                                                 bufferedImage = StaticValues.file_bin_Buffered_Image;
                                                                                 if(with_Reflection)
                                                                                    bufferedImage = StaticValues.file_bin_Buffered_Image_with_Reflection;
                                                                            }
                    else
                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "cue")){
                                                                                    bufferedImage = StaticValues.file_cue_Buffered_Image;
                                                                                    if(with_Reflection)
                                                                                        bufferedImage = StaticValues.file_cue_Buffered_Image_with_Reflection;
                                                                                }
                    else
                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "flac")){
                                                                                        bufferedImage = StaticValues.file_flac_Buffered_Image;
                                                                                        if(with_Reflection)
                                                                                            bufferedImage = StaticValues.file_flac_Buffered_Image_with_Reflection;
                                                                                    }
                    else
                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "gif")){
                                                                                            bufferedImage = StaticValues.file_gif_Buffered_Image;
                                                                                            if(with_Reflection)
                                                                                                bufferedImage = StaticValues.file_gif_Buffered_Image_with_Reflection;
                                                                                        }
                    else
                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "ical")){
                                                                                                bufferedImage = StaticValues.file_ical_Buffered_Image;
                                                                                                if(with_Reflection)
                                                                                                    bufferedImage = StaticValues.file_ical_Buffered_Image_with_Reflection;
                                                                                            }
                    else
                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "inx")){
                                                                                                    bufferedImage = StaticValues.file_inx_Buffered_Image;
                                                                                                    if(with_Reflection)
                                                                                                        bufferedImage = StaticValues.file_inx_Buffered_Image_with_Reflection;
                                                                                                }
                    else
                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "iso")){
                                                                                                        bufferedImage = StaticValues.file_iso_Buffered_Image;
                                                                                                        if(with_Reflection)
                                                                                                            bufferedImage = StaticValues.file_iso_Buffered_Image_with_Reflection;
                                                                                                    }
                    else
                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "jpg")){
                                                                                                            bufferedImage = StaticValues.file_jpg_Buffered_Image;
                                                                                                            if(with_Reflection)
                                                                                                                bufferedImage = StaticValues.file_jpg_Buffered_Image_with_Reflection;
                                                                                                        }
                    else
                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "png")){
                                                                                                                bufferedImage = StaticValues.file_png_Buffered_Image;
                                                                                                                if(with_Reflection)
                                                                                                                    bufferedImage = StaticValues.file_png_Buffered_Image_with_Reflection;
                                                                                                            }
                    else
                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "psd")){
                                                                                                                    bufferedImage = StaticValues.file_psd_Buffered_Image;
                                                                                                                    if(with_Reflection)
                                                                                                                        bufferedImage = StaticValues.file_psd_Buffered_Image_with_Reflection;
                                                                                                                }
                    else
                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "qxd")){
                                                                                                                        bufferedImage = StaticValues.file_qxd_Buffered_Image;
                                                                                                                        if(with_Reflection)
                                                                                                                            bufferedImage = StaticValues.file_qxd_Buffered_Image_with_Reflection;
                                                                                                                    }
                    else
                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "qxp")){
                                                                                                                            bufferedImage = StaticValues.file_qxp_Buffered_Image;
                                                                                                                            if(with_Reflection)
                                                                                                                                bufferedImage = StaticValues.file_qxp_Buffered_Image_with_Reflection;
                                                                                                                        }
                    else
                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "raw")){
                                                                                                                                bufferedImage = StaticValues.file_raw_Buffered_Image;
                                                                                                                                if(with_Reflection)
                                                                                                                                    bufferedImage = StaticValues.file_raw_Buffered_Image_with_Reflection;
                                                                                                                            }
                    else
                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "tif")){
                                                                                                                                    bufferedImage = StaticValues.file_tif_Buffered_Image; 
                                                                                                                                    if(with_Reflection)
                                                                                                                                        bufferedImage = StaticValues.file_tif_Buffered_Image_with_Reflection; 
                                                                                                                                }
                    else
                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "vcf")){
                                                                                                                                        bufferedImage =  StaticValues.file_vcf_Buffered_Image; 
                                                                                                                                        if(with_Reflection)
                                                                                                                                            bufferedImage =  StaticValues.file_vcf_Buffered_Image_with_Reflection; 
                                                                                                                                    }
                    else
                                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "wav")){
                                                                                                                                            bufferedImage = StaticValues.file_wav_Buffered_Image; 
                                                                                                                                            if(with_Reflection)
                                                                                                                                                bufferedImage = StaticValues.file_wav_Buffered_Image_with_Reflection; 
                                                                                                                                        }
                    else
                                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "rar")){
                                                                                                                                                bufferedImage = StaticValues.box_rar_Buffered_Image; 
                                                                                                                                                if(with_Reflection)
                                                                                                                                                    bufferedImage = StaticValues.box_rar_Buffered_Image_with_Reflection; 
                                                                                                                                            }
                    else
                                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "zip")){
                                                                                                                                                    bufferedImage = StaticValues.box_zip_Buffered_Image; 
                                                                                                                                                    if(with_Reflection)
                                                                                                                                                        bufferedImage = StaticValues.box_zip_Buffered_Image_with_Reflection; 
                                                                                                                                                }
                    else
                                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "avi")){
                                                                                                                                                        bufferedImage = StaticValues.file_avi_Buffered_Image; 
                                                                                                                                                        if(with_Reflection)
                                                                                                                                                            bufferedImage = StaticValues.file_avi_Buffered_Image_with_Reflection; 
                                                                                                                                                    }
                    else
                                                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "divx")){
                                                                                                                                                            bufferedImage = StaticValues.file_divx_Buffered_Image; 
                                                                                                                                                            if(with_Reflection)
                                                                                                                                                                bufferedImage = StaticValues.file_divx_Buffered_Image_with_Reflection; 
                                                                                                                                                        }
                    else
                                                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "flv")){
                                                                                                                                                                bufferedImage = StaticValues.file_flv_Buffered_Image; 
                                                                                                                                                                if(with_Reflection)
                                                                                                                                                                    bufferedImage = StaticValues.file_flv_Buffered_Image_with_Reflection; 
                                                                                                                                                            }
                    else
                                                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "html") || StringUtils.equalsIgnoreCase(fileExtension, "htm")){
                                                                                                                                                                     bufferedImage = StaticValues.file_html_Buffered_Image; 
                                                                                                                                                                     if(with_Reflection)
                                                                                                                                                                        bufferedImage = StaticValues.file_html_Buffered_Image_with_Reflection; 
                                                                                                                                                                }
                    else
                                                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "mov")){
                                                                                                                                                                        bufferedImage = StaticValues.file_mov_Buffered_Image; 
                                                                                                                                                                        if(with_Reflection)
                                                                                                                                                                            bufferedImage = StaticValues.file_mov_Buffered_Image_with_Reflection; 
                                                                                                                                                                    }
                    else
                                                                                                                                                                        if(StringUtils.equalsIgnoreCase(fileExtension, "mpg")){
                                                                                                                                                                            bufferedImage = StaticValues.file_mpg_Buffered_Image; 
                                                                                                                                                                            if(with_Reflection)
                                                                                                                                                                                bufferedImage = StaticValues.file_mpg_Buffered_Image_with_Reflection; 
                                                                                                                                                                        }
                    else
                                                                                                                                                                            if(StringUtils.equalsIgnoreCase(fileExtension, "php")){
                                                                                                                                                                                bufferedImage = StaticValues.file_php_Buffered_Image; 
                                                                                                                                                                                if(with_Reflection)
                                                                                                                                                                                    bufferedImage = StaticValues.file_php_Buffered_Image_with_Reflection; 
                                                                                                                                                                            }
                    else
                                                                                                                                                                                if(StringUtils.equalsIgnoreCase(fileExtension, "wma")){
                                                                                                                                                                                    bufferedImage = StaticValues.file_wma_Buffered_Image; 
                                                                                                                                                                                    if(with_Reflection)
                                                                                                                                                                                        bufferedImage = StaticValues.file_wma_Buffered_Image_with_Reflection; 
                                                                                                                                                                                }
                    else
                                                                                                                                                                                    if(StringUtils.equalsIgnoreCase(fileExtension, "xml")){
                                                                                                                                                                                        bufferedImage = StaticValues.file_xml_Buffered_Image; 
                                                                                                                                                                                        if(with_Reflection)
                                                                                                                                                                                            bufferedImage = StaticValues.file_xml_Buffered_Image_with_Reflection; 
                                                                                                                                                                                    }
                    else                                                                                                                                                                
                                                                                                                                                                                         try{
                                                                                                                                                                                          //  bufferedImage = new ImageIcon(bufferedImageToImage(javax.swing.filechooser.FileSystemView.getFileSystemView().getSystemIcon(file)))_Buffered_Image;
                                                                                                                                                                                             bufferedImage = StaticValues.document_blank_Buffered_Image;
                                                                                                                                                                                             if(with_Reflection)
                                                                                                                                                                                                bufferedImage = StaticValues.document_blank_Buffered_Image_with_Reflection;
                                                                                                                                                                                             
                                                                                                                                                                                             }
                                                                                                                                                                                             catch(NullPointerException ex){
                                                                                                                                                                                             }
                           
                   
            } 
             
             return bufferedImage;
    }
    
    
}
