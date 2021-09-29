package com.xiekun.cat.provider;

import com.alibaba.fastjson.JSON;
import com.xiekun.cat.dto.GithubAccessTokenDTO;
import com.xiekun.cat.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(GithubAccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseStr = response.body().string();
            System.out.println(responseStr);
            String tokenStr = responseStr.split("&")[0];
            String[] tokenSplit = tokenStr.split("=");
            if (tokenSplit[0].equals("access_token")) {
                return tokenSplit[1];
            }
//            System.out.println(responseStr);
//            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseStr = response.body().string();
            return JSON.parseObject(responseStr, GithubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
