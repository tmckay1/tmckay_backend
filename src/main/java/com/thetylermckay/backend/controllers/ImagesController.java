package com.thetylermckay.backend.controllers;

import com.thetylermckay.backend.exceptions.ImageNotFoundException;
import com.thetylermckay.backend.services.ImageStreamer;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/images")
public class ImagesController {

  
  /**
   * Output the given image from our resource directory (uploaded images).
   * @param imageName The name of the image to show
   * @param response The response to append the image to
   */
  @GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
  public void getImage(@PathVariable("imageName") String imageName, HttpServletResponse response) {
    ImageStreamer streamer = new ImageStreamer(response);
    try {
      streamer.streamImage(imageName);
    } catch (IOException | IllegalArgumentException e) {
      throw new ImageNotFoundException();
    }
  }
}
