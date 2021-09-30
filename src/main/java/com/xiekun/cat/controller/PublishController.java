package com.xiekun.cat.controller;

import com.xiekun.cat.mapper.QuestionMapper;
import com.xiekun.cat.mapper.UserMapper;
import com.xiekun.cat.model.Question;
import com.xiekun.cat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model) {
        // 回显标题、补充和标签
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        if(title == null || title.equals("")) {
            model.addAttribute("error", "标题不能为空！");
            return "publish";
        }

        if(description == null || description.equals("")) {
            model.addAttribute("error", "问题补充不能为空！");
            return "publish";
        }

        if(tag == null || tag.equals("")) {
            model.addAttribute("error", "标签不能为空！");
            return "publish";
        }

        User user = null;
        Cookie[] cookies = request.getCookies();
        // 如果 cookie 为空，说明用户没有登陆过
        if(cookies == null) {
            model.addAttribute("error", "用户未登录！");
            return "publish";
        }
        // 通过 cookie 获取用户 id
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        // 如果没有查询到用户信息，说明用户未登录，提示前端
        if (user == null) {
            model.addAttribute("error", "用户未登录！");
            return "publish";
        }
        // 构建 Question 对象，存储到数据库中
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
