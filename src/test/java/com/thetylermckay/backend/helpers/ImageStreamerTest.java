package com.thetylermckay.backend.helpers;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.thetylermckay.backend.config.ServerProperties;
import com.thetylermckay.backend.controllers.ImagesControllerTest;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(ImagesControllerTest.class)
@Import({ ImageStreamer.class, ServerProperties.class })
public class ImageStreamerTest {
  
  private HttpServletResponse response;

  @Autowired
  private ImageStreamer streamer;
  
  public final ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    this.response = new MockHttpServletResponse();
  }

  @Test
  public void shouldNotThrowExceptionStreamingImage() {
    assertThatCode(() -> streamer.streamImage("dogs.jpg", this.response))
        .doesNotThrowAnyException();
  }
  
  @Test
  public void shouldThrowExceptionStreamingImage() {
    assertThrows(FileNotFoundException.class,
        () -> streamer.streamImage("dogse.jpg", this.response));
    assertThrows(IllegalArgumentException.class,
        () -> streamer.streamImage("dogs", this.response));
  }
}
