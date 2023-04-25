package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor//无参构造函数
@AllArgsConstructor
public class User {
    //表映射成class
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;

    private Integer rating;
    private String photo;
}
