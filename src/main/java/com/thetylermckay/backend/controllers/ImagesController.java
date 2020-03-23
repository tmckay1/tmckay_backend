package com.thetylermckay.backend.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.thetylermckay.backend.services.ImageStreamer;
import com.thetylermckay.backend.exceptions.ImageNotFoundException;

@Controller
@RequestMapping(path="/images")
public class ImagesController {

	@GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(@PathVariable("imageName") String imageName, HttpServletResponse response) {
		ImageStreamer streamer = new ImageStreamer(response);
		try {
			streamer.streamImage(imageName);
		}catch(IOException | IllegalArgumentException e) {
			throw new ImageNotFoundException();
		}
    }
}
