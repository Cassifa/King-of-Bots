package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.awt.*;
import java.awt.event.*;
// 主要是下面这行注解
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class Login extends Frame{
//    private final TextField usernameField;
//    private final TextField passwordField;
//    public Login() {
//        super("注册");
//        // 创建用户名和密码输入框
//        usernameField = new TextField(20);
//        passwordField = new TextField(20);
//        passwordField.setEchoChar('*');
//        // 创建登录按钮
//        Button loginButton = new Button("Login");
//        loginButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String username = usernameField.getText();
//                String password = passwordField.getText();
//                // 校验用户名和密码
//                if (username.equals("admin") && password.equals("password")) {
//                    System.out.println("Login success");
//                } else {
//                    System.out.println("Login failed");
//                }
//            }
//        });
//        // 将控件添加到窗口中
//        setLayout(new GridLayout(3, 2));
//        add(new Label("用户名: "));
//        add(usernameField);
//        add(new Label("password: "));
//        add(passwordField);
//        add(new Label());
//        add(loginButton);
//        // 设置窗口大小和关闭操作
//        setSize(300, 150);
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                dispose();
//            }
//        });
//    }
//}
class KobApplicationTests {
    @Test
    void contextLoads() {
//        Login loginWindow = new Login();
//        loginWindow.setVisible(true);
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123"));
        System.out.println(passwordEncoder.matches("123","$2a$10$kwzYw92JYAhANMXoc8kH5epp95PsTBlaLcvckAVJ77cgzNAfNqXoK"));
        String password="123";
        String encodedPassword=passwordEncoder.encode(password);
        System.out.println(encodedPassword);
   }

}