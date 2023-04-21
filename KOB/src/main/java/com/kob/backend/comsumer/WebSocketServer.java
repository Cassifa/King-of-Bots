package com.kob.backend.comsumer;
import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.comsumer.utils.Game;
import com.kob.backend.comsumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import jdk.nashorn.internal.scripts.JO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    public static ConcurrentHashMap<Integer,WebSocketServer> users=new ConcurrentHashMap<>();
    //匹配池
    private final static CopyOnWriteArraySet<User> matchpoll=new CopyOnWriteArraySet<>();
    private Session session =null;
    private User user;

    //非单例模式
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;

    private Game game=null;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper=userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper=recordMapper;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session=session;
        System.out.println("continted");
        Integer userId= JwtAuthentication.getUserId(token);
        this.user=userMapper.selectById(userId);

        if(this.user!=null){
            users.put(userId,this);
        }else{
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("closed");
        if(this.user!=null){
            users.remove(this.user.getId() );
            matchpoll.remove(this.user);
        }
    }

    private void startMatching(){
        matchpoll.add(this.user);
        System.out.println("startMatching");
        while (matchpoll.size()>=2){
            Iterator<User> it=matchpoll.iterator();
            User a=it.next(),b=it.next();
            matchpoll.remove(a);
            matchpoll.remove(b);

            Game game=new Game(13,14,20,a.getId(),b.getId());
            game.createMap();

            users.get(a.getId()).game=game;
            users.get(b.getId()).game=game;

            game.start();

            JSONObject respGame=new JSONObject();
            respGame.put("a_id",game.getPlayerA().getId());
            respGame.put("a_sx",game.getPlayerA().getSx());
            respGame.put("a_sy",game.getPlayerA().getSy());
            respGame.put("b_id",game.getPlayerB().getId());
            respGame.put("b_sx",game.getPlayerB().getSx());
            respGame.put("b_sy",game.getPlayerB().getSy());
            respGame.put("gamemap",game.getG());

            JSONObject respA= new JSONObject();
            respA.put("event","start-matching");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("game",respGame);
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB= new JSONObject();
            respB.put("event","start-matching");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("game",respGame);
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    private void stopMatching(){
        matchpoll.remove(this.user);
        System.out.println("stopMatching");
    }

    private void move(Integer d){
        if(game.getPlayerA().getId()==user.getId()){
            game.setNextStepA(d);
        }else if(game.getPlayerB().getId()==user.getId()){
            game.setNextStepB(d);
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("recived");
        JSONObject data=JSONObject.parseObject(message);
        String event=data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    public void sendMessage(String message){//发送信息
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
    public void setSession(String message) {
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
