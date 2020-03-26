package com.thetylermckay.backend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thetylermckay.backend.config.ServerProperties;
import com.thetylermckay.backend.helpers.ImageStreamer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ImagesController.class)
@Import({ ServerProperties.class, ImageStreamer.class })
public class ImagesControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void shouldShowImage() throws Exception {
    mvc.perform(get("/images/dogs.jpg")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldNotShowImage() throws Exception {
    mvc.perform(get("/images/dogse.jpg")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is4xxClientError());
    mvc.perform(get("/images/dogs")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is4xxClientError());
  }
}
