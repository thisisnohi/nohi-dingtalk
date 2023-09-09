# nohi-dd-mp
> create by nohi 20230818

钉钉后台服务接口

## 环境
* 控制台： https://open-dev.dingtalk.com/#/index
* API: https://open-dev.dingtalk.com/apiExplorer
* 管理后台： https://oa.dingtalk.com

## 项目运行
* 修改：钉钉应用配置
> application.yml

```yaml
dingtalk:
    mp:
      agentId: xxxxxx
      appKey: aaaaaa
      appSecret: bbbbbb
      dingTalkServer: https://oapi.dingtalk.com
      managerId: ddddd
```

* knif4j: `http://127.0.0.1:8081/dd-mp/doc.html#/home`

### 后端

```
 cd nohi-dd-view
 yarn dev
```
* `http://localhost:3001/vab/#/`
