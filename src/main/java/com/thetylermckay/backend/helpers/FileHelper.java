package com.thetylermckay.backend.helpers;

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
}
