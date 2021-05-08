Hawk是一个致力于加速软件交付、降低资源使用成本的PAAS平台。通过屏蔽底层知识，使得测试人员、开发人员更容易上手。通过丰富的功能保证可以灵活创建服务实例，全方位了解服务运行状态。

### 功能

项目管理：

- 创建项目：
  - 项目来源： 支持gitlab，通过用户名密码登录获取代码。
  - 构建类型：支持maven，gradle，npm。
  - 运行方式：支持tomcat、springboot、nginx。
- 共享项目：其他部门同事也可以管理此项目，包括构建、运行、查看运行指标等。
- 构建项目：支持全量构建，部分构件(只构建部分应用)，自动部署，查看构建历史信息、进度。

实例管理：

- 新建实例: 基于构建历史创建新的运行实例。
  - springboot 可以指定运行时profile
  - 指定是否开启健康检查、指定健康检查URL
  - 配置可持久化的日志路径
  - 性能级别调整(对应不同的系统参数、及其jvm参数)
  - 支持远程DEBUG
  - JProfiler性能分析
  - 远程SSH
  - 服务网格
  - 启动预执行脚本定义
  - 挂载卷配置
  - Java属性或环境变量配置
  - 管理员配置（只有已配置的管理员可以修改实例）
- 停止、启动、删除实例。
- 查看实例运行指标，包括CPU、内存、负载、网络流量，磁盘、JVM、GC等。

日志查看：

支持3种不同方式查看日志：

1. 平台中直接操作查看，只能看到输出到console的日志。
2. 实例开启SSH功能，可以SSH进入容器中查看。
3. 配置持久化日志路径，将日志输出到指定卷中，再将卷mount到指定vm中统一查看。

### 安装

Hawk不提供服务基础设施的配置或安装，在安装时假设已经存在基础可用的环境及资源，包括：

1. 关闭selinux及其防火墙firewalld

2. 完整可用的kubernetes集群

   - 目前支持版本

   | 1.16 | 测试可用 |
   | ---- | -------- |
   | 1.17 | 测试可用 |
   | 1.18 | 测试可用   |
   | 1.19 | 测试可用   |
   | 1.20 | 测试可用   |

   - ApiServer支持全部端口，并保证80端口不被占用。

     ```shell
     vi /etc/kubernetes/manifests/kube-apiserver.yaml
     在command下面添加：
     - --service-node-port-range=1-65535
     ```

3. kubernetes集群至少要有4C/16G。

4. Helm3.x客户端程序

   使用(`curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash`)来安装。

5. 可用的存储， 并至少保证50Gi可用，下列方式任选其一：

   - NFS

     - 安装nfs server

     - 配置/etc/exports

       ```shell
       /app/nfs/repo *(rw,sync,no_root_squash,no_all_squash)
       ```

     - systemctl restart  nfs

   - GlusterFS, Ceph 等需要配置storageClass

将安装以下内容:

1. Hawk应用服务，基于springcloud。
2. 数据库mysql8.0
3. 消息中间件Kafka+zookeeper
4. 监控Prometheus
5. 缓存Redis
6. 前端H5页面hawk-ui

安装步骤：

1. 解压安装包
2. 查看并配置values.yaml。具体参数项可以查看values.yaml文件的相关注释。
3. 执行(`sh install.sh [namespace]` ), namespace默认为hawk

dns配置：

有内部dns server， 直接配置自定义域名泛域名解析到api sever上面即可，域名要跟values.yaml文件中hawk.host一致，如hawk.cn

没有内部dns server，执行 [dns_patch.sh + 域名] 如`sh dns_patch.sh hawk.cn` ， 域名同样要与values.yaml中的hawk.host一致，执行命令后，会暴露k8s集群内部dns，并开启udp53端口。在本地执行dig hawk.cn @(apiserver ip) 如 dig hawk.cn @192.168.1.100， 检查域名是否指向apiserver。

配置dns server地址编辑/etc/resolv.conf， 添加nameserver (apiserver ip)， 如nameserver 192.168.1.100。

### 卸载

`sh uninstall [namespace]`,namespace默认为hawk

### 构建方式

1. 构建hawk应用

   `sh build.sh verion [path]`

   version: hawk构建后版本号

   path：可以只针对一个模块单独构建, eg: sh build.sh v1.0 base-service

2. 构建依赖

   依赖github地址为 https://github.com/kangta123/hawk-deps

   `sh build-deps-images.sh version` eg sh build-deps-images.sh v1.1

### 使用

假设我们设置域名为hawk.cn，当服务启动成功后便可以访问各服务：

| url          | 说明           |
| ------------ | -------------- |
| hawk.cn      | 访问Hawk网址   |
| prom.hawk.cn | 访问prometheus |

