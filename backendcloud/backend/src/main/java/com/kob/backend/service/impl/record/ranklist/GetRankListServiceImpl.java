package com.kob.backend.service.impl.record.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRankListServiceImpl implements GetRankListService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userIPage = new Page<>(page, 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users;
        if (page != -1)
            users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        else users = userMapper.selectList(null);
        JSONObject resp = new JSONObject();
        int rank = 1;
        for (User user : users) {
            user.setPassword("No." + rank);
            rank++;
        }
        resp.put("users_count", userMapper.selectCount(null));
        resp.put("users", users);
        return resp;
    }
}
