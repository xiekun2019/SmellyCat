package com.xiekun.cat.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiekun.cat.dto.GiteeAccessTokenDTO;
import com.xiekun.cat.dto.GithubAccessTokenDTO;
import com.xiekun.cat.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GiteeProvider {
    public String getAccessToken(GiteeAccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));

        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            // 返回结果
            // {"access_token":"adf795fbd4b41d138af9ed2fee96e2f8","token_type":"bearer",
            // "expires_in":86400,"refresh_token":"dc81814f1cf9121f62dc1152ba91bde3cc0e0f7fd226b14e4b79590c0f6498c3","scope":"user_info","created_at":1632889562}
            String responseStr = response.body().string();
            System.out.println(responseStr);
            JSONObject object = JSON.parseObject(responseStr);
            String token = (String) object.get("access_token");
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseStr = response.body().string();
            return JSON.parseObject(responseStr, GithubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
