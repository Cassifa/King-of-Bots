package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Map<String, String> getinfo() {

        UsernamePasswordAuthenticationToken authentication=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser =(UserDetailsImpl) authentication.getPrincipal();
        User user=loginUser.getUser();

        Map<String,String> map=new HashMap<>();
        QueryWrapper<User> queryWrapper=new QueryWrapper<User>();
        queryWrapper.eq("username",user.getUsername());
        User user1=userMapper.selectOne(queryWrapper);
        map.put("error_message","success");
        map.put("id",user1.getId().toString());
        map.put("username", user.getUsername());
        map.put("rating", user1.getRating().toString());
        map.put("photo",user.getPhoto());
        return map;
    }
}
