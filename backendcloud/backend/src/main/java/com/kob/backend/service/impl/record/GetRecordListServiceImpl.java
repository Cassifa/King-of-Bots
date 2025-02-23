package com.kob.backend.service.impl.record;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        //Mybatis-Plus API
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records;
        if (page != -1)
            records= recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        else records=recordMapper.selectList(null);

        JSONObject jsonObject = new JSONObject();
        List<JSONObject> items = new LinkedList<>();

        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            if (userA == null || userB == null) {
                continue;
            }
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            item.put("record", record);
            String result = "平局";
            if ("a".equals(record.getLoser())) result = "B胜";
            else if ("b".equals(record.getLoser())) result = "A胜";
            item.put("result", result);
            items.add(item);
        }

        jsonObject.put("records", items);
        jsonObject.put("record_count", recordMapper.selectCount(null));
        System.out.println(items.toString());
        return jsonObject;
    }
}
