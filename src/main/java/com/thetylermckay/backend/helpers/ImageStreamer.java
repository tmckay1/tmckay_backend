package com.thetylermckay.backend.helpers;

import com.thetylermckay.backend.config.ServerProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class ImageStreamer {
  
  @Autowired
  private ServerProperties serverProperties;

  /**
   * Stream an image to the given response.
   * @param imageName Name of the image to stream
   * @throws IOException Exception to encounter if the image does not exist
   * @throws IllegalArgumentException Exception to throw if the image does
   *        not have a valid extension
   */
  public void streamImage(String imageName, HttpServletResponse response)
      throws IOException, IllegalArgumentException {
    String imagePath = String.format("%s/images/%s", serverProperties.getResourcePath(), imageName);
    String absPath = (new File(imagePath)).getAbsolutePath();
    response.setContentType(getImageType(imageName));
    StreamUtils.copy(new FileInputStream(absPath), response.getOutputStream());
  }

  private String getImageType(String imageName) {
    String extension = FileHelper.getFileExtension(imageName).toLowerCase();

    switch (extension) {
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
