package com.kob.backend.mapper;
import com.kob.backend.comsumer.utils.Player;
import com.kob.backend.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    Object selectById(Player playerA, long id);
    //将pojo class加上sql语句
}
