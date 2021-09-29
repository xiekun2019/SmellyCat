package com.xiekun.cat.controller;

import com.xiekun.cat.dto.GiteeAccessTokenDTO;
import com.xiekun.cat.dto.GithubAccessTokenDTO;
import com.xiekun.cat.dto.GithubUser;
import com.xiekun.cat.provider.GiteeProvider;
import com.xiekun.cat.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code) {
        GiteeAccessTokenDTO accessTokenDTO = new GiteeAccessTokenDTO();
        accessTokenDTO.setGrant_type(grantType);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = giteeProvider.getAccessToken(accessTokenDTO);
        GithubUser user = giteeProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
