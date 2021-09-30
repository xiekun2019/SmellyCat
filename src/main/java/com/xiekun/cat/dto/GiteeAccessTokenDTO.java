package com.xiekun.cat.dto;

import lombok.Data;

@Data
public class GiteeAccessTokenDTO {
    private String grant_type;
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    // private String state;
}
