package com.thetylermckay.backend.helpers;

import com.thetylermckay.backend.config.ServerProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

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
    String absPath = getAbsoluteImagePath(imageName);
    response.setContentType(getImageType(imageName));
    StreamUtils.copy(new FileInputStream(absPath), response.getOutputStream());
  }
  
  /**
   * Upload an image to the image directory.
   * @param image Image to upload
   * @return Name of the image
   * @throws IOException Exception to encounter if we cannot write to the
   *        image directory
   */
  public String uploadImage(MultipartFile image)
      throws IOException {
    byte[] bytes = image.getBytes();
    String imageName = getNewImageName(image.getOriginalFilename());
    String absPath = getAbsoluteImagePath(imageName);
    Files.write(Paths.get(absPath), bytes);
    return imageName;
  }
  
  /**
   * For the given image name, check if it exists and if it does keep creating
   * a new name until we find a name that does not exist.
   * @param imageName Name of the original image
   * @return New image name not taken in image directory
   */
  private String getNewImageName(String imageName) {
    String basename = FileHelper.getFileBaseName(imageName);
    String dotExtension = "." + FileHelper.getFileExtension(imageName);
    String newImageName = imageName;

    Integer currentIndex = 0;
    while (FileHelper.fileExists(getAbsoluteImagePath(newImageName))) {
      currentIndex++;
      newImageName = basename + currentIndex.toString() + dotExtension;
    }

    return newImageName;
  }
  
  private String getAbsoluteImagePath(String imageName) {
    String imagePath = String.format("%s/images/%s", serverProperties.getResourcePath(), imageName);
    return (new File(imagePath)).getAbsolutePath();
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
