# care helper app

### 0 简介



### 1 TODO

1.文件读取

### 2 指令设计

指令基本结构

```json
{
	"type": "",
  "content": {},  //提交内容结构体
  "send_time": "", // 发送时间
  "version": "",  // 版本号
}
```

包结构

```
{
	status: 0/1/2/3,
	message: "",  // 信息
	command: {}
}
```
