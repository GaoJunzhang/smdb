package com.zgj.mps.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {
    private String account;
    private String password;
    private String code;
    private String smsCode;
    private String mobile;
}
