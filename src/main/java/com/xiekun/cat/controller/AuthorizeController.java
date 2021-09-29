package com.xiekun.cat.controller;

import com.xiekun.cat.dto.GiteeAccessTokenDTO;
import com.xiekun.cat.dto.GithubAccessTokenDTO;
import com.xiekun.cat.dto.GithubUser;
import com.xiekun.cat.mapper.UserMapper;
import com.xiekun.cat.model.User;
import com.xiekun.cat.provider.GiteeProvider;
import com.xiekun.cat.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GiteeProvider giteeProvider;

    @Value("${gitee.client.id}")
    private String clientId;

    @Value("${gitee.client.secret}")
    private String clientSecret;

    @Value("${gitee.grant.type}")
    private String grantType;

    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           HttpServletRequest request) {
        GiteeAccessTokenDTO accessTokenDTO = new GiteeAccessTokenDTO();
        accessTokenDTO.setGrant_type(grantType);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = giteeProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = giteeProvider.getUser(accessToken);
        if (githubUser != null) {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            // 登录成功，写 cookie 和 session
            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        } else {
            // 登录失败，重新登录
            return "redirect:/";
        }
    }
}
