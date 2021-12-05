# Simulation-Client-API
RW-HPS DisCord的Bot衍生产物    
仅供参考  

## API-文档

## GET (/api/get/+Api)
rwping?ip=ip:port (不带端口使用默认5123)  
更多请查看源码
...

### 返回类型

GET/POST 均返回如下

```
{"State":0,"Result":"XXXXX"}
```

State为状态码, 如需知道状态码具体,请看后文
Result为返回数据, 为Json, 但嵌套了一层Base64(为了防中文乱码)


RwPing:
```
{"State":0,"Result":"eyJNYXBOYW1lIjoiW3o7cDEwXUNyb3NzaW5nIExhcmdlICgxMHApIiwiVHJ5Q291bnQiOjMsIkF1dG9HYW1lTmFtZSI6IumTgemUiC3mmJ/ogZTlhajmsYnljJYiLCJQbGF5ZXJMaW1pdCI6MTAsIlByUGluZyI6NTE3LCJDb25uZWN0UGluZyI6MTUwLCJQbGF5ZXJDb3VudCI6MSwiRmFpbHVyZXMiOjAsIlBrZ05hbWUiOiJjb20uY29ycm9kaW5nZ2FtZXMucnRzLnF6In0="}

{"MapName":"[z;p10]Crossing Large (10p)","TryCount":3,"AutoGameName":"铁锈-星联全汉化","PlayerLimit":10,"PrPing":517,"ConnectPing":150,"PlayerCount":1,"Failures":0,"PkgName":"com.corrodinggames.rts.qz"}
```

### 错误码

被Ping服务器于关闭状态  
SERVER_CLOSE(20003,"Server close")

数据返回为空  
DATA_NULL(20005,"Data null")

房间已满  
SERVER_FULL(20006,"Server full")  
更多请查看源码
...