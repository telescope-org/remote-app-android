# 安卓远程执行命令APP

### 0 简介

这是一款基于安卓的远程控制端App，可以通过App + 控制台进行指令下发并在手机端实现执行，目前已实现远程获取通话记录，获取短信息，读取微信和QQ的下载文件，获取地理位置，音量调整到最大等能力，研发这款APP的初心是想帮助远在家乡的父母，当她们的手机出现问题的时候，我们能够第一时间进行修复，远程获取到手机的状态，考虑到系统的安全性，不打算提供公共服务平台，如有需要，可前往GitHub下载编译，并配置即可。

### 1 使用

由于本APP涉及到的权限过高，故无法在应用商店上架，所以需要开发者自行下载源码，编译为APK自行安装，下面为将叙述完整的安装过程。

#### 1.1 配置腾讯地图SDK

本APP的定位能力是基于腾讯地图SDK实现，故开发者需要自信前往腾讯地图开放平台申请相关定位能力，并获取到SK填写到对应文件中。官网如下：https://lbs.qq.com/

获取到SecKey之后找到项目中的AndroidManifest.xml文件，将SecKey配置到如下位置：

```xml
        <meta-data
            android:name="TencentMapSDK"
            android:value="这里填写官网的腾讯云SDK"/>
```

#### 1.2 配置远程服务端地址

由于本App所有的指令都是基于远程服务端所下发的，所以我们需要配置一个响应的服务端来进行指令的下发。

找到项目中名为：Constant的Java文件，将其中的BaseAPI替换为自己部署的API即可。

```java
public class Constant {
    public final static String BASE_API = "http://服务端地址:9090";
}
```

#### 1.3 编译并使用

完成上述配置之后，我们只需要在Android Studio中编译为一个APK文件并安装到手机即可完成。

### 2 扩展能力

本APP提供完备的可扩展支持，接下来我将描述如何扩展一个新能力。

在service目录下新建一个TargetService并继承自Service，实现doCommand方法。

```java
public class TargetService implements Service {
  
  	public void doTarget() {}
    @Override
    public CommandResponse doCommand(String type) {
        // Target相关业务逻辑
        this.doTarget();
        return null;
    }
}
```

如果需要依赖注入，在CareHelperService的基类的BaseService中添加注入（这里本来想抄Spring的依赖注入的，但是作者有一点懒，所以就写了一个简陋版的依赖注入）

```java
        this.container
                .addService("PhoneService", new PhoneService(context))
                .addService("FileService", new FileService(context, activity))
                .addService("AppService", new AppService(context))
                .addService("LocationService", new LocationService(context))
                .addService("TargetService", new TargetService());
```

当完成注入之后，可以在CareHelperService中实现能力调用。就像下面这样。

```java
    public CareHelperService(Context context, FragmentActivity activity) {
        super(context, activity);
        try {
            this.activity = activity;
            this.commandService = new CommandService();
            this.targetService = ((TargetService) this.container.getService("TargetService"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

最后在MessageHandler那里处理指令即可。

```java
            if ((Integer) jsonObject.get("code") == 200) {
                switch ((Integer) jsonObject.get("type")) {
                    case 0:
           							// todo
                        break;
                    case 1:
                        // todo
                        break;
                    case 2:
                    		// 主要解析命令的地方，我们实现后的也是从这里解析走的
                        JSONObject commands = (JSONObject) jsonObject.get("data");
                        if (commands != null && !commands.isEmpty()) {
                            List<Object> cmds = new ArrayList<>(commands.values());
                            commandService.parseCommandsAndExec(cmds);
                        }
                    default:
                        Log.i("", "");
                }
            }
            // 位于commandService中
            public void parseCommandAndExec(String command) {
                Log.i("command", "doCommand: " + command);
                String type = command.split(":")[0];
                switch (type) {
                    case CommandType.FILE:
                        this.fileService.doCommand(command);
                        break;
                    case CommandType.LOCATION:
                        this.locationService.doCommand(command);
                        break;
                    case CommandType.PHONE:
                        this.phoneService.doCommand(command);
                        break;
                    default:
                        Log.e("CommandService", "type error!");
                        break;
                }
            }
```

### 3 触发指令

```
// 文件读取指令
file:qq  // 读取qq文件列表
file:we   // 读取微信文件列表
// 手机能力
phone:setMaxVolum // 手机音量调到最大
phone:ContactList // 获取联系人记录
phone:callPhone:手机号码 // 拨打电话
phone:PhoneRecords     // 获取通话记录
phone:PhoneMessageList //  获取电话信息
// 定位
location:update // 开启位置更新
location:cancel // 取消位置更新
```

### 4 部署后台服务

TelescopeWeb: https://github.com/telescope-org/telescope-web

### 5 To do list

- 抓取微信聊天记录

### 6 欢迎提建议

github issues即可，会定期维护更新
