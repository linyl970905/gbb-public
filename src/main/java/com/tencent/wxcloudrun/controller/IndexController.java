package com.tencent.wxcloudrun.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * index控制器
 */
@RestController
@RequestMapping("/gbb")
public class IndexController {

  /**
   * 主页页面
   *
   * @return API response html
   */
  @GetMapping
  public String index() {
    return "index";
  }

  @GetMapping("/welcome")
  public String welcomePage() {
    return "Welcome To ... GBB!";
  }
}
