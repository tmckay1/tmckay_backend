package com.thetylermckay.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectController {

  @RequestMapping(
      value = {"/{path:[^\\.]*}", "/**/{path:^(?!oauth).*}/{path:[^\\.]*}"},
      method = RequestMethod.GET
  )
  public String redirect() {
    return "forward:/index.html";
  }
}
