package co.yiiu.web.admin;

import co.yiiu.config.properties.SiteConfig;
import co.yiiu.core.base.BaseController;
import co.yiiu.core.bean.Page;
import co.yiiu.core.bean.Result;
import co.yiiu.module.comment.pojo.Comment;
import co.yiiu.module.comment.pojo.CommentWithBLOBs;
import co.yiiu.module.comment.service.CommentService;
import co.yiiu.module.topic.pojo.Topic;
import co.yiiu.module.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController extends BaseController {

  @Autowired
  private SiteConfig siteConfig;
  @Autowired
  private CommentService commentService;
  @Autowired
  private TopicService topicService;

  /**
   * 评论列表
   *
   * @param model
   * @return
   */
  @GetMapping("/list")
  public String list(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
    Page<Map> page = commentService.findAllForAdmin(pageNo, siteConfig.getPageSize());
    model.addAttribute("page", page);
    return "admin/comment/list";
  }

  /**
   * 编辑评论
   *
   * @param id
   * @param model
   * @return
   */
  @GetMapping("/edit")
  public String edit(Integer id, Model model) {
    Comment comment = commentService.findById(id);
    Topic topic = topicService.findById(comment.getTopicId());
    model.addAttribute("comment", comment);
    model.addAttribute("topic", topic);
    return "admin/comment/edit";
  }

  /**
   * 更新评论内容
   *
   * @param id
   * @param content
   * @return
   */
  @PostMapping("/edit")
  @ResponseBody
  public Result update(Integer id, String content) {
    CommentWithBLOBs comment = commentService.findById(id);
    Assert.notNull(comment, "评论不存在");

    comment.setContent(content);
    commentService.update(comment);
    return Result.success();
  }

  /**
   * 删除评论
   *
   * @param id
   * @return
   */
  @GetMapping("/delete")
  @ResponseBody
  public Result delete(Integer id) {
    commentService.delete(id, getAdminUser().getId());
    return Result.success();
  }

}
