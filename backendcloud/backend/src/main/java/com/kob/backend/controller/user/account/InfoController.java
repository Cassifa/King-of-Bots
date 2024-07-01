package com.kob.backend.controller.user.account;

import com.kob.backend.comsumer.WebSocketServer;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {
    private final static String sensitiveWordUrl="http://127.0.0.1:2999/SensitiveWord/";

    public static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        //在两个spring boot 间通信
        InfoController.restTemplate=restTemplate;
    }
    @Autowired
    private InfoService infoService;
    @GetMapping("/api/user/account/info/")
    public Map<String,String> getinfo(){
        return infoService.getinfo();
    }

    @GetMapping("/api/user/account/sensitiveWord/")
    public Map<String, String> getSensitiveWord(@RequestParam String text) {
        // 构造请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        // 发送 HTTP POST 请求到另一个项目的接口
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                sensitiveWordUrl,
                requestBody,
                Map.class
        );

        // 获取响应体
        Map<String, String> responseBody = responseEntity.getBody();

        // 返回响应体
        return responseBody;
    }
}
