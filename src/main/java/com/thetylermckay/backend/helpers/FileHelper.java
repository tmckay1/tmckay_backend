package com.thetylermckay.backend.helpers;

public class FileHelper {
	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		
		return (i > p) ? fileName.substring(i+1).toLowerCase() : "";
	}
}
