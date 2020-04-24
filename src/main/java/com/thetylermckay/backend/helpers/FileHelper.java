package com.thetylermckay.backend.helpers;

import java.io.File;
import org.apache.commons.io.FilenameUtils;

public class FileHelper {
  /**
   * Get the file extension for the given name.
   * @param fileName Name of the file to get the extension for
   * @return String The file extension or an empty string if fileName
   *        is not a valid file name.
   */
  public static String getFileExtension(String fileName) {
    int i = fileName.lastIndexOf('.');
    int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

    return (i > p) ? fileName.substring(i + 1).toLowerCase() : "";
  }

  public static String getFileBaseName(String fileName) {
    return FilenameUtils.getBaseName(fileName);
  }
  
  public static boolean fileExists(String filePath) {
    return (new File(filePath)).exists();
  }
}
