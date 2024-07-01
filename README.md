# King of Bots:联机在线AI对战游戏



*注：此文档是King of Bots网页端及后端的说明文档，安卓端见：[Li Feifei / KOB_Android · GitLab )](https://git.acwing.com/Cassifa/KOBAndroid)*



## 1.概述

​		**由贪吃蛇游戏改编而来，两名玩家在线联机对战，玩家可以选择指派自己设计的AI参与对战，或亲自出马。同时支持玩家赛后查看比赛回放**



## 2.项目介绍

​		**前后端分离项目，前端由vue3搭配Bootstrap编写；后端为基于Maven的SpringCloud项目，由backend, matchingsystem, botrunningsystem 三个微服务组成，并集成了SpringSecurity, WebSocket, JsonWebToken, MyBatisPlus, FastJson, joor-java-8等服务。比赛为在线对战回合制1v1游戏，玩家操控一条蛇躲避障碍物并设法堵住对方以取得胜利。玩家可以上传由java编写的AI来代替自己出战并赢取天梯积分。支持赛后查看对局录像，并实时公布天梯排名。**



### 2.1系统架构图

![][systemArchitecture]

### 2.2网络架构图

​	项目由四个后端服务，前端通过接口去后端Backend交互，Backend可调用BotRunningSystem、MatchingSystem、SensitiveWord提供的微服务执行特定任务

![][networdArchitectures]

### 2.3系统模块图

​	分为信息管理、游戏与回放、Bot管理、文本脱敏四大模块

![][moduleDiagram]



## 3.需求分析

### 	3.1用户需求分析

用户可以登录注册、与其他玩家匹配游戏、查看对局回放列表、查看天梯排行、上传，编辑自己的代码、查看开发人员发布的公告。

![][UserDemandAnalysis]

### 	3.2 代码运行系统需求分析

BotRunningSystem需要相应Backend的调用，实现动态编译执行玩家代码并将结果返还给Backend功能

![][BotRunningSystemDemandAnalysis]

### 	3.3 匹配系统需求分析

MatchingSystem需要相应Backend的调用，将Backend的传递的玩家信息加入匹配池进行匹配并将匹配成功的结果返回Backend

![][MatchingSystemDemandAnalysis]



## 4.UI界面展示

### 	4.1pk页面

- 匹配页面即主页面

![Image text][index]

- 匹配成功

![Image text][match]

- 游戏结束

![Image text][win]

### 	4.2对局列表

![Image text][gameList]

### 	4.3排行榜

![Image text][rankList]

### 	4.4我的Bot

- Bot列表

![Image text][botList]

- 编辑Bot

![Image text][editBot]

### 	4.5公告板

![Image text][board]

### 	4.6登录注册

![Image text][login]





## 5.后端逻辑泳道图

​		玩家选择出战Bot并开始匹配后Backend会接受玩家匹配请求，建立WebSocket连接并向MatchingSystem发送玩家信息。MatchingSystem 会维护匹配池并每秒匹配一次所有玩家，匹配成功后向Backend发送匹配结果。Backend接收到匹配结果后为两位玩家开辟一个Game线程，线程内维护一局游戏所有信息。Game首先创建一张地图并发送给玩家，然后不断接受玩家或BotRunningSystem的输入并判断输入合法性，直至至少一名玩家有不合法操作为止。检测到不合法操作会向玩家公布比赛结果，结束线程并中断WebSocket连接。

![Image text][process1]

## 6.数据库设计

### 6.1数据库结构

1. User表

   | 列名     | 元素类型      | 备注                 |
   | -------- | ------------- | -------------------- |
   | id       | int           | 用户id               |
   | username | varchar(100)  | 用户名               |
   | password | varchar(100)  | 用户密码，密文       |
   | photo    | varchar(1000) | 用户头像             |
   | rating   | int           | 用户天梯分，默认1500 |

   

2. Record表

   | 列名        | 元素类型      | 备注           |
   | ----------- | ------------- | -------------- |
   | id          | int           | 对局编号       |
   | a_id        | int           | a玩家id        |
   | a_sx        | int           | a玩家起始x坐标 |
   | a_sy        | int           | a玩家起始y坐标 |
   | b_id        | int           | b玩家id        |
   | b_sx        | int           | b玩家起始x坐标 |
   | b_sy        | int           | b玩家起始y坐标 |
   | a_steps     | varchar(1000) | a玩家操作序列  |
   | b_steps     | varchar(1000) | b玩家操作序列  |
   | map         | varchar(1000) | 游戏地图       |
   | loser       | varchar(20)   | 败者           |
   | create_time | datetime      | 对局时间       |

   

3. Bot表

   | 列名        | 元素类型       | 备注         |
   | ----------- | -------------- | ------------ |
   | id          | int            | Bot id       |
   | user_id     | int            | 拥有者id     |
   | title       | varchar(100)   | Bot标题      |
   | description | varchar(300)   | Bot描述      |
   | content     | varchar(10000) | Bot内容      |
   | createtime  | datetime       | 创建时间     |
   | modifytime  | datetime       | 最后修改时间 |

   

### 6.2 ER图

**ER图**

![][ER]

**数据间关系**

![][dataRel]

### 6.3数据流图

**顶层图**

![][dataFlowDiagramtop]



**0层图**

![][dataFlowDiagram0]



## 7.文件结构

以下不展示.gitignore中文件

1. bakendcloud 后端微服务管理
  - backend 主服务后端
    - comsumer 基于websocket，通过与前端、matchingsystem、botrunningsystem通信维护当前游戏进程
    
      - utils 
        - Game.java 游戏地图类
        - Cell.java 小蛇身体的一格
        - Player.java 一局游戏中玩家小蛇状态
        - JwtAuthentication.java 根据玩家token寻找当前玩家ID
      - WebSocketServer.java 用于维护玩家开始匹配后与服务器的全双工连接
    
    - config 配置SpringSecurity JWT验证  MyBatis RestTemplate等功能模块
    
    - controller 接受用户调度
    
      - pk 用户开启游戏，发送小蛇移动申请接口
      - ranklist 获取排行榜接口
      - record 获取对局列表接口
      - user
        - account 用户登录，注册，获取信息接口
        - bot 用户添加，删除，修改自己的Bot接口
    
    - service 业务实现
    
    - mapper 操纵数据库
    
    - pojo 将Bot Record User三个数据库映射
    
    - utils 全局工具类
      - JwtUtil 为玩家分配token实现登录持久化
    
        
    
  - matchingsystem 玩家匹配微服务
    - controller 开设匹配池，接受backend调用添加玩家并将匹配结果返还backend
    
    - service 业务实现
    
    - config SpringSecurity RestTemplate模块配置
    
      
    
  - botrunningsystem 代码运行微服务
    - controller 仅接受backend的调用并向其返回玩家Bot运行结果
    
    - service 业务实现
    
    - utils 模拟AI工具
    
    - config SpringSecurity RestTemplate模块配置
    
      
2. web.src 前端代码地址
  - compontents 存放复用资源快
    - ContentField.vue 卡片容器
    - GmeMap.vue 游戏地图页面
    - MatchingGround.vue 匹配界面
    - NavBar.vue 导航栏
    - PlayGround.vue 游戏页面
    - ResultBoard.vue 游戏结果公示栏
  - views
    - /board 公告栏页面
    - /pk 游戏页面
    - /ranklist 排行榜页面
    - /record 对局回放相关页面
    - /user
      - /account 登录注册相关页面
      - /bot Bot代码修改相关页面
  - assets
    - /images 图片资源
    - /scripts 支持游戏运行的js代码
  - store 存放全局信息
    - pk.js 对局相关信息
    - record.js 记录回放信息
    - user.js 用户登录验证相关信息
  - router 路由
3. README.md



## 8.部分代码逻辑

#### 8.1.小蛇运动

​	小蛇由一串联通的canvas画出的正方形构成，头尾为圆形，头部有眼睛作标识。继承自游戏基类，每秒刷新60次，每次清除之前图像再渲染新图像以达到动画效果。每次移动会向前推进头尾格

**贪吃蛇生命周期流程图**

![][snakeLifeCycle]

```javascript
//游戏基类内函数
const step= timestamp =>{//每秒执行60次
    for(let obj of AC_GAME_OBJESTS){
        //如果没有初始化过则初始化
        if(!obj.has_called_start){
            obj.has_called_start=true;
            obj.start();//启动函数
        }
        //否则更新
        else{
            obj.timedelta=timestamp-last_timestamp;
            obj.update();//更新函数
        }
    }
    last_timestamp=timestamp;
    requestAnimationFrame(step)
}
requestAnimationFrame(step);
//小蛇类
	set_direction(d){//设置移动方向
        this.direacion=d;
    }
    check_tail_increasing(){//是否变长
        if(this.step<=10)return true;
        if(!((this.step-9)%3))return true;
        return false;
    }
    next_step(){//蛇移动 游戏类内调用两条蛇的next_step函数
        const d=this.direacion;
        this.eye_direction=d;
        this.next_cell=new Cell(this.cells[0].r+this.dr[d],this.cells[0].c+this.dc[d]);
        this.direacion=-1;
        this.status="move";//更新移动状态
        this.step++;
        const k=this.cells.length;
        for(let i=k;i;i--){
            this.cells[i]=JSON.parse(JSON.stringify(this.cells[i-1]));
        }
    }
    update_move(){
        const move_dis=this.speed*this.timedelta/1000;
        const dx=this.next_cell.x-this.cells[0].x;
        const dy=this.next_cell.y-this.cells[0].y;
        const dis=Math.sqrt(dx*dx+dy*dy);
        if(dis<this.eps){
            this.status='idle';
            this.cells[0]=this.next_cell;
            this.next_cell=null;
        }
        else{//移动蛇头
            this.cells[0].x+=move_dis*dx/dis;
            this.cells[0].y+=move_dis*dy/dis;
        }
        if(!this.check_tail_increasing()){//不增长则尾巴缩短
            const k=this.cells.length;
            const tail=this.cells[k-1], tail_target=this.cells[k-2];
            const tdx=tail_target.x-tail.x;
            const tdy=tail_target.y-tail.y;
            if(dis<this.eps){//尾巴已经重叠，删去
                this.cells.pop();
            }
            else{//移动尾巴
                tail.x+=move_dis*tdx/dis;
                tail.y+=move_dis*tdy/dis;
            }
        }
    }
    update(){//更新当前状态，每秒更新60次
        if(this.status==="move"){//移动
            this.update_move();
        }
        this.render();
    }

    render(){
        //使用canvas渲染小蛇当前状态,继承游戏基类每秒渲染60次达到动画效果
    }
```



#### 8.2.地图生成

​	为了防止玩家作弊，地图生成及操作合法性的检验均在后端进行。GameMap类初始化时会随机生成一个中心对称地图，并DFS判断两名玩家是否联通,直至生成联通地图向前端返回地图结果

**地图生成流程图**

![][mapGenerateFlowChart]

```java
    private boolean draw(){//画一次地图
        for(int i=0;i<rows;i++)//初始化
            for(int j=0;j<cols;j++)
                    g[i][j]=0;
        //四周墙
        for(int i=0;i<rows;i++)
            g[i][0]=g[i][cols-1]=1;
        for(int i=0;i<cols;i++)
            g[0][i]=g[rows-1][i]=1;
        //随机墙
        Random random=new Random();
        for(int i=0;i<this.inner_walls_count;i+=4){
            for(int j=0;j<1001;j++){
                int r=random.nextInt(rows);//返回0~x-1
                int c=random.nextInt(cols);
                if(g[r][c]==1||g[rows-r-1][cols-c-1]==1||g[rows-r-1][c]==1||g[r][cols-c-1]==1)continue;
                if((r==rows-2&&c==1)||(r==1&&c==cols-2))continue;
                g[r][c]=g[this.rows-r-1][this.cols-c-1]=g[this.rows-r-1][c]=g[r][this.cols-c-1]=1;
                break;
            }
        }
        for(int i=0;i<rows;i++)
                for(int j=0;j<cols;j++)
                        cpg[i][j]=g[i][j];
        if(is_connected(this.rows-2,1)){
            return true;//判断是否联通，联通则返回合法地图
        }
        return false;
    }
    //Flood Fill判断地图是否合法
    private  boolean is_connected(int x,int y){
        if(x==1&&y==this.cols-2)return true;
        for(int i=0;i<4;i++){
            int a=x+dx[i],b=y+dy[i];
            if(x<1||x==rows-1||y<1||y==cols-1)continue;
            if(cpg[a][b]==1)continue;
            cpg[a][b]=1;
            if(this.is_connected(a,b)) return true;
        }
        return false;
    }
	//生成地图
    public void createMap(){
        for(int i=0;i<1000;i++)
            if(draw())break;
    }
```



#### 8.3.回放录像

​	回放录像集成在游戏类中，每次游戏会判断当前是回放还是正式赛来选择输入信息源,若是回放则解析回放操作序列作为输入

```js
    add_listening_events(){
        //如果是回放解析操作序列
        if(this.store.state.record.is_record){//在state区有记录当前比赛是否为回放
            let k=0;
            const [snake0,snake1]= this.snakes;
            const loser=this.store.state.record.record_loser;
            const a_step=this.store.state.record.a_step;//获取操作序列
            const b_step=this.store.state.record.b_step;
            //每300ms执行
            const interval_id=setInterval(()=>{
                if(k>=a_step.length-1){//如果有死亡
                    if(loser==="all"||loser==="a")
                        snake0.status="die";
                    if(loser==="all"||loser==="b")
                        snake1.status="die";
                    clearInterval(interval_id);
                    
                }else{//展示下一步
                    snake0.set_direction(parseInt(a_step[k]));
                    snake1.set_direction(parseInt(b_step[k]));
                    k++;
                }

            },350)//一秒3步
        }else{
            //不是回放监听键盘输入
        }

    }
```



#### 8.4.AI代码运行及AI逻辑

1. 利用反射机制动态编译运行代码，模拟生产者消费者模型消息队列，添加代码后立即消耗

   ```java
       public void addBot(Integer userId,String botCode,String input){//生产者添加待处理代码
           lock.lock();
           try{
               bots.add(new Bot(userId,botCode,input));//玩家 代码 局面
               condition.signal();//唤醒线程
           }finally {
               lock.unlock();
           }
       }
       private void consume(Bot bot){//消费者
           Consumer consumer =new Consumer();
           consumer.startTimeout(2000L,bot);//最多执行两秒
       }
       public void run(){
           while (true){//模拟消息队列
               lock.lock();
               if(bots.isEmpty()){//等待生产消息
                   try {
                       condition.await();//阻塞当前进程,直到唤醒/结束 自动释放锁
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                       lock.unlock();
                       break;
                   }
               }else {//消费消息
                   Bot bot=bots.remove();
                   lock.unlock();
                   consume(bot);//动态编译运行代码
               }
   
           }
       }
   ```

2. Lev3 AI代码逻辑

   ​	dfs搜所有可行区域，以最远可达块为当前决策估值，每次走估值最高的一步，为减小搜索空间，只会搜7步且不会考虑对方决策
   ```java
       private int[][] now_g= new int[13][14];
       private int eval(int x,int y,int left){//这一步走哪里 剩余搜索长度
           if(left==0)return 0;//剩余步数用尽
           if(now_g[x][y]==1)return 0;//不合法
           int[] dx={-1,0,1,0},dy={0,1,0,-1};
           int ans=0;
           now_g[x][y]=1;//记录当前位置已搜
           for(int i=0;i<4;i++){
               int a=x+dx[i],b=y+dy[i];
               ans=Math.max(ans,eval(a,b,left-1));//搜子区域
           }
           now_g[x][y]=0;//还原现场
           return ans+1;//可行搜索区域估值为其子区域估值+1
       }
       public Integer nextMove(int[][] g,List<Cell> aCells,List<Cell> bCells) {
           for (int i=0;i<13;i++)
               for(int j=0;j<14;j++)
                   now_g[i][j]=g[i][j];//标记搜索数组
           int[] dx={-1,0,1,0},dy={0,1,0,-1};
           int ans=0,res=0;
           for(int i=0;i<4;i++){
               int x=aCells.get(aCells.size()-1).x+dx[i];
               int y=aCells.get(aCells.size()-1).y+dy[i];
               //对当前这步进行估值
               int t=eval(x,y,7);//为限制搜索空间只搜7步
               if(t>res){//当前估值较大更新答案
                   ans=i;res=t;
               }//若多个决策估值相同则随机走一步
               else if(t==res&&(aCells.size()&1)==1){
                   ans=i;res=t;
               }
           }
           return ans;
       }
   ```



#### 8.5.代码编辑区

集成**vue3-ace-editor**依赖

```vue
<script>
//Model API
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
      setup(){
        ace.config.set("basePath","https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
        const store=useStore();
        let bots=ref([]);
        const botadd=reactive({
            title:"",
            description:"",
            content:"",
            error_message:"",
        })
      }
</script>
```



#### 8.6.玩家匹配

​	Backend会将玩家id、AI id、玩家天梯分传给MatchingSystem,系统会将玩家加入匹配池并每秒尝试匹配一次所有玩家，每个玩家有可最大可容忍天梯分差值，随时间推移最大可容忍差值会逐步扩大。匹配过程会优先匹配加入早的玩家

```java
    private void matchPlayers(){
        boolean[] used=new  boolean[players.size()];
        for (int i=0;i<players.size();i++){//优先老玩家 Cn2进行匹配
            if(used[i])continue;
            for(int j=i+1;j<players.size();j++){
                if (used[j])continue;
                if(checkMatched(players.get(i),players.get(j))){//判断两名玩家是否互相可匹配
                    used[i]=used[j]=true;
                    sendResult(players.get(i),players.get(j));//回传匹配成功信息
                    break;
                }
            }
        }
        List<Player> newPlayers =new ArrayList<>();
        for(int i=0;i<players.size();i++){
            if (!used[i])newPlayers.add(players.get(i));
        }
        players=newPlayers;
    }
    @Override
    public void run(){
        while (true){
            try {
                lock.lock();
                try {
                    increaseWaitingTime();//增加当前匹配池所有人等待时间
                    matchPlayers();//尝试匹配
                }finally {
                    lock.unlock();
                }
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
```



#### 8.7.持久化登录

​	配置jwt登录验证模块，玩家登录后为其分配token，存储用户本地，后续验证其token有效性

```javascript
//前端记录token 函数存在state区
	login(contxt,data){
      $.ajax({
        url:"http://127.0.0.1:3000/api/user/account/token/",
        type:"post",
        data: {
          username: data.username,
          password: data.password,
        },
        success(resp){
            if(resp.error_message=="success"){
                  localStorage.setItem("jwt_token",resp.token);//存储用户本地
                  contxt.commit("updateToken",resp.token);//更新state区token
                  data.success(resp);
            }
            else{
                data.error(resp);
            }
        },
        error(resp){
          data.error(resp);
        }
      });
    },
```

```java
//后端登录验证
	protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring(7);
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User user = userMapper.selectById(Integer.parseInt(userid));

        if (user == null) {
            throw new RuntimeException("用户名未登录");
        }
        UserDetailsImpl loginUser = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
```



## 9.技术支持

### 9.1开发工具

​		**后端编译器：IDEA 		前端编译器：VSCode 		数据库：MySQL		版本管理：Git，TortoiseGit**

### 9.2项目依赖

- 后端

  | 依赖                          | 作用                                          | 版本    |
  | ----------------------------- | --------------------------------------------- | ------- |
  | Project Lombok                | 自动生成构造函数                              | 1.18.24 |
  | Spring Boot Starter JDBC      | 连接数据库                                    | 2.7.1   |
  | MySQL Connector/J             | 连接MySQL                                     | 8.0.29  |
  | mybatis-plus-boot-starter     | 添加默认sql语句                               | 3.5.2   |
  | mybatis-plus-generator        | 生成 Mybatis 的实体类、Mapper 接口和 XML 文件 | 3.5.3   |
  | spring-boot-starter-security  | 集成Spring Security                           | 2.7.1   |
  | jjwt-api                      | 集成JWT                                       | 0.11.5  |
  | jjwt-impl                     | 集成JWT                                       | 0.11.5  |
  | jjwt-jackson                  | 集成JWT                                       | 0.11.5  |
  | spring-boot-starter-websocket | 集成websocket                                 | 2.7.2   |
  | fastjson                      | 集成JSON库                                    | 2.0.25  |
  | joor-java-8                   | 简化java反射代码操作，动态编译执行Bot代码     | 0.9.14  |
  
  

- 前端

  | 依赖            | 作用             | 版本   |
  | --------------- | ---------------- | ------ |
  | vue             | 前后端交互       | 3.2.47 |
  | bootstrap       | 格式控制         | 5.2.3  |
  | jquery          | 动态选择html标签 | 3.6.3  |
  | vue-router      | 提供导航栏路由   | 4.1.6  |
  | vuex            | 存储全局状态     | 4.1.0  |
  | vue3-ace-editor | 支持代码编辑框   | 2.2.2  |
  | core-js         | 适配浏览器       | 3.27.2 |
  | @popperjs/core  | 管理css格式      | 2.11.6 |



### 9.3部署环境

CPU与内存：1核(vCPU) 2 GiB

操作系统：Ubuntu  20.04 64位

域名：https://app5356.acapp.acwing.com.cn/







[index]: ./desgin/img/index.jpg
[match]: ./desgin/img/match.jpg
[win]: ./desgin/img/win.jpg
[gameList]: ./desgin/img/gameList.jpg
[rankList]: ./desgin/img/rankList.jpg
[botList]: ./desgin/img/botList.jpg
[editBot]: ./desgin/img/editBot.jpg
[board]: ./desgin/img/board.jpg
[login]: ./desgin/img/login.jpg
[process1]: ./desgin/img/process1.jpg
[UserDemandAnalysis]: ./desgin/img/UserDemandAnalysis.png
[MatchingSystemDemandAnalysis]: ./desgin/img/MatchingSystemDemandAnalysis.png
[BotRunningSystemDemandAnalysis]: ./desgin/img/BotRunningSystemDemandAnalysis.png
[ER]: ./desgin/img/ER.png
[dataRel]: ./desgin/img/dataRel.jpg
[dataFlowDiagramtop]: ./desgin/img/dataFlowDiagramtop.png
[dataFlowDiagram0]: ./desgin/img/dataFlowDiagram0.png
[snakeLifeCycle]: ./desgin/img/snakeLifeCycle.png
[mapGenerateFlowChart]: ./desgin/img/mapGenerateFlowChart.png

[systemArchitecture]: ./desgin/img/systemArchitecture.png
[networdArchitecture]: ./desgin/img/networdArchitecture.png
[moduleDiagram]: ./desgin/img/moduleDiagram.png

