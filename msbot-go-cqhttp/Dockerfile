## 使用编译好的
FROM centos:7
WORKDIR /usr/src/cqhttp

ENV TZ Asia/Shanghai

RUN ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone

CMD ./go-cqhttp -update-protocol -faststart
# CMD ./go-cqhttp -update-protocol -faststart -D


## 自己编译
# FROM golang:1.20

# WORKDIR /usr/src/cqhttp

# ## 使用git源编译
# # RUN rm -rf /usr/src/cqhttp/

# # 阿里云镜像
# # RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# # git 代理
# # RUN git config --global https.proxy http://127.0.0.1:8118
# # RUN git config --global https.proxy https://127.0.0.1:8118


# # git 下载
# # RUN git clone https://github.com/Mrs4s/go-cqhttp.git /usr/src/cqhttp/

# ## 使用本地源编译
# # ADD ./git-1.0.0-rc5/ /usr/src/cqhttp/

# # golang.org代理访问镜像, 编译
# RUN GOPROXY="https://goproxy.cn" go build -ldflags "-s -w -extldflags '-static'"
# # RUN go build -ldflags "-s -w -extldflags '-static'"



# CMD ./go-cqhttp