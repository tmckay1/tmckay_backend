package com.thetylermckay.backend.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import com.thetylermckay.backend.helpers.FileHelper;

public class ImageStreamer {
	
	private HttpServletResponse response;
	
	public ImageStreamer(HttpServletResponse response) {
		this.response = response;
	}
	
	public void streamImage(String imageName) throws IOException, IllegalArgumentException {
		String imageType = getImageType(imageName);
		String path = String.format("images/%s", imageName);
		ClassPathResource imgFile = new ClassPathResource(path);
	    this.response.setContentType(imageType);
	    StreamUtils.copy(imgFile.getInputStream(), this.response.getOutputStream());
	}
	
	private String getImageType(String imageName) {
		String extension = FileHelper.getFileExtension(imageName);

		switch(extension) {
		case "jpg":
			return MediaType.IMAGE_JPEG_VALUE;
		case "png":
			return MediaType.IMAGE_PNG_VALUE;
		case "gif":
			return MediaType.IMAGE_GIF_VALUE;
		default:
			throw new IllegalArgumentException();
		}
	}
}
