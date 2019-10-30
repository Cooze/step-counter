#### step-counter
#### 流量计数器、流程计步器
```$xslt
    
    step-source (调用step-client)
          |
          |
          v
    step-reciever (接收日志)
          |
          |
          v
    step-counter (汇总时段内各个字段统计总数)
          |
          |
          v
    step-sink (定时刷入不同的存储系统中)      
```

### 项目依赖组件
    服务端依赖：zookepper 
#### 编译命令
`mvn clean -Dmaven.test.skip=true  package`