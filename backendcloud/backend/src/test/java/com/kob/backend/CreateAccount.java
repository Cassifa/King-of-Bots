package com.kob.backend;

import java.sql.*;
import java.util.concurrent.locks.Condition;
//@SuppressWarnings("all")
public class CreateAccount {
    public static void main(String[] args) {
        String url=
                "jdbc:mysql://localhost:3306/bookstore?" +
                        "useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user="root";
        String password="254519";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement statement = conn.createStatement()) {
            String sql = "CREATE TABLE `account` (`userid` VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "`balance` FLOAT(6,2)) ENGINE=InnoDB";
            statement.executeUpdate(sql);
            System.out.println("建表成功");

            sql = "INSERT INTO `account` VALUES (?, ?)";
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, "甲");
                pstm.setFloat(2, 500.00f);
                pstm.executeUpdate();

                pstm.setString(1, "乙");
                pstm.setFloat(2, 200.00f);
                pstm.executeUpdate();
            }
            System.out.println("插入成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
