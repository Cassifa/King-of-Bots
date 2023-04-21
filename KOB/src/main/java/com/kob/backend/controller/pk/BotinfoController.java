package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk/")
public class BotinfoController {
    //负责请求转发，接受页面过来的参数，传给Service处理，接到返回值，再传给页面
    @RequestMapping("getbotinfo/")
    public Map<String,String> getBotInfo(){
        Map<String,String> map = new HashMap<>();
        map.put("name","Cassifa");
        map.put("rating","800");
        return map;
    }
}
