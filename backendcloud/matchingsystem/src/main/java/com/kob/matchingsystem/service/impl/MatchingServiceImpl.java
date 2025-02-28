package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool =new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating,Integer botId) {
        System.out.println("addPlayer"+userId);
        matchingPool.addPlayer(userId,rating,botId);
        return "addsuccess";
    }

    @Override
    public String removePlayer(Integer userId, Integer rating) {
        System.out.println("removePlayer"+userId);
        matchingPool.removePlayer(userId);
        return "remove success";
    }
}
