package com.kob.backend.service.user.account;

import java.util.Map;
//使用mapper处理具体业务逻辑，接受controller调度
public interface LoginService {
    public Map<String,String> getToken(String username,String password);
}
